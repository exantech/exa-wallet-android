package com.exawallet.model.meta;

import android.content.Context;
import android.graphics.Bitmap;
import com.exawallet.model.NetworkType;
import com.exawallet.model.WalletColor;
import com.exawallet.model.WalletStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static com.exawallet.model.WalletStatus.CONFIRMED;
import static com.exawallet.model.WalletStatus.DELETED;
import static com.exawallet.model.meta.SharedMeta.parseSharedMeta;

public class WalletMeta implements Comparable<WalletMeta> {
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String WALLET_COLOR = "walletColor";
    private static final String STATUS = "status";
    private static final String DAEMON = "daemon";
    private static final String LANGUAGE = "language";
    private static final String BLOCK_HEIGHT = "blockHeight";
    private static final String NETWORK_TYPE = "networkType";
    private static final String LAST_SYNC = "lastSync";
    private static final String SHARED_META = "multisigMeta";
    private static final String TRANSACTION_COUNT = "transactionCount";
    private static final String EMPTY_PASSWORD = "emptyPassword";

    private final File mFile;
    private String mName;
    private boolean isEmptyPassword;
    private WalletColor mColor;
    private String mDaemon;
    private final NetworkType mNetworkType;

    private final String mLanguage;
    private long mBlockHeight;

    private String mAddress;
    private WalletStatus mStatus;
    private Date mLastSync;

    private String mHost;
    private int mPort;
    private String mUser;
    private String mPassword;

    private int mTransactionCount;

    private SharedMeta mSharedMeta;

    private Bitmap mQRAddress;

    public WalletMeta(File file) throws IOException {
        String filename = file.getName();
        this.mFile = new File(file.getParent(), filename.substring(0, filename.length() - 4)); // 5 is length of ".wpr"+1

        Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        mName = properties.getProperty(NAME);
        mAddress = properties.getProperty(ADDRESS);
        mColor = WalletColor.valueOf(properties.getProperty(WALLET_COLOR));
        isEmptyPassword = properties.containsKey(EMPTY_PASSWORD);
        mLanguage = properties.getProperty(LANGUAGE);
        mStatus = WalletStatus.valueOf(properties.getProperty(STATUS));
        mDaemon = properties.getProperty(DAEMON);
        mNetworkType = NetworkType.valueOf(properties.getProperty(NETWORK_TYPE));
        mBlockHeight = Long.parseLong(properties.getProperty(BLOCK_HEIGHT));
        mLastSync = new Date(Date.parse(properties.getProperty(LAST_SYNC)));
        mTransactionCount = Integer.parseInt(properties.getProperty(TRANSACTION_COUNT, "0"));

        mSharedMeta = parseSharedMeta(properties.getProperty(SHARED_META, null));

        properties.clear();

        parseDaemonAddress(mDaemon);
    }

    public WalletMeta(Context context, String name, SharedMeta sharedMeta, WalletColor walletColor, boolean emptyPassword, String language, WalletStatus status, String daemon, NetworkType networkType, long blockHeight, Date lastSync) throws IOException {
        this.mFile = new File(context.getFilesDir(), "w" + System.currentTimeMillis());

        this.mName = name;
        this.mAddress = "";
        this.mColor = walletColor;
        this.isEmptyPassword = emptyPassword;
        this.mLanguage = language;
        this.mStatus = status;
        this.mDaemon = daemon;
        this.mNetworkType = networkType;
        this.mBlockHeight = blockHeight;
        this.mLastSync = lastSync;
        this.mSharedMeta = sharedMeta;

        parseDaemonAddress(daemon);

        store();
    }

    public void store() throws IOException {
        Properties properties = new Properties();

        properties.setProperty(NAME, mName);
        properties.setProperty(ADDRESS, mAddress);
        properties.setProperty(WALLET_COLOR, mColor.name());
        if (isEmptyPassword) {
            properties.setProperty(EMPTY_PASSWORD, "true");
        }
        properties.setProperty(LANGUAGE, mLanguage);
        properties.setProperty(STATUS, mStatus.name());
        properties.setProperty(DAEMON, mDaemon);
        properties.setProperty(NETWORK_TYPE, mNetworkType.name());
        properties.setProperty(BLOCK_HEIGHT, String.valueOf(mBlockHeight));
        properties.setProperty(LAST_SYNC, mLastSync.toString());
        properties.setProperty(TRANSACTION_COUNT, String.valueOf(mTransactionCount));

        if (null != mSharedMeta) {
            properties.setProperty(SHARED_META, mSharedMeta.toString());
        }

        properties.store(new FileOutputStream(new File(mFile.getAbsolutePath() + ".wpr")), "");
    }

    private void parseDaemonAddress(String daemon) {
        String daemonAddress;
        String a[] = daemon.split("@");
        if (a.length == 1) { // no credentials
            daemonAddress = a[0];
            mUser = "";
            mPassword = "";
        } else if (a.length == 2) { // credentials
            String userPassword[] = a[0].split(":");
            if (userPassword.length != 2) {
                throw new IllegalArgumentException("User:Password invalid");
            }
            mUser = userPassword[0];
            mPassword = mUser.isEmpty() ? "" : userPassword[1];
            daemonAddress = a[1];
        } else {
            throw new IllegalArgumentException("Too many @");
        }

        String da[] = daemonAddress.split(":");

        if ((da.length > 2) || (da.length < 1)) {
            throw new IllegalArgumentException("Too many ':' or too few");
        }
        mHost = da[0];
        if (da.length == 2) {
            try {
                mPort = Integer.parseInt(da[1]);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Port not numeric");
            }
        } else {
            switch (mNetworkType) {
                case NETWORK_TYPE_MAINNET:
                    mPort = 18081;
                    break;
                case NETWORK_TYPE_TESTNET:
                    mPort = 28081;
                    break;
                case NETWORK_TYPE_STAGENET:
                    mPort = 38081;
                    break;
                default:
                    mPort = 0;
            }
        }
    }

    public Bitmap getQRAddress() {
        return mQRAddress;
    }

    public void setQRAddress(Bitmap result) {
        mQRAddress = result;
    }

    public void updateAddress(String address) throws IOException {
        this.mAddress = address;

        store();
    }

    public void confirmed() throws IOException {
        this.mStatus = CONFIRMED;

        store();
    }

    public void deleted() throws IOException {
        this.mStatus = DELETED;

        store();
    }

    public boolean isEmptyPassword() {
        return isEmptyPassword;
    }

    public String getDaemonAddress() {
        return mHost + ":" + mPort;
    }

    public String getDaemonUsername() {
        return mUser;
    }

    public String getDaemonPassword() {
        return mPassword;
    }

    public void setLastUpdated(long millis) {
        this.mLastSync = new Date(millis);
    }

    public NetworkType getNetworkType() {
        return mNetworkType;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public SharedMeta getSharedMeta() {
        return mSharedMeta;
    }

    public boolean isReady() {
        return null == mSharedMeta || null == mSharedMeta.getCreatingMeta();
    }

    public boolean isNotReady() {
        return null != mSharedMeta && null != mSharedMeta.getCreatingMeta();
    }

    public boolean isSharedReady() {
        return null != mSharedMeta && null == mSharedMeta.getCreatingMeta();
    }

    @Override
    public int compareTo(WalletMeta another) {
        int n = mName.toLowerCase().compareTo(another.mName.toLowerCase());
        // wallet names are the same
        return 0 != n ? n : mAddress.compareTo(another.mAddress);
    }

    public void setDaemon(String daemon) {
        parseDaemonAddress(this.mDaemon = daemon);
    }

    public String getAddress() {
        return mAddress;
    }

    public WalletStatus getStatus() {
        return mStatus;
    }

    public void setTransactionCount(int transactionCount) {
        this.mTransactionCount = transactionCount;
    }

    public int getTransactionCount() {
        return mTransactionCount;
    }

    public void setSharedMeta(SharedMeta sharedMeta) {
        this.mSharedMeta = sharedMeta;
    }

    public String getName() {
        return mName;
    }

    public File getFile() {
        return mFile;
    }

    public String getFileName() {
        return mFile.getName();
    }

    public WalletColor getColor() {
        return mColor;
    }

    public void setColor(WalletColor color) {
        mColor = color;
    }

    public void setName(String walletName) {
        mName = walletName;
    }

    public String getFilePath() {
        return mFile.getAbsolutePath();
    }
}