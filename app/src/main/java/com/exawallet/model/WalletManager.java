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

import android.graphics.Bitmap;
import com.exawallet.common.QRAddress;
import com.exawallet.common.WalletNewBlock;
import com.exawallet.common.WalletRefreshed;
import com.exawallet.model.meta.WalletMeta;
import com.exawallet.utils.HLog;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.exawallet.model.NetworkType.NETWORK_TYPE_MAINNET;
import static com.exawallet.model.Wallet.Status.Status_Ok;
import static com.exawallet.model.WalletStatus.CONFIRMED;
import static java.lang.System.currentTimeMillis;

public class WalletManager extends ModelInitializer {
    private final static String TAG = "com.exawallet.model.WalletManager";
    // no need to keep a reference to the REAL walletManager (we get it every tvTime we need it)
    private static WalletManager Instance = null;

    public static final int MIN_PASSWORD_LENGTH = -1;

    private final EventBus mEventBus = EventBus.getDefault();

    public static synchronized WalletManager walletManager() {
        if (WalletManager.Instance == null) {
            WalletManager.Instance = new WalletManager();
        }
        return WalletManager.Instance;
    }

    private Wallet managedWallet = null;

    public Wallet getWallet() {
        return managedWallet;
    }

    private native long createWalletFromKeysJ(String path, String password, String language, int networkType, long restoreHeight, String addressString, String viewKeyString, String spendKeyString);

    public boolean walletExists(File file) {
        return walletExists(file.getAbsolutePath());
    }

    public native boolean walletExists(String path);

    public native boolean verifyWalletPassword(String keys_file_name, String password, boolean watch_only);

    public void setQRAddress(Bitmap result) {
        managedWallet.getWalletMeta().setQRAddress(result);
        mEventBus.post(new QRAddress(result));
    }

    public void setQRInviteCode(Bitmap result) {
        managedWallet.getWalletMeta().getSharedMeta().getCreatingMeta().setQRInviteCode(result);
    }

    //public native List<String> findWallets(String path); // this does not work - some error in boost

    public List<WalletMeta> findWallets(File path) {
        List<WalletMeta> wallets = new ArrayList<>();
        HLog.debug(TAG, String.format("Scanning: %s", path.getAbsolutePath()));

        File[] found = path.listFiles((dir, filename) -> filename.endsWith(".wpr"));

        for (File aFound : found) {
            try {
                WalletMeta walletMeta = new WalletMeta(aFound);
                if (CONFIRMED == walletMeta.getStatus() && NETWORK_TYPE_MAINNET == walletMeta.getNetworkType()) {
                    wallets.add(walletMeta);
                }
            } catch (Exception e) {
                HLog.error(TAG, "The file " + aFound + " has been broken", e);
            }
        }
        return wallets;
    }

    private native String getErrorStringJ();

    public String getErrorString() {
        try {
            return getErrorStringJ();
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

//TODO virtual bool checkPayment(const std::string &address, const std::string &txid, const std::string &txkey, const std::string &daemon_address, uint64_t &received, uint64_t &height, std::string &error) const = 0;

    public void setDaemon(WalletMeta walletMeta) {
        HLog.debug(TAG, String.format("setDaemon %s", walletMeta.getName()));
        setDaemonAddressJ(walletMeta.getDaemonAddress());
    }

    private native void setDaemonAddressJ(String address);

    public native int getDaemonVersion();

    public native long getBlockchainHeight();

    public native long getBlockchainTargetHeight();

    public native long getNetworkDifficulty();

    public native double getMiningHashRate();

    public native long getBlockTarget();

    public native boolean isMining();

    public native boolean startMining(String address, boolean background_mining, boolean ignore_battery);

    public native boolean stopMining();

    public native String resolveOpenAlias(String address, boolean dnssec_valid);

    public Wallet.ConnectionStatus getConnectionStatus() {
        return null == managedWallet ? Wallet.ConnectionStatus.ConnectionStatus_Disconnected : managedWallet.getConnectionStatus();
    }

    private Wallet recoveryWalletFromKeys(WalletMeta walletMeta, String password, String language, long restoreHeight, String addressString, String viewKeyString, String spendKeyString) throws Exception {
        return processWallet(walletMeta, () -> createWalletFromKeysJ(walletMeta.getFilePath(), password, language, walletMeta.getNetworkType().getValue(), restoreHeight, addressString, viewKeyString, spendKeyString));
    }

    public Wallet recoveryWallet(WalletMeta walletMeta, String mnemonic, long restoreHeight, String password) throws Exception {
        return processWallet(walletMeta, () -> recoveryWalletJ(walletMeta.getFilePath(), password, mnemonic, walletMeta.getNetworkType().getValue(), restoreHeight));
    }

    public Wallet createWallet(WalletMeta walletMeta, String password) throws Exception {
        return processWallet(walletMeta, () -> createWalletJ(walletMeta.getFilePath(), password, walletMeta.getLanguage(), walletMeta.getNetworkType().getValue()));
    }

    public Wallet openWallet(WalletMeta walletMeta, String password) throws Exception {
        return processWallet(walletMeta, () -> openWalletJ(walletMeta.getFilePath(), password, walletMeta.getNetworkType().getValue()));
    }

    private native long recoveryWalletJ(String path, String password, String mnemonic, int networkType, long restoreHeight);

    private native long createWalletJ(String path, String password, String language, int networkType);

    private native long openWalletJ(String path, String password, int networkType);

    private Wallet processWallet(WalletMeta walletMeta, Callable<Long> callable) throws Exception {
        if (native_library_loaded) {
            finishWallet();
            setDaemon(walletMeta);
            HLog.debug(TAG, String.format("processWallet %s", walletMeta.getName()));
            if (Status_Ok == (managedWallet = new Wallet(callable.call(), walletMeta)).getStatus()) {
                HLog.debug(TAG, String.format("processWallet %s Status_Ok ", walletMeta.getName()));
                managedWallet.setListener(new WalletListener() {
                    private long mLastBlock = 0;
                    private long mCurrentMaxHeight = 0;
                    private long mLastUpdate = 0;

                    @Override
                    public void moneySpent(String txId, long amount) {
                        HLog.debug(TAG, "WalletListener moneySpent " + txId + " " + amount);
                    }

                    @Override
                    public void moneyReceived(String txId, long amount) {
                        HLog.debug(TAG, "WalletListener moneyReceived " + txId + " " + amount);
                    }

                    @Override
                    public void unconfirmedMoneyReceived(String txId, long amount) {
                        HLog.debug(TAG, "WalletListener unconfirmedMoneyReceived " + txId + " " + amount);
                    }

                    @Override
                    public void newBlock(long height) {
                        HLog.debug(TAG, "WalletListener newBlock " + height + " " + mCurrentMaxHeight);
                        if (500 < currentTimeMillis() - mLastUpdate || height == mCurrentMaxHeight) {
                            managedWallet.getWalletMeta().setLastUpdated(mLastUpdate = currentTimeMillis());
                            mEventBus.post(new WalletNewBlock(mLastBlock = Math.max(mLastBlock, mCurrentMaxHeight = managedWallet.getDaemonBlockChainHeight() - 1), mCurrentMaxHeight, height));
                        }
                    }

                    @Override
                    public void updated() {
                        HLog.debug(TAG, "WalletListener updated");
                    }

                    @Override
                    public void refreshed() {
                        HLog.debug(TAG, "WalletListener refreshed " + getConnectionStatus().name());

                        if (Status_Ok != managedWallet.getStatus()) {
                            HLog.debug(TAG, "WalletListener refreshed Status_Error " + managedWallet.getErrorString());
                        } else {
                            managedWallet.store();
                            try {
                                managedWallet.getWalletMeta().store();
                            } catch (IOException e) {
                                HLog.error(TAG, "Can't save walletMeta", e);
                            }
                            managedWallet.getHistory().refresh();
                        }

                        mEventBus.post(new WalletRefreshed());
                    }
                });
                managedWallet.initialize(walletMeta, 0L);
                return managedWallet;
            }

            throw new GeneralSecurityException(managedWallet.getErrorString());
        } else {
            throw new GeneralSecurityException("The exawallet library are not loaded");
        }
    }

    private native boolean closeJ(Wallet wallet);

    public synchronized boolean finishWallet() {
        HLog.debug(TAG, "finishWallet");
        if (null != managedWallet) {
            managedWallet.close();

            if (closeJ(managedWallet)) {
                managedWallet = null;
                return true;
            }
            return false;
        }
        return true;
    }

    public synchronized boolean collapseWallet() {
        HLog.debug(TAG, "collapseWallet");
        if (null != managedWallet) {
            managedWallet.collapse();

            if (closeJ(managedWallet)) {
                managedWallet = null;
                return true;
            }
            return false;
        }
        return true;
    }

    public void startRefresh() {
        HLog.debug(TAG, "startRefresh " + (null != managedWallet ? managedWallet.getAddress() : null));
        if (null != managedWallet) {
            managedWallet.startRefreshJ();
        }
    }

    public void stopRefresh() {
        HLog.debug(TAG, "stopRefresh " + (null != managedWallet ? managedWallet.getAddress() : null));
        if (null != managedWallet) {
            managedWallet.pauseRefreshJ();
        }
    }

    //TODO static std::tuple<bool, std::string, std::string, std::string, std::string> checkUpdates(const std::string &software, const std::string &subdir);
}