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

package bisq.wallets.electrum.rpc.calls;

import bisq.wallets.json_rpc.reponses.JsonRpcStringResponse;
import bisq.wallets.json_rpc.DaemonRpcCall;
import com.squareup.moshi.Json;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class ElectrumGetTransactionRpcCall extends DaemonRpcCall<ElectrumGetTransactionRpcCall.Request, JsonRpcStringResponse> {

    @Getter
    @ToString
    @EqualsAndHashCode
    public static final class Request {
        @Json(name = "txid")
        private final String txId;

        public Request(String txId) {
            this.txId = txId;
        }
    }

    public ElectrumGetTransactionRpcCall(Request request) {
        super(request);
    }

    @Override
    public String getRpcMethodName() {
        return "gettransaction";
    }

    @Override
    public boolean isResponseValid(JsonRpcStringResponse response) {
        return true;
    }

    @Override
    public Class<JsonRpcStringResponse> getRpcResponseClass() {
        return JsonRpcStringResponse.class;
    }
}
