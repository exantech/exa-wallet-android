package com.exawallet.presenters.operations;

import com.exawallet.RootActivity;
import com.exawallet.presenters.results.EmptyResult;
import com.exawallet.utils.HLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.exawallet.engine.Engine.ENGINE;
import static com.exawallet.model.ElectrumUtils.*;

public class LoadWordListOperation extends NoSplashScreenOperation<EmptyResult> {
    private LoadWordListOperation(RootActivity activity) {
        super(activity);
    }

    @Override
    protected EmptyResult execute() {
        if (null == EN_WORD_LIST) {
            EN_WORD_LIST = populateWordList(EN_MNEMONIC_WORDS);
        }
        if (null == EN_OLD_WORD_LIST) {
            EN_OLD_WORD_LIST = populateWordList(EN_OLD_MNEMONIC_WORDS);
        }
        if (null == CH_SM_WORD_LIST) {
            CH_SM_WORD_LIST = populateWordList(CH_SM_MNEMONIC_WORDS);
        }
        if (null == DU_WORD_LIST) {
            DU_WORD_LIST = populateWordList(DU_MNEMONIC_WORDS);
        }
        if (null == EP_WORD_LIST) {
            EP_WORD_LIST = populateWordList(EP_MNEMONIC_WORDS);
        }
        if (null == FR_WORD_LIST) {
            FR_WORD_LIST = populateWordList(FR_MNEMONIC_WORDS);
        }
        if (null == DE_WORD_LIST) {
            DE_WORD_LIST = populateWordList(DE_MNEMONIC_WORDS);
        }
        if (null == IT_WORD_LIST) {
            IT_WORD_LIST = populateWordList(IT_MNEMONIC_WORDS);
        }
        if (null == JP_WORD_LIST) {
            JP_WORD_LIST = populateWordList(JP_MNEMONIC_WORDS);
        }
        if (null == LO_WORD_LIST) {
            LO_WORD_LIST = populateWordList(LO_MNEMONIC_WORDS);
        }
        if (null == PO_WORD_LIST) {
            PO_WORD_LIST = populateWordList(PO_MNEMONIC_WORDS);
        }
        if (null == RU_WORD_LIST) {
            RU_WORD_LIST = populateWordList(RU_MNEMONIC_WORDS);
        }
        if (null == SP_WORD_LIST) {
            SP_WORD_LIST = populateWordList(SP_MNEMONIC_WORDS);
        }
        return new EmptyResult();
    }

    @Override
    protected void onSuccess(EmptyResult result) {
        HLog.debug(TAG, "onSuccess");
    }

    private List<String> populateWordList(String fileName) {
        List<String> result = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mActivity.getAssets().open(fileName)));

            // do reading, usually loop until end of file reading
            String line;
            while ((line = reader.readLine()) != null) {
                //process line
                result.add(line);
            }
        } catch (IOException e) {
            HLog.error(TAG, "populateWordList reader", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                    HLog.error(TAG, "populateWordList close", e);
                }
            }
        }
        return result;
    }

    public static void loadWordList(RootActivity activity) {
        ENGINE.submit(new LoadWordListOperation(activity));
    }
}