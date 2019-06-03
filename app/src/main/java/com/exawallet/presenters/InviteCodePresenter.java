package com.exawallet.presenters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.widget.Toast;
import com.exawallet.RootActivity;
import com.exawallet.fragments.SyncSplashFragment;
import com.exawallet.model.meta.CreatingMeta;
import com.exawallet.model.meta.SharedStage;
import com.exawallet.monerowallet.R;
import com.exawallet.views.IInviteCodeView;
import org.greenrobot.eventbus.Subscribe;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.widget.Toast.makeText;
import static com.exawallet.model.meta.SharedStage.DONE;
import static org.greenrobot.eventbus.ThreadMode.MAIN;

public class InviteCodePresenter extends AttachedPresenter<IInviteCodeView> {
    private CreatingMeta mCreatingMeta;

    @Override
    public void onAttach(RootActivity activity) {
        super.onAttach(activity);
        mCreatingMeta = mWalletMeta.getSharedMeta().getCreatingMeta();
    }

    @Override
    public void onResume() {
        super.onResume();

        mView.setInviteCode(mCreatingMeta.getInviteCode());
        mView.showQRImage(mCreatingMeta.getQRInviteCode());
        setParticipantsProgress();

        if (mWalletMeta.isReady()) {
            mWallet.startRefresh();
            mActivity.show(SyncSplashFragment.newInstance());
        }
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(final SharedStage event) {
        setParticipantsProgress();

        if (DONE == event) {
            mWallet.startRefresh();
            mActivity.show(SyncSplashFragment.newInstance());
        }
    }

    private void setParticipantsProgress() {
        mView.setParticipantsProgress(null == mCreatingMeta.getMultisigInfos() ? 0 : mCreatingMeta.getMultisigInfos().length, mWalletMeta.getSharedMeta().getParticipants());
    }

    public void onCopyButtoon() {
        ((ClipboardManager) mActivity.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", mCreatingMeta.getInviteCode()));
        makeText(mActivity, R.string.invite_code_clipboard, Toast.LENGTH_SHORT).show();
    }

    public void onShareButton() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, mCreatingMeta.getInviteCode());
        intent.setType("text/plain");
        mActivity.startActivity(intent);
    }
}