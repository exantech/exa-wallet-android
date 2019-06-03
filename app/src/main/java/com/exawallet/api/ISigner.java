package com.exawallet.api;

public interface ISigner {
    String signMessage(String message, String secretKey);
}