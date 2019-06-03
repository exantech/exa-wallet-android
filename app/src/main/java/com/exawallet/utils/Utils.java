package com.exawallet.utils;

import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import com.exawallet.RootActivity;
import com.exawallet.fragments.BalanceFragment;
import com.exawallet.fragments.InviteCodeFragment;
import com.exawallet.fragments.SplashFragment;
import com.exawallet.model.NetworkType;
import com.exawallet.model.meta.CreatingMeta;
import com.exawallet.model.meta.SharedMeta;
import com.exawallet.model.meta.WalletMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.text.TextUtils.indexOf;
import static android.text.TextUtils.isEmpty;
import static com.exawallet.common.AppContext.DECIMAL_SEPARATOR;
import static com.exawallet.model.NetworkType.NETWORK_TYPE_STAGENET;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.model.meta.SharedRole.*;
import static com.exawallet.presenters.operations.QRCodeOperation.generateQRCode;
import static com.exawallet.sequences.InitSharedWalletSequence.initSharedWallet;
import static com.exawallet.sequences.JoinSharedWalletSequence.joinSharedWallet;
import static com.exawallet.sequences.RestoreSharedWalletSequence.restoreSharedWallet;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public class Utils {
    private static final SimpleDateFormat DATE_FULL_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_SHORT_FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);

    public static String formatUnconfirmedAmount(Long amount) {
        return format("+ %s XMR unconfirmed", amountToString(amount));
    }

    public static boolean isFilled(String source, Character value) {
        for (int index = 0; index < source.length(); index++) {
            if (source.charAt(index) != value) {
                return false;
            }
        }

        return true;
    }

    public static String validateAmount(String value) {
        String[] values = isEmpty(value) ? null : value.split(Pattern.quote(DECIMAL_SEPARATOR));

        if (null == values) {
            return value;
        } else if (2 < values.length) {
            throw new IllegalArgumentException();
        } else if (2 == values.length) {
            return values[0].concat(DECIMAL_SEPARATOR).concat(13 > values[1].length() ? values[1] : values[1].substring(0, 12));
        } else {
            return value;
        }
    }

    public static long parseAmount(String value) {
        String[] values = isEmpty(value) ? null : value.split(Pattern.quote(DECIMAL_SEPARATOR));

        if (null == values) {
            return 0L;
        } else if (2 < values.length) {
            throw new IllegalArgumentException();
        } else if (2 == values.length) {
            return Long.parseLong(values[0].concat(13 > values[1].length() ? values[1].concat(getZeros(12 - values[1].length())) : values[1].substring(0, 12)));
        } else {
            return Long.parseLong(values[0].concat(getZeros(12)));
        }
    }

    private static String balanceToString(double balance) {
        return balanceToString(balance, 4);
    }

    private static String balanceUsdToString(double balance) {
        return balanceToString(balance, 2);
    }

    private static String balanceToString(double balance, int length) {
        String value = valueOf(balance / 1e12);
        int index = indexOf(value, DECIMAL_SEPARATOR);
        String left;
        String right;

        left = value.substring(0, index);
        right = value.substring(index + 1, Math.min(index + 1 + length, value.length()));

        return left.concat(DECIMAL_SEPARATOR).concat(right);
    }

    private static String amountToString(long amount) {
        String value = valueOf(amount);
        String left;
        String right;

        if (12 < value.length()) {
            left = value.substring(0, value.length() - 12);
            right = value.substring(value.length() - 12, 12);
        } else {
            left = "0";
            right = getZeros(12 - value.length()).concat(value);
        }

        while (1 < right.length() && right.endsWith("0")) {
            right = right.substring(0, right.length() - 1);
        }

        return left.concat(DECIMAL_SEPARATOR).concat(right);
    }

    public static long getBlockHeightByDate(NetworkType networkType, long timestamp) {
        return NETWORK_TYPE_STAGENET == networkType ? (timestamp - 1517657680) / 120 : 1458748658 < timestamp ? 1009827 + (timestamp - 1458748658) / 120 : (timestamp - 1397818193) / 60;
    }

    public static void selectWalletScreen(WalletMeta walletMeta, RootActivity activity) {
        if (walletMeta.isNotReady()) {
            activity.show(SplashFragment.newInstance());

            SharedMeta sharedMeta = walletMeta.getSharedMeta();
            CreatingMeta creatingMeta = sharedMeta.getCreatingMeta();
            String inviteCode = creatingMeta.getInviteCode();

            if (INITIATOR == creatingMeta.getRole()) {
                initSharedWallet(activity, walletMeta.getAddress());
            } else if (JOINER == creatingMeta.getRole()) {
                joinSharedWallet(activity, walletMeta.getAddress());
            } else if (RESTORER == creatingMeta.getRole()) {
                restoreSharedWallet(activity, walletMeta.getAddress());
            }

            if (null != inviteCode) {
                generateQRCode(activity, inviteCode, qrResult -> {
                    walletManager().setQRInviteCode(qrResult.getResult());
                    activity.show(InviteCodeFragment.newInstance());
                });
            }
        } else {
            activity.show(BalanceFragment.newInstance());
        }
    }

    private static String getZeros(int length) {
        return 0 < length ? format("%0" + length + "d", 0) : "";
    }

    public static CharSequence formatBalanceTicker(double amount, final String ticker) {
        return format("%s ".concat(ticker), balanceToString(amount));
    }

    public static CharSequence formatBalanceUsd(double amount) {
        return format("%s ".concat("USD"), balanceUsdToString(amount));
    }

    public static CharSequence formatAmount(Long amount) {
        return format("%s XMR", amountToString(amount));
    }

    public static CharSequence formatAmountB(Long amount) {
        String format = format("%s XMR", amountToString(amount));
        SpannableString spannableString = new SpannableString(format);
        spannableString.setSpan(new RelativeSizeSpan(0.6f), format.indexOf(DECIMAL_SEPARATOR), format.indexOf(' '), SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static boolean containString(String[] values, String value) {
        if (null != values) {
            for (String compare : values) {
                if (compare.equals(value)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void writeStringArray(String counter, String key, String[] values, Properties properties) {
        properties.setProperty(counter, String.valueOf(null != values ? values.length : 0));

        for (int index = 0; null != values && index < values.length; index++) {
            properties.setProperty(key.concat(String.valueOf(index)), values[index]);
        }
    }

    public static String[] parseStringArray(String counter, String key, Properties properties) {
        int count = properties.containsKey(counter) ? parseInt(properties.getProperty(counter)) : 0;

        String[] result = new String[count];

        for (int index = 0; index < count; index++) {
            result[index] = properties.getProperty(key.concat(String.valueOf(index)));
        }

        return result;
    }

    public static boolean contains(String output, String[] imported) {
        for (String item : imported) {
            if (item.equals(output)) {
                return true;
            }
        }

        return false;
    }

    public static String formatDateFull(Date value) {
        return DATE_FULL_FORMAT.format(value);
    }

    public static String formatDateShort(Date value) {
        return DATE_SHORT_FORMAT.format(value);
    }
}