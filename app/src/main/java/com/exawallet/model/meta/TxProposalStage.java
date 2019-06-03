package com.exawallet.model.meta;

public enum TxProposalStage {
    CREATE_TX_PROPOSALS, // create new multisig transaction andsend it to server
    EXPECT_TX_PROPOSALS_DECISION, // expect user decision
    SEND_TX_PROPOSALS_DECISION, // send proposal decision to server
    TX_PROPOSALS_DECISION_ACCEPTED, // stop sequence because decision is accepted (not last in the chain)
    TX_PROPOSALS_RELAY, // last signum is accepted, need send transaction to blockchain
    TX_PROPOSALS_RELAY_DONE, // transaction to blockchain
    OUTPUT_EXPORTED
}
