package com.exawallet.common;

import com.exawallet.model.NetworkType;
import com.exawallet.utils.HLog;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.NetworkType.NETWORK_TYPE_MAINNET;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.currentTimeMillis;

public enum AppContext {
    APP_CONTEXT;

    private static final String TAG = AppContext.class.getSimpleName();

    static final String API_STAGE_URL = "https://mws-stage.exan.tech/api/v1/";
    static final String API_PROD_URL = "https://mws.exan.tech/api/v1/";

    private static final String PIN = "pin";
    private static final String PIN_TIME = "pinTime";
    private static final String PIN_ATTEMPT = "pinAttempt";
    private static final String COLLAPSE_TIME = "collapseTime";
    private static final String FCM_TOKEN = "fcmToken";
    public static final String USE_SPY_PROTECTION = "useSpyProtection";

    public static final int SECRET_LENGTH = 3;
    private static final int MAX_PIN_ATTEMPT = 3;
    private static final int COLLAPSE_TIME_DELTA = 30000;
    private static final int PIN_DELTA_TIME = 30000;
    public static final String DECIMAL_SEPARATOR = ".";
    public static final String SETTINGS_INI = "settings.ini";

    private String mFCMToken;

    private long mCollapseTime = 0;
    private long mStartPinTime = 0;

    private int mPinAttempt = MAX_PIN_ATTEMPT;

    private byte[] mPin = new byte[0];

    private boolean isChanged = false;
    private String mDeviceUid;
    private boolean mUseSpyProtection;

    private static String sAddress;

    /**
     * @return true if should be locked by PIN
     */
    public boolean onRootActivityExpand() {
        return null != mPin && 0 < mPin.length && mCollapseTime + COLLAPSE_TIME_DELTA - currentTimeMillis() < 0;
    }

    public void notifyCollapseTime() {
        HLog.debug(TAG, "notifyCollapseTime");
        mCollapseTime = currentTimeMillis();
    }

    public byte[] getPin() {
        return mPin;
    }

    public void setPin(byte[] pin) {
        this.mPin = pin;
    }

    public void notifyPinAttempt(boolean attempt) {
        if (attempt) {
            mPinAttempt = MAX_PIN_ATTEMPT;
            mStartPinTime = 0;
        } else {
            mPinAttempt--;
            if (mPinAttempt < 1) {
                mStartPinTime = currentTimeMillis();
            }
        }
    }

    public long getPinTime() {
        return mPinAttempt < 1 ? mStartPinTime + PIN_DELTA_TIME - currentTimeMillis() : 0;
    }

    public NetworkType getNetworkType() {
        return NETWORK_TYPE_MAINNET;
    }

    public String getApiURL() {
        return NETWORK_TYPE_MAINNET == getNetworkType() ? API_PROD_URL : API_STAGE_URL;
    }

    public void notifyChange() {
        isChanged = true;
    }

    public void load(File filesDir) {
        File[] found = filesDir.listFiles((dir, filename) -> filename.endsWith(SETTINGS_INI));

        if (null != found && 0 < found.length) {
            try {
                Properties properties = new Properties();
                properties.load(new FileInputStream(found[0]));
                if (properties.containsKey(PIN)) {
                    mPin = parsePin(properties.getProperty(PIN));
                }
                mCollapseTime = parseLong(properties.getProperty(COLLAPSE_TIME, "0"));
                mStartPinTime = parseLong(properties.getProperty(PIN_TIME, "0"));
                mPinAttempt = parseInt(properties.getProperty(PIN_ATTEMPT, "0"));
                mFCMToken = properties.getProperty(FCM_TOKEN, mFCMToken);
                mUseSpyProtection = parseBoolean(properties.getProperty(USE_SPY_PROTECTION, "false"));
                properties.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(File filesDir) {
        if (isChanged) {
            try {
                Properties properties = new Properties();

                if (null != mPin && 0 < mPin.length) {
                    String pin = "";

                    for (Byte code : mPin) {
                        pin = pin.concat(code.toString());
                    }

                    properties.setProperty(PIN, pin);
                    properties.setProperty(PIN_TIME, String.valueOf(mStartPinTime));
                    properties.setProperty(PIN_ATTEMPT, String.valueOf(mPinAttempt));
                    properties.setProperty(COLLAPSE_TIME, String.valueOf(mCollapseTime));
                }

                if (null != mFCMToken) {
                    properties.setProperty(FCM_TOKEN, mFCMToken);
                }

                properties.setProperty(USE_SPY_PROTECTION, String.valueOf(mUseSpyProtection));

                properties.store(new FileOutputStream(new File(filesDir, SETTINGS_INI)), "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] parsePin(String pin) {
        byte[] result = new byte[pin.length()];

        for (int index = 0; result.length > index; index++) {
            result[index] = Byte.parseByte(pin.substring(index, index + 1));
        }
        return result;
    }

    public void setFCMToken(String token) {
        mFCMToken = token;
        notifyChange();
    }

    public String getFCMToken() {
        return isEmpty(mFCMToken) ? FirebaseInstanceId.getInstance().getToken() : mFCMToken;
    }

    public void setDeviceUid(String deviceUid) {
        this.mDeviceUid = deviceUid;
    }

    public String getDeviceUid() {
        return mDeviceUid;
    }

    public static String getCurrentWalletAddress() {
        return sAddress;
    }

    public static void setCurrentWalletAddress(String address) {
        sAddress = address;
    }

    public boolean useSpyProtection() {
        return mUseSpyProtection;
    }

    public void setUseSpyProtect(boolean checked) {
        mUseSpyProtection = checked;
        notifyChange();
    }
}