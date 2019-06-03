package com.exawallet.model.meta;

import android.graphics.Bitmap;
import com.exawallet.utils.HLog;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.meta.SharedRole.JOINER;
import static com.exawallet.model.meta.SharedStage.OPEN_SESSION;

public class CreatingMeta {
    private static final String TAG = CreatingMeta.class.getSimpleName();

    private static final String ROLE = "role";
    private static final String STAGE = "stage";
    private static final String INVITE_CODE = "inviteCode";
    private static final String EXTRA_MULTISIG_INFO = "extraMultisigInfo";
    private static final String COUNTER = "counter";

    private final SharedRole mRole;
    private SharedStage mStage;

    private String mExtraMultisigInfo;
    private String mInviteCode;

    private int mCounter = 0;

    private Bitmap mQRInviteCode;
    private String[] mMultisigInfos;

    CreatingMeta(String inviteCode) {
        this(JOINER, OPEN_SESSION);
        this.mInviteCode = inviteCode;
    }

    CreatingMeta(SharedRole role, SharedStage stage) {
        this.mRole = role;
        this.mStage = stage;
    }

    private CreatingMeta(Reader reader) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);

        mRole = SharedRole.valueOf(properties.getProperty(ROLE));
        mStage = SharedStage.valueOf(properties.getProperty(STAGE));

        mCounter = Integer.valueOf(properties.getProperty(COUNTER, "0"));

        mInviteCode = properties.getProperty(INVITE_CODE, null);
        mExtraMultisigInfo = properties.getProperty(EXTRA_MULTISIG_INFO, null);
    }

    @Override
    public final String toString() {
        try {
            Properties properties = new Properties();

            properties.setProperty(ROLE, mRole.name());
            properties.setProperty(STAGE, mStage.name());

            if (!isEmpty(mInviteCode)) {
                properties.setProperty(INVITE_CODE, mInviteCode);
            }

            if (!isEmpty(mExtraMultisigInfo)) {
                properties.setProperty(EXTRA_MULTISIG_INFO, mExtraMultisigInfo);
            }

            properties.setProperty(COUNTER, String.valueOf(mCounter));

            StringWriter writer = new StringWriter();
            properties.store(writer, TAG);
            return writer.toString();
        } catch (IOException e) {
            HLog.error(TAG, "Can't save CreatingMeta", e);
        }
        return null;
    }

    public SharedRole getRole() {
        return mRole;
    }

    public void setStage(SharedStage stage) {
        mStage = stage;
    }

    public SharedStage getStage() {
        return mStage;
    }

    public void setMultisigInfos(String[] multisigInfos) {
        mMultisigInfos = multisigInfos;
    }

    public String[] getMultisigInfos() {
        return mMultisigInfos;
    }

    public void setExtraMultisig(String extraMultisigInfo, boolean n1Scheme) {
        this.mExtraMultisigInfo = extraMultisigInfo;
        mCounter = n1Scheme ? Integer.MIN_VALUE : 1 + mCounter;
    }

    public String getExtraMultisigInfo() {
        return mExtraMultisigInfo;
    }

    public void setInviteCode(String inviteCode) {
        this.mInviteCode = inviteCode;
    }

    public String getInviteCode() {
        return mInviteCode;
    }

    public void setQRInviteCode(Bitmap qrInviteCode) {
        this.mQRInviteCode = qrInviteCode;
    }

    public Bitmap getQRInviteCode() {
        return mQRInviteCode;
    }

    public int getCounter() {
        return mCounter;
    }

    static CreatingMeta parseCreatingMeta(String property) {
        if (null != property) {
            try {
                return new CreatingMeta(new StringReader(property));
            } catch (IOException e) {
                HLog.error(TAG, "Can't load CreatingMeta", e);
            }
        }
        return null;
    }

    public void dropCounter() {
        mCounter = Integer.MIN_VALUE;
    }
}