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

package bisq.wallets.elementsd.rpc.calls;

import bisq.wallets.elementsd.rpc.responses.ElementsdGetAddressInfoResponse;
import bisq.wallets.json_rpc.DaemonRpcCall;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ElementsdGetAddressInfoRpcCall
        extends DaemonRpcCall<ElementsdGetAddressInfoRpcCall.Request, ElementsdGetAddressInfoResponse> {

    @Getter
    @ToString
    @EqualsAndHashCode
    public static final class Request {
        private final String address;

        public Request(String address) {
            this.address = address;
        }
    }

    public ElementsdGetAddressInfoRpcCall(Request request) {
        super(request);
    }

    @Override
    public String getRpcMethodName() {
        return "getaddressinfo";
    }

    @Override
    public boolean isResponseValid(ElementsdGetAddressInfoResponse response) {
        return true;
    }

    @Override
    public Class<ElementsdGetAddressInfoResponse> getRpcResponseClass() {
        return ElementsdGetAddressInfoResponse.class;
    }
}
