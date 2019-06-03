/*
 * Copyright (c) 2017 m2049r
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exawallet.model;

import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.utils.HLog;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.Wallet.Status.Status_Ok;

public class Wallet extends ModelInitializer {
    private static final String TAG = Wallet.class.getSimpleName();
    private long handle;

    private long listenerHandle = 0;
    private final WalletMeta walletMeta;

    Wallet(long handle, WalletMeta walletMeta) {
        this.handle = handle;
        this.walletMeta = walletMeta;
    }

    public String getName() {
        return new File(getPath()).getName();
    }

    public long getUnconfirmedBalance() {
        return getBalance() - getUnlockedBalance();
    }

    public WalletMeta getWalletMeta() {
        return walletMeta;
    }

    public enum Status {
        Status_Ok,
        Status_Error,
        Status_Critical
    }

    public enum ConnectionStatus {
        ConnectionStatus_Disconnected,
        ConnectionStatus_Connected,
        ConnectionStatus_WrongVersion
    }

    public native String getSeed();

    public native String getSeedLanguage();

    public native void setSeedLanguage(String language);

    public Status getStatus() {
        Status value = Status.values()[getStatusJ()];

        HLog.debug(TAG, "getStatus " + value.name());

        return value;
    }

    private native int getStatusJ();

    public native String getErrorStringJ();

    String getErrorString() {
        try {
            String errorStringJ = getErrorStringJ();
            HLog.debug(TAG, "getErrorString " + errorStringJ);

            return errorStringJ;
        } catch (Throwable e) {
            String message = e.getMessage();
            HLog.debug(TAG, "getErrorString throwable " + message);

            return message;
        }
    }

    public native boolean setPassword(String password);

    public String getAddress() {
        return getAddressJ();
    }

    private native String getAddressJ();

    public native String getPath();

    public NetworkType getNetworkType() {
        return NetworkType.fromInteger(nettype());
    }

    public native int nettype();

//TODO virtual void hardForkInfo(uint8_t &version, uint64_t &earliest_height) const = 0;
//TODO virtual bool useForkRules(uint8_t version, int64_t early_blocks) const = 0;

    public native String getIntegratedAddress(String payment_id);

    public native String getSecretViewKey();

    public native String getPublicViewKey();

    public native String getSecretSpendKey();

    public native String getPublicSpendKey();

    public native String getPublicMultisigSignerKey();

    public native String getMultisigInfo();

    public native String signMessage(String message);

    public native String signMultisigParticipant(String message);

    public native String makeMultisigJ(String[] keys, int required);

    public native String exchangeMultisigKeysJ(String[] keys);

    public native boolean finalizeMultisigJ(String[] keys);

    private native String exportMultisigImagesJ();

    private native int importMultisigImagesJ(String[] keys);

    public native boolean hasMultisigPartialKeyImages();

    public synchronized boolean store() {
        return storeByPath("");
    }

    private synchronized boolean storeByPath(String path) {
        HLog.debug(TAG, "store");
        return store(path);
    }

    private native boolean store(String path);

    public void close() {
        pauseRefreshJ();
    }

    void collapse() {
    }

    private native String getFilename();

    private native boolean initJ(String daemonAddress, long upperTransactionSizeLimit, String daemonUsername, String daemonPassword);

    boolean initialize(WalletMeta walletMeta, long upperTransactionSizeLimit) throws GeneralSecurityException, IOException {
        HLog.debug(TAG, String.format("initialize %s", walletMeta.getName()));
        boolean result = initJ(walletMeta.getDaemonAddress(), upperTransactionSizeLimit, walletMeta.getDaemonUsername(), walletMeta.getDaemonPassword());

        if (result && Status_Ok == getStatus()) {
            setTrustedDaemon(true);
            if (Status_Ok == getStatus()) {
//                useFastSync();
                if (Status_Ok == getStatus()) {
                    walletMeta.updateAddress(getAddress());
                    if (Status_Ok == getStatus()) {
                        return result;
                    }
                }
            }
        }

        throw new GeneralSecurityException(getErrorString());
    }

    public String exportMultisigImages() {
        String result = exportMultisigImagesJ();
        return Status_Ok == getStatus() ? result : null;
    }

    public void importMultisigImages(String[] outputs) throws GeneralSecurityException {
        int result = importMultisigImagesJ(outputs);

        if (Status_Ok != getStatus()) {
            throw new GeneralSecurityException(getErrorString());
        }
    }

//    virtual bool createWatchOnly(const std::string &path, const std::string &password, const std::string &language) const = 0;
//    virtual void setRefreshFromBlockHeight(uint64_t refresh_from_block_height) = 0;
//    virtual void setRecoveringFromSeed(bool recoveringFromSeed) = 0;
//    virtual bool connectToDaemon() = 0;

    ConnectionStatus getConnectionStatus() {
        return ConnectionStatus.values()[getConnectionStatusJ()];
    }

    private native int getConnectionStatusJ();

    public native void setTrustedDaemon(boolean arg);

    public native boolean trustedDaemon();

    private native long getBalance();

    public native long getUnlockedBalance();

    public native boolean isWatchOnly();

    public native long getBlockChainHeight();

    public native long getApproximateBlockChainHeight();

    public native long getDaemonBlockChainHeight();

    public native long getDaemonBlockChainTargetHeight();

    public native boolean isSynchronized();

    public static native String getDisplayAmount(long amount);

    public static native long getAmountFromString(String amount);

    public static native long getAmountFromDouble(double amount);

    public static native String generatePaymentId();

    public static native boolean isPaymentIdValid(String paymentId);

    public static boolean isAddressValid(WalletMeta walletMeta) {
        return isAddressValid(walletMeta.getDaemonAddress(), walletMeta.getNetworkType().getValue());
    }

    public static native boolean isAddressValid(String address, int networkType);

//TODO static static bool keyValid(const std::string &secret_key_string, const std::string &address_string, bool isViewKey, bool testnet, std::string &error);

    public static native String getPaymentIdFromAddress(String address, boolean isTestNet);

    public static native long getMaximumAllowedAmount();

    native void startRefreshJ();

    native void pauseRefreshJ();

    public native boolean refresh();

    public native void refreshAsync();

    public native boolean rescanBlockchain();

    public native void rescanBlockchainAsync();

//TODO virtual void setAutoRefreshInterval(int millis) = 0;
//TODO virtual int autoRefreshInterval() const = 0;

    public void startRefresh() {
        startRefreshJ();
    }

    private void disposePendingTransaction(PendingTransaction pendingTransaction) {
        if (pendingTransaction != null) {
            disposeTransaction(pendingTransaction);
        }
    }

    public String[] sendMultisigTransaction(TxProposalsMeta txProposalsMeta, String signedTransaction) throws Exception {
        if (native_library_loaded) {
            PendingTransaction pendingTransaction = new PendingTransaction(restoreMultisigTransactionJ(signedTransaction));
            if (txProposalsMeta.isSended()) {
                return getTxIdJ(pendingTransaction);
            } else {
                String[] result = commitTransaction(pendingTransaction);
                txProposalsMeta.setSended();
                return result;
            }
        } else {
            throw new GeneralSecurityException("The exawallet library are not loaded");
        }
    }

    public String[] sendTransaction(String dstAddr, String paymentId, String paymentNote, long amount, int mixinCount, PendingTransaction.Priority priority) throws Exception {
        if (native_library_loaded) {
            PendingTransaction pendingTransaction = new PendingTransaction(createTransactionJ(dstAddr, paymentId, amount, mixinCount, priority.getValue()));

            if (!isEmpty(paymentNote)) {
                setUserNote(pendingTransaction.getFirstTxId(), paymentNote);
            }

            return commitTransaction(pendingTransaction);
        } else {
            throw new GeneralSecurityException("The exawallet library are not loaded");
        }
    }

    private String[] commitTransaction(PendingTransaction pendingTransaction) throws GeneralSecurityException {
        if (pendingTransaction.getStatus() == PendingTransaction.Status.Status_Ok) {
            pendingTransaction.commit();
            if (pendingTransaction.getStatus() == PendingTransaction.Status.Status_Ok) {
                return getTxIdJ(pendingTransaction);
            }
        }

        disposePendingTransaction(pendingTransaction);
        throw new GeneralSecurityException(pendingTransaction.getErrorString());
    }

    private String[] getTxIdJ(PendingTransaction pendingTransaction) throws GeneralSecurityException {
        String[] result = pendingTransaction.getTxIdJ();
        if (pendingTransaction.getStatus() == PendingTransaction.Status.Status_Ok) {
            disposePendingTransaction(pendingTransaction);
            return result;
        }

        disposePendingTransaction(pendingTransaction);
        throw new GeneralSecurityException(pendingTransaction.getErrorString());
    }

    public String createMultisigTransaction(String dstAddr, String paymentId, String paymentNote, long amount, int mixinCount, PendingTransaction.Priority priority) throws Exception {
        if (native_library_loaded) {
            PendingTransaction pendingTransaction = new PendingTransaction(createTransactionJ(dstAddr, paymentId, amount, mixinCount, priority.getValue()));
            if (pendingTransaction.getStatus() == PendingTransaction.Status.Status_Ok) {
                if (!isEmpty(paymentNote)) {
                    setUserNote(pendingTransaction.getFirstTxId(), paymentNote);
                }

                String signedTransaction = pendingTransaction.getSignTransaction();
                if (pendingTransaction.getStatus() == PendingTransaction.Status.Status_Ok) {
                    disposePendingTransaction(pendingTransaction);
                    return signedTransaction;
                }
            }

            disposePendingTransaction(pendingTransaction);
            throw new GeneralSecurityException(getErrorString());
        } else {
            throw new GeneralSecurityException("The exawallet library are not loaded");
        }
    }

    public String restoreMultisigTransaction(String signedTransaction) throws Exception {
        if (native_library_loaded) {
            PendingTransaction pendingTransaction = new PendingTransaction(restoreMultisigTransactionJ(signedTransaction));
            String errorString = getErrorString();
            if (getStatus() == Status.Status_Ok) {
                String signTransaction = pendingTransaction.signTransaction();

                errorString = pendingTransaction.getErrorString();
                if (pendingTransaction.getStatus() == PendingTransaction.Status.Status_Ok) {
                    disposePendingTransaction(pendingTransaction);
                    return signTransaction;
                }
            }
            disposePendingTransaction(pendingTransaction);

            throw new GeneralSecurityException(errorString);
        } else {
            throw new GeneralSecurityException("The exawallet library are not loaded");
        }
    }

    private native long createTransactionJ(String dst_addr, String payment_id, long amount, int mixin_count, int priority);

    public native long restoreMultisigTransactionJ(String signedTransaction);

    public PendingTransaction createSweepUnmixableTransaction() {
        return new PendingTransaction(createSweepUnmixableTransactionJ());
    }

    private native long createSweepUnmixableTransactionJ();

//virtual UnsignedTransaction * loadUnsignedTx(const std::string &unsigned_filename) = 0;
//virtual bool submitTransaction(const std::string &fileName) = 0;

    public native void disposeTransaction(PendingTransaction pendingTransaction);

//virtual bool exportKeyImages(const std::string &filename) = 0;
//virtual bool importKeyImages(const std::string &filename) = 0;


//virtual TransactionHistory * history() const = 0;

    private TransactionHistory history = null;

    public TransactionHistory getHistory() {
        if (history == null) {
            history = new TransactionHistory(getHistoryJ());
        }
        return history;
    }

    private native long getHistoryJ();

//virtual AddressBook * addressBook() const = 0;
//virtual void setListener(WalletListener *) = 0;

    private native long setListenerJ(WalletListener listener);

    void setListener(WalletListener listener) {
        this.listenerHandle = setListenerJ(listener);
    }

    public native int getDefaultMixin();

    public native void setDefaultMixin(int mixin);

    public native boolean setUserNote(String txid, String note);

    public native String getUserNote(String txid);

    public native String getTxKey(String txid);

//    public native void useFastSync();

//    public native void useStandardSync();

//virtual std::string signMessageJ(const std::string &message) = 0;
//virtual bool verifySignedMessage(const std::string &message, const std::string &addres, const std::string &signature) const = 0;

//virtual bool parse_uri(const std::string &uri, std::string &address, std::string &payment_id, uint64_t &tvAmount, std::string &tx_description, std::string &recipient_name, std::vector<std::string> &unknown_parameters, std::string &error) = 0;
//virtual bool rescanSpent() = 0;
}