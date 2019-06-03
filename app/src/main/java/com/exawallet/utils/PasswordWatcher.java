package com.exawallet.utils;

import android.text.Editable;
import com.exawallet.views.IPasswordView;

import java.util.HashSet;
import java.util.Set;

import static android.text.TextUtils.isEmpty;
import static java.lang.Math.*;
import static java.lang.String.valueOf;

public class PasswordWatcher extends SimpleTextWatcher {
    private static final double GLOBAL_REDUCTION = 5.6;
    private static final double LOG2 = log(2.0);

    private static final int LOW_LETTER = 0;
    private static final int CAPS_LETTER = 1;
    private static final int DIGIT = 2;
    private static final int SYMBOL = 3;

    private final IPasswordView mView;

    private final Set<Character>[] mSet = new HashSet[]{new HashSet<Character>(), new HashSet<Character>(), new HashSet<Character>(), new HashSet<Character>()};
    private final double[] mReduction = new double[]{4.4, 3.2, 2.0, 1.0, 10.0};

    public PasswordWatcher(IPasswordView mView) {
        this.mView = mView;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        double security = 0;

        String password = valueOf(editable);

        for (Set set : mSet) {
            set.clear();
        }

        if (!isEmpty(password)) {
            for (Character c : password.toCharArray()) {
                if (Character.isDigit(c)) {
                    mSet[DIGIT].add(c);
                } else if (Character.isLetter(c)) {
                    if (Character.isLowerCase(c)) {
                        mSet[LOW_LETTER].add(c);
                    } else {
                        mSet[CAPS_LETTER].add(c);
                    }
                } else {
                    mSet[SYMBOL].add(c);
                }
            }

            security = log(password.length() * sqrt(superposition())) / LOG2 / GLOBAL_REDUCTION;
        }

        mView.setSecurity(min(security, 1.0));
    }

    private double superposition() {
        double summ = 0.0;

        for (int x = 0; x < mSet.length; x++) {
            for (int y = 0; y < mSet.length; y++) {
                summ += calc(mSet[x].size(), mSet[y].size()) / mReduction[x] / mReduction[y] / (x == y ? mReduction[4] : 1.0);
            }
        }

        return summ;
    }

    private double calc(double value1, double value2) {
        return log(1 + value1) * log(1 + value2) / LOG2 / LOG2;
    }
}
