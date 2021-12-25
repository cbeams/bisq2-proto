/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package network.misq.network.p2p.services.data;

import lombok.extern.slf4j.Slf4j;
import network.misq.common.util.OsUtils;
import network.misq.network.p2p.MockMessage;
import network.misq.network.p2p.node.Address;
import network.misq.network.p2p.node.transport.Transport;
import network.misq.network.p2p.services.data.storage.auth.AddAuthenticatedDataRequest;
import network.misq.network.p2p.services.data.storage.auth.AuthenticatedDataStore;
import network.misq.network.p2p.services.data.storage.auth.MockAuthenticatedPayload;
import network.misq.network.p2p.services.data.storage.auth.MockNetworkData;
import network.misq.network.p2p.services.monitor.MultiNodesSetup;
import network.misq.network.p2p.services.peergroup.PeerGroup;
import network.misq.network.p2p.services.broadcast.BroadcastResult;
import network.misq.security.KeyGeneration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class DataServiceServiceIntegrationTest extends DataServiceNodeBase {
    private final String appDirPath = OsUtils.getUserDataDir() + File.separator + "misq_DataServiceServiceIntegrationTest";
    int numSeeds = 2;
    int numNodes = 4;

    // @Test
    public void testBroadcast() throws InterruptedException, ExecutionException {
        List<DataService> dataServices = getBootstrappedDataServices();
        DataService dataService = dataServices.get(0);
        int minExpectedConnections = numSeeds + numNodes - 2;
        BroadcastResult result = dataService.broadcast(new MockMessage("data1")).get();
        log.error("result={}", result.toString());
        assertTrue(result.numSuccess() >= minExpectedConnections);
    }


    @Test
    public void testAddAuthenticatedDataRequest() throws GeneralSecurityException, InterruptedException, ExecutionException {
        MockNetworkData networkData = new MockNetworkData("test");
        MockAuthenticatedPayload data = new MockAuthenticatedPayload(networkData);
        AuthenticatedDataStore store = new AuthenticatedDataStore(appDirPath, data.getMetaData());
        store.readPersisted().join();
        KeyPair keyPair = KeyGeneration.generateKeyPair();

        AddAuthenticatedDataRequest addRequest = AddAuthenticatedDataRequest.from(store, data, keyPair);
        store.add(addRequest);

        List<DataService> dataServices = getBootstrappedDataServices();
        DataService dataService_0 = dataServices.get(0);
        DataService dataService_1 = dataServices.get(1);
        DataService dataService_2 = dataServices.get(2);


        CountDownLatch latch = new CountDownLatch(2);
        dataService_1.addDataListener(new DataListener() {
            @Override
            public void onNetworkDataAdded(NetworkData networkData) {
                log.error("onNetworkDataAdded at dataService_1");
                latch.countDown();
            }

            @Override
            public void onNetworkDataRemoved(NetworkData networkData) {

            }
        });
        dataService_2.addDataListener(new DataListener() {
            @Override
            public void onNetworkDataAdded(NetworkData networkData) {
                log.error("onNetworkDataAdded at dataService_2");
                latch.countDown();
            }

            @Override
            public void onNetworkDataRemoved(NetworkData networkData) {

            }
        });
        int minExpectedConnections = numSeeds + numNodes - 1;
        BroadcastResult broadcastResult = dataService_0.broadcast(addRequest).get();
        assertTrue(broadcastResult.numSuccess() >= minExpectedConnections);
        latch.countDown();
        log.error("broadcastResult={}", broadcastResult);
        assertTrue(latch.await(30, TimeUnit.SECONDS));
    }

    private List<DataService> getBootstrappedDataServices() throws InterruptedException {
        Transport.Type type = Transport.Type.CLEAR;

        Map<Transport.Type, List<Address>> allNodes = bootstrapMultiNodesSetup(Set.of(type), numSeeds, numNodes);

        List<Address> nodes = MultiNodesSetup.getNodeAddresses(type, numNodes);
        List<DataService> dataServices = multiNodesSetup.getNetworkServicesByAddress().entrySet().stream()
                .filter(e -> nodes.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(e -> e.findServiceNode(type).stream())
                .flatMap(e -> e.getDataService().stream())
                .collect(Collectors.toList());

        DataService dataService = dataServices.get(0);
        int expectedConnections = numSeeds + numNodes - 2;
        PeerGroup peerGroup = dataService.getPeerGroupService().getPeerGroup();
        while (true) {
            long numCon = peerGroup.getAllConnections().count();
            if (numCon >= expectedConnections) {
                break;
            }
            Thread.sleep(5000);
        }

        return dataServices;
    }
}
