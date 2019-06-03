package com.exawallet.utils;

import android.util.Log;

public class HLog {
    public static void verbose(String tag, String message, Throwable tr) {
        Log.v(tag, message, tr);
    }

    public static void verbose(String tag, String message) {
        Log.v(tag, message);
    }

    public static void debug(String tag, String message, Throwable tr) {
        Log.d(tag, message, tr);
    }

    public static void debug(String tag, String message) {
        Log.d(tag, message);
    }

    public static void info(String tag, String message, Throwable tr) {
        Log.i(tag, message, tr);
    }

    public static void info(String tag, String message) {
        Log.i(tag, message);
    }

    public static void warning(String tag, String message, Throwable tr) {
        Log.w(tag, message, tr);
    }

    public static void warning(String tag, String message) {
        Log.w(tag, message);
    }

    public static void error(String tag, String message, Throwable tr) {
        Log.e(tag, message, tr);
    }

    public static void error(String tag, String message) {
        Log.e(tag, message);
    }
}
