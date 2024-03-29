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

package bisq.wallets.elementsd.rpc;

import bisq.wallets.bitcoind.rpc.BitcoindWallet;
import bisq.wallets.bitcoind.rpc.calls.BitcoindGetNewAddressRpcCall;
import bisq.wallets.bitcoind.rpc.calls.BitcoindSignMessageRpcCall;
import bisq.wallets.bitcoind.rpc.calls.BitcoindVerifyMessageRpcCall;
import bisq.wallets.bitcoind.rpc.calls.BitcoindWalletLockRpcCall;
import bisq.wallets.core.model.AddressType;
import bisq.wallets.elementsd.rpc.calls.*;
import bisq.wallets.elementsd.rpc.responses.*;
import bisq.wallets.json_rpc.JsonRpcClient;

import java.util.List;
import java.util.Optional;

public class ElementsdWallet {
    private final JsonRpcClient rpcClient;

    public ElementsdWallet(JsonRpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public String claimPegin(Optional<String> passphrase, String bitcoinTxId, String txOutProof) {
        walletPassphrase(passphrase);

        var request = ElementsdClaimPeginRpcCall.Request.builder()
                .bitcoinTxId(bitcoinTxId)
                .txOutProof(txOutProof)
                .build();
        var rpcCall = new ElementsdClaimPeginRpcCall(request);
        String result = rpcClient.call(rpcCall).getResult();

        walletLock();
        return result;
    }

    public double getLBtcBalance() {
        return getAssetBalance("bitcoin");
    }

    public double getAssetBalance(String assetLabel) {
        var rpcCall = new ElementsdGetBalancesRpcCall();
        ElementsdGetBalancesResponse response = rpcClient.call(rpcCall);
        ElementsdGetMineBalances mineBalancesResponse = response.getResult().getMine();

        double trustedBalance = mineBalancesResponse.getTrusted().getOrDefault(assetLabel, 0.);
        double pendingBalance = mineBalancesResponse.getUntrustedPending().getOrDefault(assetLabel, 0.);
        return trustedBalance + pendingBalance;
    }

    public ElementsdGetAddressInfoResponse getAddressInfo(String address) {
        var request = new ElementsdGetAddressInfoRpcCall.Request(address);
        var rpcCall = new ElementsdGetAddressInfoRpcCall(request);
        return rpcClient.call(rpcCall);
    }

    public String getNewAddress(AddressType addressType, String label) {
        var request = BitcoindGetNewAddressRpcCall.Request.builder()
                .addressType(addressType.getName())
                .label(label)
                .build();
        var rpcCall = new BitcoindGetNewAddressRpcCall(request);
        return rpcClient.call(rpcCall).getResult();
    }

    public ElementsdGetPeginAddressResponse getPeginAddress() {
        var rpcCall = new ElementsdGetPeginAddressRpcCall();
        return rpcClient.call(rpcCall);
    }

    public ElementsdIssueAssetResponse issueAsset(Optional<String> passphrase, double assetAmount, double tokenAmount) {
        walletPassphrase(passphrase);

        var request = ElementsIssueAssetRpcCall.Request.builder()
                .assetAmount(assetAmount)
                .tokenAmount(tokenAmount)
                .build();
        var rpcCall = new ElementsIssueAssetRpcCall(request);
        ElementsdIssueAssetResponse response = rpcClient.call(rpcCall);

        walletLock();
        return response;
    }

    public List<ElementsdListTransactionsResponse.Entry> listTransactions(int count) {
        var request = ElementsdListTransactionsRpcCall.Request.builder()
                .count(count)
                .build();
        var rpcCall = new ElementsdListTransactionsRpcCall(request);
        return rpcClient.call(rpcCall).getResult();
    }

    public List<ElementsdListUnspentResponse.Entry> listUnspent() {
        var rpcCall = new ElementsdListUnspentRpcCall();
        return rpcClient.call(rpcCall).getResult();
    }

    public String sendLBtcToAddress(Optional<String> passphrase, String address, double amount) {
        return sendAssetToAddress(passphrase, "bitcoin", address, amount);
    }

    public String sendAssetToAddress(Optional<String> passphrase, String assetLabel, String address, double amount) {
        walletPassphrase(passphrase);

        var request = ElementsdSendToAddressRpcCall.Request.builder()
                .assetLabel(assetLabel)
                .address(address)
                .amount(amount)
                .build();
        var rpcCall = new ElementsdSendToAddressRpcCall(request);
        String txId = rpcClient.call(rpcCall).getResult();

        walletLock();
        return txId;
    }

    public String signMessage(Optional<String> passphrase, String address, String message) {
        walletPassphrase(passphrase);

        var request = BitcoindSignMessageRpcCall.Request.builder()
                .address(address)
                .message(message)
                .build();
        var rpcCall = new BitcoindSignMessageRpcCall(request);
        String signature = rpcClient.call(rpcCall).getResult();

        walletLock();
        return signature;
    }

    public String unblindRawTransaction(String rawTxInHex) {
        var request = new ElementsdUnblindRawTransactionRpcCall.Request(rawTxInHex);
        var rpcCall = new ElementsdUnblindRawTransactionRpcCall(request);
        return rpcClient.call(rpcCall).getResult().getHex();
    }

    public boolean verifyMessage(String address, String signature, String message) {
        var request = BitcoindVerifyMessageRpcCall.Request.builder()
                .address(address)
                .signature(signature)
                .message(message)
                .build();
        var rpcCall = new BitcoindVerifyMessageRpcCall(request);
        return rpcClient.call(rpcCall).getResult();
    }

    public void walletLock() {
        var rpcCall = new BitcoindWalletLockRpcCall();
        rpcClient.call(rpcCall);
    }

    private void walletPassphrase(Optional<String> passphrase) {
        BitcoindWallet.walletPassphrase(rpcClient, passphrase);
    }
}
