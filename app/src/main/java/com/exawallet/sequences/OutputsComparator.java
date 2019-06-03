package com.exawallet.sequences;

import java.util.Comparator;

import static android.text.TextUtils.isEmpty;

public class OutputsComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return isEmpty(o1) ? 1 : isEmpty(o2) ? -1 : Integer.compare(o2.length(), o1.length());
    }
}