package com.exawallet.model;

import static java.lang.System.loadLibrary;

class ModelInitializer {
    static final boolean native_library_loaded;

    static {
        native_library_loaded = load("exawallet");
    }

    private static boolean load(String name) {
        try {
            loadLibrary(name);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}