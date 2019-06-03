package com.exawallet.presenters;

import android.view.View;
import android.widget.AdapterView;
import com.exawallet.views.ICreateSharedWalletView;

import static com.exawallet.model.meta.SharedMeta.buildInited;
import static com.exawallet.presenters.operations.CreateWalletOperation.createWallet;

public class CreateSharedWalletPresenter extends CreateWalletPresenter<ICreateSharedWalletView> {
    private int mSigners;
    private int mParticipants;

    protected void buildWallet(String password, String walletName, String node){
        createWallet(password, walletName, node, buildInited(mParticipants, mSigners), mWalletColor, mLanguage, mActivity);
    }

    public AdapterView.OnItemSelectedListener OnSelectSignatures() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((mSigners = getSigners(position)) > mParticipants) {
                    mView.fixParticipants(getParticipantsPosition(mSigners));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if ((mSigners = getSigners(0)) > mParticipants) {
                    mView.fixParticipants(getParticipantsPosition(mSigners));
                }
            }
        };
    }

    public AdapterView.OnItemSelectedListener OnSelectMembers() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSigners > (mParticipants = getParticipants(position))) {
                    mView.fixSigners(getSignersPosition(mParticipants));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (mSigners > (mParticipants = getParticipants(0))) {
                    mView.fixSigners(getSignersPosition(mParticipants));
                }
            }
        };
    }

    private int getSignersPosition(int signers) {
        return signers - 2;
    }

    private int getParticipantsPosition(int participants) {
        return participants - 2;
    }

    private int getParticipants(int position) {
        return position + 2;
    }

    private int getSigners(int position) {
        return position + 2;
    }
}