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

import java.util.List;

// this is not the TransactionInfo from the API as that is owned by the TransactionHistory
// this is a POJO for the TransactionInfoAdapter
public class TransactionInfo implements  Comparable<TransactionInfo> {
    static final String TAG = "TransactionInfo";

    private static final long MIN_CONFIRM = 10;

    public String getAddress() {
        if (null != transfers) {
            for (Transfer transfer : transfers) {
                return transfer.address;
            }
        }

        return null;
    }

    public enum Direction {
        Direction_In(0),
        Direction_Out(1);

        public static Direction fromInteger(int n) {
            switch (n) {
                case 0:
                    return Direction_In;
                case 1:
                    return Direction_Out;
            }
            return null;
        }

        public int getValue() {
            return value;
        }

        private int value;

        Direction(int value) {
            this.value = value;
        }
    }

    public Direction direction;
    public boolean isPending;
    public boolean isFailed;
    public long amount;
    public long fee;
    public long blockheight;
    public String hash;
    public long timestamp;
    public String paymentId;
    public long confirmations;
    public List<Transfer> transfers;

    public String txKey = null;
    public String notes = null;

    public TransactionInfo(
            int direction,
            boolean isPending,
            boolean isFailed,
            long amount,
            long fee,
            long blockheight,
            String hash,
            long timestamp,
            String paymentId,
            long confirmations,
            List<Transfer> transfers) {
        this.direction = Direction.values()[direction];
        this.isPending = isPending;
        this.isFailed = isFailed;
        this.amount = amount;
        this.fee = fee;
        this.blockheight = blockheight;
        this.hash = hash;
        this.timestamp = timestamp;
        this.paymentId = paymentId;
        this.confirmations = confirmations;
        this.transfers = transfers;
    }

    public String toString() {
        return direction + "@" + blockheight + " " + amount;
    }

    @Override
    public int compareTo(TransactionInfo another) {
        return Long.compare(another.timestamp, this.timestamp);
    }

    public static boolean isConfirmed(TransactionInfo transactionInfo) {
        return null != transactionInfo && MIN_CONFIRM < transactionInfo.confirmations;
    }
}