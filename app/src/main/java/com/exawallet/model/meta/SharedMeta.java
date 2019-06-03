package com.exawallet.model.meta;

import com.exawallet.utils.HLog;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static android.text.TextUtils.isEmpty;
import static com.exawallet.model.NativeCryptoUtils.cryptoUtils;
import static com.exawallet.model.NativeCryptoUtils.getHash;
import static com.exawallet.model.meta.CreatingMeta.parseCreatingMeta;
import static com.exawallet.model.meta.SharedRole.INITIATOR;
import static com.exawallet.model.meta.SharedRole.RESTORER;
import static com.exawallet.model.meta.SharedStage.OPEN_SESSION;
import static com.exawallet.model.meta.TxProposalsMeta.parseTxProposalsMetas;
import static com.exawallet.model.meta.TxProposalsMeta.toStringTxProposalsMetas;
import static com.exawallet.utils.Utils.*;

public class SharedMeta {
    private static final String TAG = SharedMeta.class.getSimpleName();
    private static final String PARTICIPANTS = "participants";
    private static final String SIGNERS = "signers";

    private static final String SESSION_ID = "sessionId";
    private static final String NONCE = "nonce";
    private static final String CREATING_META = "creatingMeta";
    private static final String TX_PROPOSALS_META = "TxProposalsMeta";
    private static final String OUTPUT_EXPORT = "OutputExport";
    private static final String OUTPUT_IMPORT = "OutputImport";
    private static final String OUTPUT_IMPORT_COUNT = "OutputImportCount";
    private static final String SECRET_SPEND_KEY = "SecretSpendKey";
    private static final String SEED = "Seed";

    private int mParticipants;
    private int mSigners;

    private int mNonce = 1;
    private String mSessionId;

    private String mOutput;
    private String[] mImported;
    private String[] mObtained;

    private CreatingMeta mCreatingMeta;
    private HashMap<String, TxProposalsMeta> mTxProposalsMetaMap;

    private String mSecretSpendKey;
    private String mSeed;

    private SharedMeta() {
        this.mParticipants = -1;
        this.mSigners = -1;
    }

    private SharedMeta(CreatingMeta creatingMeta) {
        this(-1, -1, creatingMeta);
    }

    private SharedMeta(int participants, int signers, CreatingMeta creatingMeta) {
        this.mParticipants = participants;
        this.mSigners = signers;

        this.mCreatingMeta = creatingMeta;
    }

    private SharedMeta(Reader reader) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);

        mParticipants = Integer.parseInt(properties.getProperty(PARTICIPANTS));
        mSigners = Integer.parseInt(properties.getProperty(SIGNERS));

        mSessionId = properties.getProperty(SESSION_ID, null);
        mNonce = Integer.valueOf(properties.getProperty(NONCE, "1"));

        mOutput = properties.getProperty(OUTPUT_EXPORT);
        mImported = parseStringArray(OUTPUT_IMPORT_COUNT, OUTPUT_IMPORT, properties);

        mCreatingMeta = parseCreatingMeta(properties.getProperty(CREATING_META, null));
        mTxProposalsMetaMap = parseTxProposalsMetas(properties.getProperty(TX_PROPOSALS_META, null));

        if (properties.containsKey(SECRET_SPEND_KEY)) {
            mSecretSpendKey = properties.getProperty(SECRET_SPEND_KEY);
        }

        if (properties.containsKey(SEED)) {
            mSeed = properties.getProperty(SEED);
        }
    }

    public void setSecretSpendKey(String secretSpendkey, String password) throws NoSuchAlgorithmException {
        mSecretSpendKey = cryptoUtils().chachaEncryptJ(secretSpendkey, getHash(password));
    }

    public String getSecretSpendKey(String password) throws NoSuchAlgorithmException {
        return !isEmpty(mSecretSpendKey) ? cryptoUtils().chachaDecryptJ(mSecretSpendKey, getHash(password)) : null;
    }

    public boolean hasSeed() {
        return !isEmpty(mSeed);
    }

    public void setSeed(String seed, String password) throws NoSuchAlgorithmException {
        mSeed = cryptoUtils().chachaEncryptJ(seed, getHash(password));
    }

    public String getSeed(String password) throws NoSuchAlgorithmException {
        return !isEmpty(mSeed) ? cryptoUtils().chachaDecryptJ(mSeed, getHash(password)) : null;
    }

    public void setParticipants(int participants) {
        this.mParticipants = participants;
    }

    public int getParticipants() {
        return mParticipants;
    }

    public void setSigners(int signers) {
        this.mSigners = signers;
    }

    public int getSigners() {
        return mSigners;
    }

    public Integer getNonce() {
        return mNonce++;
    }

    public String getSessionId() {
        return mSessionId;
    }

    public void setSessionId(String sessionId) {
        this.mSessionId = sessionId;
    }

    public void setOutput(String output) {
        this.mOutput = output;
    }

    public String getOutput() {
        return mOutput;
    }

    public void setImported(String[] imported) {
        this.mImported = imported;
    }

    public void setObtained(String[] obtained) {
        this.mObtained = obtained;
    }

    public String[] getObtained() {
        return mObtained;
    }

    public CreatingMeta getCreatingMeta() {
        return mCreatingMeta;
    }

    public void setCreatingMeta(CreatingMeta creatingMeta) {
        mCreatingMeta = creatingMeta;
    }

    public boolean isN1Scheme() {
        return 1 == mParticipants - mSigners;
    }

    public void setTxProposalRequest(TxProposalsMeta txProposalsMeta) {
        if (null == mTxProposalsMetaMap) {
            mTxProposalsMetaMap = new HashMap<>();
        }

        mTxProposalsMetaMap.put(txProposalsMeta.getId(), txProposalsMeta);
    }

    public TxProposalsMeta getTxProposalsMeta(String proposalId) {
        return null == mTxProposalsMetaMap ? null : mTxProposalsMetaMap.get(proposalId);
    }

    public void replaceTxProposalsMeta(TxProposalsMeta txProposalsMeta, String proposalId) {
        mTxProposalsMetaMap.remove(txProposalsMeta.getId());
        txProposalsMeta.setId(proposalId);
        mTxProposalsMetaMap.put(txProposalsMeta.getId(), txProposalsMeta);
    }

    public List<TxProposalsMeta> getTxProposalMetas() {
        return null != mTxProposalsMetaMap ? new ArrayList<>(mTxProposalsMetaMap.values()) : null;
    }

    @Override
    public final String toString() {
        try {
            Properties properties = new Properties();

            properties.setProperty(PARTICIPANTS, String.valueOf(mParticipants));
            properties.setProperty(SIGNERS, String.valueOf(mSigners));

            if (!isEmpty(mSessionId)) {
                properties.setProperty(SESSION_ID, mSessionId);
                properties.setProperty(NONCE, String.valueOf(mNonce));
            }

            if (null != mCreatingMeta) {
                properties.setProperty(CREATING_META, mCreatingMeta.toString());
            }

            if (!isEmpty(mOutput)) {
                properties.setProperty(OUTPUT_EXPORT, mOutput);
            }
            writeStringArray(OUTPUT_IMPORT_COUNT, OUTPUT_IMPORT, mImported, properties);

            if (null != mTxProposalsMetaMap && !mTxProposalsMetaMap.isEmpty()) {
                properties.setProperty(TX_PROPOSALS_META, toStringTxProposalsMetas(mTxProposalsMetaMap));
            }

            if (!isEmpty(mSecretSpendKey)) {
                properties.setProperty(SECRET_SPEND_KEY, mSecretSpendKey);
            }

            if (!isEmpty(mSeed)) {
                properties.setProperty(SEED, mSeed);
            }

            StringWriter writer = new StringWriter();
            properties.store(writer, TAG);
            return writer.toString();
        } catch (IOException e) {
            HLog.error(TAG, "Can't save SharedMeta", e);
        }
        return null;
    }

    static SharedMeta parseSharedMeta(String property) {
        if (null != property) {
            try {
                return new SharedMeta(new StringReader(property));
            } catch (IOException e) {
                HLog.error(TAG, "Can't load SharedMeta", e);
            }
        }
        return null;
    }

    public boolean needSendOutput() {
        return !isEmpty(mOutput) && null != mObtained && !containString(mObtained, mOutput);
    }

    public boolean needRequestOutputs() {
        if (isEmpty(mOutput)) {
            return false;
        }

        if (null == mObtained) {
            return true;
        }

        int count = 0;
        for (String obtained : mObtained) {
            if (mOutput.length() == obtained.length()) {
                count++;
            }
        }

        if (mParticipants > count) {
            return true;
        }

        int updated = 0;

        for (String outputsIitem : mObtained) {
            if (mOutput.length() == outputsIitem.length() && !contains(outputsIitem, mImported)) {
                updated++;
            }
        }

        return mParticipants > updated && updated > 0;
    }

    public boolean needInitImported() {
        return null == mImported && null != mObtained && mParticipants == mObtained.length;
    }

    public boolean isImportExpired() {
        if (isEmpty(mOutput) || null == mObtained || mParticipants > mObtained.length) {
            return false;
        }

        int count = 0;
        for (String obtained : mObtained) {
            if (mOutput.length() == obtained.length()) {
                count++;
            }
        }

        if (mParticipants > count) {
            return false;
        } else {
            if (containString(mImported, mOutput)) {
                boolean matches = true;

                for (String obtained : mObtained) {
                    matches &= containString(mImported, obtained);
                }

                return !matches;
            }

            return false;
        }
    }

    public boolean needImport() {
        if (null == mObtained || mParticipants > mObtained.length) {
            return false;
        }

        int count = 0;
        for (String obtained : mObtained) {
            if (mOutput.length() == obtained.length()) {
                count++;
            }
        }

        if (mParticipants > count) {
            return false;
        } else {
            int updated = 0;

            for (String obtainedItem : mObtained) {
                if (!containString(mImported, obtainedItem)) {
                    updated++;
                }
            }

            return mParticipants <= updated;
        }
    }

    public static SharedMeta buildRestored() {
        return new SharedMeta(new CreatingMeta(RESTORER, OPEN_SESSION));
    }

    public static SharedMeta buildInited(int participants, int signers) {
        return new SharedMeta(participants, signers, new CreatingMeta(INITIATOR, OPEN_SESSION));
    }

    public static SharedMeta buildJoined(String inviteCode) {
        return new SharedMeta(new CreatingMeta(inviteCode));
    }

    public static SharedMeta buildEmpty() {
        return new SharedMeta();
    }
}