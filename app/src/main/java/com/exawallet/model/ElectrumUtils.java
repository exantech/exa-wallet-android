package com.exawallet.model;

import java.util.List;

import static com.exawallet.model.RestoreMnemonicState.*;

public class ElectrumUtils {
    public static final String CH_SM_MNEMONIC_WORDS = "chinese_simplified-mnemonic-word-list.txt";
    public static final String DU_MNEMONIC_WORDS = "dutch-mnemonic-word-list.txt";
    public static final String EN_MNEMONIC_WORDS = "english-mnemonic-word-list.txt";
    public static final String EN_OLD_MNEMONIC_WORDS = "english-old-mnemonic-word-list.txt";
    public static final String EP_MNEMONIC_WORDS = "esperanto-mnemonic-word-list.txt";
    public static final String FR_MNEMONIC_WORDS = "french-mnemonic-word-list.txt";
    public static final String DE_MNEMONIC_WORDS = "german-mnemonic-word-list.txt";
    public static final String IT_MNEMONIC_WORDS = "italian-mnemonic-word-list.txt";
    public static final String JP_MNEMONIC_WORDS = "japanese-mnemonic-word-list.txt";
    public static final String LO_MNEMONIC_WORDS = "lojban-mnemonic-word-list.txt";
    public static final String PO_MNEMONIC_WORDS = "portuguese-mnemonic-word-list.txt";
    public static final String RU_MNEMONIC_WORDS = "russian-mnemonic-word-list.txt";
    public static final String SP_MNEMONIC_WORDS = "spanish-mnemonic-word-list.txt";

    public static List<String> EN_WORD_LIST;
    public static List<String> EN_OLD_WORD_LIST;
    public static List<String> CH_SM_WORD_LIST;
    public static List<String> DU_WORD_LIST;
    public static List<String> EP_WORD_LIST;
    public static List<String> FR_WORD_LIST;
    public static List<String> DE_WORD_LIST;
    public static List<String> IT_WORD_LIST;
    public static List<String> JP_WORD_LIST;
    public static List<String> LO_WORD_LIST;
    public static List<String> PO_WORD_LIST;
    public static List<String> RU_WORD_LIST;
    public static List<String> SP_WORD_LIST;

    public static RestoreMnemonicState validateMnemonic(String mnemonic) {
        RestoreMnemonicState result = INVALID_MNEMONIC_LENGTH;

        String[] mnemonicWords = mnemonic.split(" ");

        if (25 == mnemonicWords.length) {
            for (int language = 0; SUCCESS != result && 12 > language; language++) {
                result = SUCCESS;

                for (int index = 0; SUCCESS == result && index < mnemonicWords.length; index++) {
                    boolean found = false;
                    for (String memo : getWordList(language)) {
                        found |= memo.equals(mnemonicWords[index]);
                    }

                    if (!found) {
                        result = UNKNOW_LANGUAGE;
                    }
                }

                if (SUCCESS == result) {
                    SUCCESS.setLanguage(getMnemonicLanguage(language));
                }
            }
        }
        return result;
    }

    public static List<String> getWordList(String language) {
        return getWordList(getMnemonicLanguage(language));
    }

    private static List<String> getWordList(int language) {
        switch (language) {
            case 0: {
                return EN_WORD_LIST;
            }
            case 1: {
                return FR_WORD_LIST;
            }
            case 2: {
                return SP_WORD_LIST;
            }
            case 3: {
                return IT_WORD_LIST;
            }
            case 4: {
                return DU_WORD_LIST;
            }
            case 5: {
                return RU_WORD_LIST;
            }
            case 6: {
                return EN_OLD_WORD_LIST;
            }
            case 7: {
                return EP_WORD_LIST;
            }
            case 8: {
                return JP_WORD_LIST;
            }
            case 9: {
                return DE_WORD_LIST;
            }
            case 10: {
                return CH_SM_WORD_LIST;
            }
            case 11: {
                return LO_WORD_LIST;
            }
            case 12: {
                return PO_WORD_LIST;
            }
        }
        return EN_WORD_LIST;
    }

    public static int getMnemonicLanguage(String language) {
        switch (language) {
            case "English": {
                return 0;
            }
            case "Français": {
                return 1;
            }
            case "Español": {
                return 2;
            }
            case "Italiano": {
                return 3;
            }
            case "Nederlands": {
                return 4;
            }
            case "русский язык": {
                return 5;
            }
            case "EnglishOld": {
                return 6;
            }
            case "Esperanto": {
                return 7;
            }
            case "日本語": {
                return 8;
            }
            case "Deutsch": {
                return 9;
            }
            case "简体中文 (中国)": {
                return 10;
            }
            case "Lojban": {
                return 11;
            }
            case "Português": {
                return 12;
            }
        }
        return 0;
    }

    public static String getMnemonicLanguage(int language) {
        switch (language) {
            case 0: {
                return "English";
            }
            case 1: {
                return "Français";
            }
            case 2: {
                return "Español";
            }
            case 3: {
                return "Italiano";
            }
            case 4: {
                return "Nederlands";
            }
            case 5: {
                return "русский язык";
            }
            case 6: {
                return "EnglishOld";
            }
            case 7: {
                return "Esperanto";
            }
            case 8: {
                return "日本語";
            }
            case 9: {
                return "Deutsch";
            }
            case 10: {
                return "简体中文 (中国)";
            }
            case 11: {
                return "Lojban";
            }
            case 12: {
                return "Português";
            }
        }
        return "English";
    }
}