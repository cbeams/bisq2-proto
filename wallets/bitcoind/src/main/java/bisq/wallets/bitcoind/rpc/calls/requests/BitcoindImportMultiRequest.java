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

package bisq.wallets.bitcoind.rpc.calls.requests;

import com.squareup.moshi.Json;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class BitcoindImportMultiRequest {
    private String desc;
    private Map<String, String> scriptPubKey;
    private final String timestamp = "now";
    @Json(name = "redeemscript")
    private String redeemScript;
    @Json(name = "witnessscript")
    private String witnessScript;
    @Json(name = "pubkeys")
    private List<String> pubKeys;
    private List<String> keys;
}
