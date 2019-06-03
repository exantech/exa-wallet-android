package com.exawallet.model.meta;

import com.exawallet.api.requests.TxProposalsSigned;
import com.exawallet.utils.HLog;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;

import static com.exawallet.model.meta.TxProposalStage.*;
import static com.exawallet.utils.Utils.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.System.arraycopy;
import static java.lang.System.currentTimeMillis;

public class TxProposalsMeta implements Comparable<TxProposalsMeta> {
    private static final String TAG = TxProposalsMeta.class.getSimpleName();
    private static final String ID = "id";
    private static final String STAGE = "stage";
    private static final String SIGNED_TRANSACTION = "signedTransaction";
    private static final String DESTINATION_ADDRESS = "destinationAddress";
    private static final String DESCRIPTION = "description";
    private static final String AMOUNT = "amount";
    private static final String FEE = "fee";
    private static final String APPROVAL_NONCE = "approvalNonce";
    private static final String TIMESTAMP = "timestamp";
    private static final String APPROVALS_COUNT = "acceptedCount";
    private static final String APPROVALS = "accepted";
    private static final String REJECTS_COUNT = "declinedCount";
    private static final String REJECTS = "declined";
    private static final String IS_SENDED = "isSended";

    private String mId;
    private TxProposalStage mStage;

    private String mSignedTransaction;
    private final String mDestinationAddress;
    private final String mDescription;
    private final long mAmount;
    private final long mFee;
    private int mApprovalNonce;
    private long mTimestamp;
    private String[] mApprovals;
    private String[] mRejects;
    private boolean isSended = false;

    public TxProposalsMeta(String signedTransaction, String id, String destinationAddress, String description, long amount, long fee, String[] approvals, String[] rejects, TxProposalStage stage) {
        this.mId = id;
        this.mSignedTransaction = signedTransaction;
        this.mDestinationAddress = destinationAddress;
        this.mDescription = description;
        this.mAmount = amount;
        this.mFee = fee;
        this.mStage = stage;
        this.mApprovals = approvals;
        this.mRejects = rejects;
        this.mTimestamp = currentTimeMillis();
    }

    private TxProposalsMeta(Reader stringReader) throws IOException {
        Properties properties = new Properties();
        properties.load(stringReader);

        mId = properties.getProperty(ID);
        mStage = TxProposalStage.valueOf(properties.getProperty(STAGE));
        mSignedTransaction = properties.getProperty(SIGNED_TRANSACTION);
        mDestinationAddress = properties.getProperty(DESTINATION_ADDRESS);
        mDescription = properties.getProperty(DESCRIPTION);
        mAmount = parseLong(properties.getProperty(AMOUNT));
        mFee = parseLong(properties.getProperty(FEE));

        mApprovalNonce = parseInt(properties.getProperty(APPROVAL_NONCE));

        mApprovals = parseStringArray(APPROVALS_COUNT, APPROVALS, properties);
        mRejects = parseStringArray(REJECTS_COUNT, REJECTS, properties);

        mTimestamp = parseLong(properties.getProperty(TIMESTAMP));

        if (properties.containsKey(IS_SENDED)) {
            isSended = parseBoolean(properties.getProperty(IS_SENDED));
        }
    }

    @Override
    public final String toString() {
        try {
            Properties properties = new Properties();
            properties.setProperty(ID, mId);
            properties.setProperty(STAGE, mStage.name());
            properties.setProperty(SIGNED_TRANSACTION, mSignedTransaction);
            properties.setProperty(DESTINATION_ADDRESS, mDestinationAddress);
            properties.setProperty(DESCRIPTION, mDescription);
            properties.setProperty(AMOUNT, String.valueOf(mAmount));
            properties.setProperty(FEE, String.valueOf(mFee));
            properties.setProperty(APPROVAL_NONCE, String.valueOf(mApprovalNonce));
            properties.setProperty(TIMESTAMP, String.valueOf(mTimestamp));
            properties.setProperty(IS_SENDED, String.valueOf(isSended));

            writeStringArray(APPROVALS_COUNT, APPROVALS, mApprovals, properties);
            writeStringArray(REJECTS_COUNT, REJECTS, mRejects, properties);

            StringWriter writer = new StringWriter();
            properties.store(writer, TAG);
            return writer.toString();
        } catch (IOException e) {
            HLog.error(TAG, "Can't convert to string TxProposalsMeta", e);
        }
        return null;
    }

    public void setStage(TxProposalStage stage) {
        this.mStage = stage;
    }

    public TxProposalStage getStage() {
        return mStage;
    }

    public boolean isExistsInProcess() {
        return TX_PROPOSALS_DECISION_ACCEPTED != mStage && TX_PROPOSALS_RELAY_DONE != mStage && OUTPUT_EXPORTED != mStage;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getDestinationAddress() {
        return mDestinationAddress;
    }

    public void setSignedTransaction(String signedTransaction) {
        this.mSignedTransaction = signedTransaction;
    }

    public String getSignedTransaction() {
        return mSignedTransaction;
    }

    public TxProposalsSigned getTxProposals() {
        return new TxProposalsSigned(mDestinationAddress, mDescription, mAmount, mFee, mSignedTransaction);
    }

    public long getAmount() {
        return mAmount;
    }

    public Long getFee() {
        return mFee;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public String getDescription() {
        return mDescription;
    }

    public void updateApprovals(String[] approvals, String publicMultisigSignerKey) {
        setApprovals(isApproved(publicMultisigSignerKey) ? setDecision(publicMultisigSignerKey, approvals) : approvals);
    }

    public void setApprovals(String[] approvals) {
        mApprovals = approvals;
    }

    public String[] getApprovals() {
        return mApprovals;
    }

    public int getApprovalsCount() {
        return null != mApprovals ? mApprovals.length : 0;
    }

    public void updateRejects(String[] rejects, String publicMultisigSignerKey) {
        setRejects(isRejected(publicMultisigSignerKey) ? setDecision(publicMultisigSignerKey, rejects) : rejects);
    }

    public void setRejects(String[] rejects) {
        mRejects = rejects;
    }

    public int getRejectsCount() {
        return null != mRejects ? mRejects.length : 0;
    }

    public String[] getRejects() {
        return mRejects;
    }

    public boolean isApproved(String publicMultisigSignerKey) {
        return containString(mApprovals, publicMultisigSignerKey);
    }

    private boolean isRejected(String publicMultisigSignerKey) {
        return containString(mRejects, publicMultisigSignerKey);
    }

    public void approve(String publicMultisigSignerKey) {
        mApprovals = setDecision(publicMultisigSignerKey, mApprovals);

        mStage = SEND_TX_PROPOSALS_DECISION;
    }

    public void reject(String publicMultisigSignerKey) {
        mApprovals = removeDecision(publicMultisigSignerKey, mApprovals);
        mRejects = setDecision(publicMultisigSignerKey, mRejects);

        mStage = SEND_TX_PROPOSALS_DECISION;
    }

    private String[] setDecision(String publicMultisigSignerKey, String[] exists) {
        if (!containString(exists, publicMultisigSignerKey)) {
            String[] decision = new String[null == exists ? 1 : exists.length + 1];
            if (1 < decision.length) {
                arraycopy(exists, 0, decision, 0, exists.length);
            }

            decision[decision.length - 1] = publicMultisigSignerKey;

            return decision;
        }

        return exists;
    }

    private String[] removeDecision(String publicMultisigSignerKey, String[] exists) {
        if (containString(exists, publicMultisigSignerKey)) {
            String[] decision = new String[exists.length - 1];

            int out = 0;
            for (int in = 0; in < decision.length; in++) {
                if (!exists[in].equals(publicMultisigSignerKey)) {
                    decision[out++] = exists[in];
                }
            }

            return decision;
        }

        return exists;
    }

    static String toStringTxProposalsMetas(HashMap<String, TxProposalsMeta> txProposalsMetaMap) {
        try {
            Properties properties = new Properties();
            for (String key : txProposalsMetaMap.keySet()) {
                properties.setProperty(key, txProposalsMetaMap.get(key).toString());
            }

            StringWriter writer = new StringWriter();
            properties.store(writer, TAG);
            return writer.toString();
        } catch (IOException e) {
            HLog.error(TAG, "Can't save TxProposalsMeta map", e);
        }

        return null;
    }

    static HashMap<String, TxProposalsMeta> parseTxProposalsMetas(String property) {
        if (null != property) {
            try {
                HashMap<String, TxProposalsMeta> txProposalsHashMap = new HashMap<>();

                Properties properties = new Properties();
                properties.load(new StringReader(property));
                for (String key : properties.stringPropertyNames()) {
                    try {
                        txProposalsHashMap.put(key, parseTxProposalsMeta(properties.getProperty(key)));
                    } catch (IOException e) {
                        HLog.error(TAG, "Can't load TxProposalsMeta", e);
                    }
                }

                return txProposalsHashMap;
            } catch (IOException e) {
                HLog.error(TAG, "Can't load TxProposalsMeta map", e);
            }
        }
        return new HashMap<>();
    }

    @Override
    public int compareTo(TxProposalsMeta another) {
        return Long.compare(another.mTimestamp, this.mTimestamp);
    }

    private static TxProposalsMeta parseTxProposalsMeta(String property) throws IOException {
        return null != property ? new TxProposalsMeta(new StringReader(property)) : null;
    }

    public boolean isSended() {
        return this.isSended;
    }

    public void setSended() {
        isSended = true;
    }

    public boolean hasDecision(String publicMultisigSignerKey) {
        return containString(mApprovals, publicMultisigSignerKey) || containString(mRejects, publicMultisigSignerKey);
    }
}