package com.exawallet.common;

public class WalletNewBlock implements IEvent {
    private final long mLastBlock;
    private final long mCurrentHeight;
    private final long mHeight;

    public WalletNewBlock(long lastBlock, long currentHeght, long height) {
        this.mLastBlock = lastBlock;
        this.mCurrentHeight = currentHeght;
        this.mHeight = height;
    }

    public WalletNewBlock() {
        this(-1, -1, -1);
    }

    public long getRemaining() {
        return mLastBlock - mHeight;
    }

    public boolean isDone() {
        return mLastBlock == mHeight && mHeight == mCurrentHeight && -1 == mCurrentHeight;
    }

    public boolean isConnected() {
        return 0 < mCurrentHeight;
    }
}