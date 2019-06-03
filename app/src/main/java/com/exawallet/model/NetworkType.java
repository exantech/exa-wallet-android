/*
 * Copyright (c) 2018 m2049r
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

public enum NetworkType {
    NETWORK_TYPE_MAINNET(0),
    NETWORK_TYPE_TESTNET(1),
    NETWORK_TYPE_STAGENET(2);

    public int getValue() {
        return value;
    }

    private final int value;

    NetworkType(int value) {
        this.value = value;
    }

    public static NetworkType fromInteger(int index) {
        switch (index) {
            case 0:
                return NETWORK_TYPE_MAINNET;
            case 1:
                return NETWORK_TYPE_TESTNET;
            case 2:
                return NETWORK_TYPE_STAGENET;
        }
        return null;
    }
}
