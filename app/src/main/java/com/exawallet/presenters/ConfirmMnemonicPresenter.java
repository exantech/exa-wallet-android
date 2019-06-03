package com.exawallet.presenters;

import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import com.exawallet.RootActivity;
import com.exawallet.dialogs.TwoButtonDialog;
import com.exawallet.fragments.SuccessMnemonicFragment;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IBaseView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.exawallet.common.AppContext.APP_CONTEXT;
import static com.exawallet.common.AppContext.SECRET_LENGTH;
import static com.exawallet.model.ElectrumUtils.getWordList;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.WALLETS;
import static com.exawallet.utils.Utils.selectWalletScreen;
import static com.exawallet.widget.SecretTextView.initShadowCollector;
import static java.lang.System.arraycopy;

public class ConfirmMnemonicPresenter extends AttachedPresenter<IBaseView> {
    private static final String HIDDEN_WORD = "_____";

    private String[] mMnemonicWords;
    private final List<String> mSecretWords = new ArrayList<>();
    private ArrayAdapter<String> mMnemonicAdapter;
    private ArrayAdapter<String> mSecretWordsAdapter;
    private String mSeed;

    @Override
    public void onAttach(RootActivity activity) {
        super.onAttach(activity);

        mSeed = mWallet.getSeed();
        mMnemonicWords = mSeed.split(" ");

        initSecretWords();

        initShadowCollector(getWordList(mWalletMeta.getLanguage()), APP_CONTEXT.useSpyProtection());
        mMnemonicAdapter = new ArrayAdapter<>(mActivity, R.layout.layout_mnemonic_item_white, R.id.mnemonic_item, mMnemonicWords);
        mSecretWordsAdapter = new ArrayAdapter<>(mActivity, R.layout.layout_secret_item, R.id.secret_word, mSecretWords);
    }

    private void initSecretWords() {
        for (int i = 0; i < SECRET_LENGTH; i++) {
            String mnemonicWord;
            int index;

            do {
                index = (int) (Math.random() * mMnemonicWords.length);
                mnemonicWord = mMnemonicWords[index];
            } while (HIDDEN_WORD.equals(mnemonicWord) || containSecretWord(mnemonicWord));

            mSecretWords.add(mnemonicWord);
            mMnemonicWords[index] = HIDDEN_WORD;
        }
    }

    private boolean containSecretWord(String mnemonicWord) {
        for (String secret : mSecretWords) {
            if (secret.equals(mnemonicWord)) {
                return true;
            }
        }
        return false;
    }

    public String getMnemonic() {
        String result = mMnemonicWords[0];

        for (int i = 1; i < mMnemonicWords.length; i++) {
            result = result.concat(" ").concat(mMnemonicWords[i]);
        }
        return result;
    }

    public ListAdapter getSecretAdapter() {
        return mSecretWordsAdapter;
    }

    public ListAdapter getMnemonicAdapter() {
        return mMnemonicAdapter;
    }

    public void getOnItemClickListener(int position) {
        for (int i = 0; i < mMnemonicWords.length; i++) {
            String word = mMnemonicWords[i];
            if (HIDDEN_WORD.equals(word)) {
                mMnemonicWords[i] = mSecretWords.get(position);

                mSecretWords.remove(position);
                mSecretWordsAdapter.notifyDataSetChanged();
                mMnemonicAdapter.notifyDataSetChanged();

                if (0 == mSecretWords.size()) {
                    if (getMnemonic().equals(mSeed)) {
                        try {
                            mWalletMeta.confirmed();
                            mActivity.show(SuccessMnemonicFragment.newInstance());
                        } catch (IOException e) {
                            mView.showErrorDialog(getString(R.string.error), e.getLocalizedMessage());
                        }
                    } else {
                        arraycopy(mSeed.split(" "), 0, mMnemonicWords, 0, mMnemonicWords.length);

                        initSecretWords();

                        mMnemonicAdapter.notifyDataSetChanged();
                        mSecretWordsAdapter.notifyDataSetChanged();

                        mView.showErrorDialog(getString(R.string.error), getString(R.string.invalid_mnemonc));
                    }
                }
                break;
            }
        }
    }

    public void onSkipConfirm() {
        new TwoButtonDialog(mActivity, getString(R.string.action_skip_confirm), getString(R.string.skip_mnemonic_notify)) {
            @Override
            protected void onButtonOkClick() {
                try {
                    mWalletMeta.confirmed();

                    BACK_PATH.clear();
                    BACK_PATH.setScreen(WALLETS);
                    selectWalletScreen(mWalletMeta, mActivity);

                    dismiss();
                } catch (IOException e) {
                    mView.showErrorDialog(getString(R.string.error), e.getLocalizedMessage());
                }
            }
        }.show();
    }
}