package com.exawallet;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.crashlytics.android.Crashlytics;
import com.exawallet.fragments.*;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.listeners.OnQRScanListener;
import com.exawallet.utils.Screens;
import io.fabric.sdk.android.Fabric;

import static com.exawallet.api.APIManager.API;
import static com.exawallet.common.Analytics.ANALYTICS;
import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.common.AppContext.setCurrentWalletAddress;
import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.presenters.operations.LoadContentOperation.loadContent;
import static com.exawallet.presenters.operations.LoadWordListOperation.loadWordList;
import static com.exawallet.sequences.SyncWalletSequence.startWalletSync;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.MESSAGE;
import static com.exawallet.utils.Utils.selectWalletScreen;

public class RootActivity extends AppCompatActivity {
    private static final String TAG = RootActivity.class.getSimpleName();

    private static final String TAG_CONTAINER = "container";
    private OnQRScanListener mQRScanListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_view);
        Fabric.with(this, new Crashlytics());

        ANALYTICS.init(getApplicationContext(), this);

        APP_CONTEXT.load(getFilesDir());

        API.init(this);
        ENGINE.restart();

        APP_CONTEXT.setDeviceUid(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        loadWordList(this);
        loadContent(this);
        startWalletSync(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (APP_CONTEXT.onRootActivityExpand()) {
            show(CheckPinFragment.newInstance());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        APP_CONTEXT.notifyCollapseTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        APP_CONTEXT.save(getFilesDir());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ENGINE.shutdown();
        walletManager().finishWallet();
        setCurrentWalletAddress(null);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mQRScanListener && mQRScanListener.onActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public synchronized void show(final BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (null != fragmentManager && !fragmentManager.isDestroyed()) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (null == fragmentManager.findFragmentByTag(TAG_CONTAINER)) {
                fragmentTransaction.add(R.id.container, fragment, TAG_CONTAINER);
            } else {
                fragmentTransaction.replace(R.id.container, fragment, TAG_CONTAINER);
            }

            fragmentTransaction.commit();
        }
    }

    public void goBack() {
        goScreen(BACK_PATH.getScreen());
    }

    public void goCurrent() {
        goScreen(BACK_PATH.getCurrent());
    }

    public void goBack(String message) {
        goScreen(message, BACK_PATH.getScreen());
    }

    public void goCurrent(String message) {
        goScreen(message, BACK_PATH.getCurrent());
    }

    public void goScreen(String message, Screens screen) {
        screen.pushObject(MESSAGE, message);
        goScreen(screen);
    }

    @Override
    public void onBackPressed() {
        if (BACK_PATH.isEmpty()) {
            super.onBackPressed();
        } else {
            goBack();
        }
    }

    private void goScreen(Screens screen) {
        if (null == screen) {
            loadContent(this);
        } else {
            switch (screen) {
                case ACCESS_WALLET: {
                    show(AccessWalletFragment.newInstance());
                    break;
                }
                case CREATE_PRIVATE_WALLET: {
                    show(CreatePersonalWalletFragment.newInstance());
                    break;
                }
                case CREATE_SHARED_WALLET: {
                    show(CreateSharedWalletFragment.newInstance());
                    break;
                }
                case SHOW_MNEMONIC: {
                    show(ShowMnemonicFragment.newInstance());
                    break;
                }
                case EDIT_WALLET: {
                    show(EditWalletFragment.newInstance());
                    break;
                }
                case SINGLE_MANAGE: {
                    show(SingleManageFragment.newInstance());
                    break;
                }
                case MANAGE_WALLET: {
                    show(WalletManageFragment.newInstance());
                    break;
                }
                case CHART: {
                    show(ChartFragment.newInstance());
                    break;
                }
                case SEND: {
                    show(SendFragment.newInstance());
                    break;
                }
                case RECEIVE: {
                    show(ReceiveFragment.newInstance());
                    break;
                }
                case INSERT_INVITE_CODE: {
                    show(InsertInviteCodeFragment.newInstance());
                    break;
                }
                case JOIN_SHARED_WALLET: {
                    show(JoinSharedWalletFragment.newInstance());
                    break;
                }
                case CONFIRM_MNEMONIC: {
                    show(ConfirmMnemonicFragment.newInstance());
                    break;
                }
                case SUCCESS_MNEMONIC: {
                    show(SuccessMnemonicFragment.newInstance());
                    break;
                }
                case RESTORE_WALLET: {
                    show(RestoreWalletFragment.newInstance());
                    break;
                }
                case RESTORE_MNEMONIC: {
                    show(RestoreMnemonicFragment.newInstance());
                    break;
                }
                case SETTINGS: {
                    show(SettingsFragment.newInstance());
                    break;
                }
                case SPLASH: {
                    show(SplashFragment.newInstance());
                    break;
                }
                case SELECT_WALLET_SCREEN: {
                    selectWalletScreen(walletManager().getWallet().getWalletMeta(), this);
                    break;
                }
                case WALLETS: {
                    loadContent(this);
                    break;
                }
                default: {
                    loadContent(this);
                    break;
                }
            }
        }
    }

    public void setOnQRScanListener(OnQRScanListener onQRScanListener) {
        mQRScanListener = onQRScanListener;
    }
}