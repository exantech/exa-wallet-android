package com.exawallet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.exawallet.RootActivity;
import com.exawallet.fragments.adapters.TransactionsAdapter;
import com.exawallet.fragments.adapters.TxProposalsAdapter;
import com.exawallet.model.TransactionInfo;
import com.exawallet.model.meta.TxProposalsMeta;
import com.exawallet.monerowallet.R;
import com.exawallet.presenters.BalancePresenter;
import com.exawallet.toolbars.BaseHomeToolbar;
import com.exawallet.toolbars.WalletManageToolbar;
import com.exawallet.utils.HLog;
import com.exawallet.views.IBalanceView;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.exawallet.model.WalletManager.walletManager;
import static com.exawallet.utils.BackPath.BACK_PATH;
import static com.exawallet.utils.Screens.SELECT_WALLET_SCREEN;
import static com.exawallet.utils.Utils.*;

public class BalanceFragment extends BaseSyncWalletFragment<BalancePresenter, IBalanceView, BaseHomeToolbar> implements IBalanceView {
    @BindView(R.id.chart_fab)
    View mChartFab;

    @BindView(R.id.send_fab)
    View mSendFab;

    @BindView(R.id.receive_fab)
    View mReceiveFab;

    @BindView(R.id.wallet_type)
    TextView mWalletType;

    @BindView(R.id.sync_status)
    View mSyncStatus;

    @BindView(R.id.balance)
    TextView mBalance;

    @BindView(R.id.balance_usd)
    TextView mBalanceUsd;

    @BindView(R.id.balance_unconfirmed)
    TextView mBalanceUnconfirmed;

    @BindView(R.id.txproposals_layout)
    View mTxProposalsLayout;

    @BindView(R.id.txproposals)
    RecyclerView mTxProposals;

    @BindView(R.id.empty_transactions)
    View mEmptyTransactions;

    @BindView(R.id.transactions)
    RecyclerView mTransactions;

    private TransactionsAdapter mTransactionsAdapter;
    private TxProposalsAdapter mTxProposalsAdapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_balance;
    }

    @Override
    protected BalancePresenter createPresenter() {
        return new BalancePresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChartFab.setOnClickListener(v -> mPresenter.onChart());
        mSendFab.setOnClickListener(v -> mPresenter.onSend());
        mReceiveFab.setOnClickListener(v -> mPresenter.onReceive());

        mTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTransactions.setAdapter(mTransactionsAdapter = new TransactionsAdapter((RootActivity) getActivity()));

        mTxProposals.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTxProposals.setAdapter(mTxProposalsAdapter = new TxProposalsAdapter((RootActivity) getActivity(), walletManager().getWallet().getPublicMultisigSignerKey(), walletManager().getWallet().getWalletMeta().getSharedMeta()));

        BACK_PATH.setScreen(SELECT_WALLET_SCREEN);
    }

    @Override
    protected BaseHomeToolbar createToolbar(View view) {
        return new WalletManageToolbar(view, R.string.title_balance, (RootActivity) getActivity()) {

            @Override
            protected void onButton() {
                ((RootActivity) getActivity()).show(WalletManageFragment.newInstance());
            }
        };
    }

    @Override
    public void setWalletType(int text) {
        mWalletType.setText(text);
    }

    @Override
    public void setSyncStatus(int image) {
        if (null != mSyncStatus) {
            mSyncStatus.setBackgroundResource(image);
        }
    }

    @Override
    public void showBalance(long balance) {
        if (null != mBalance) {
            mBalance.setText(formatAmountB(balance));
        }
    }

    @Override
    public void showBalanceUsd(double balance) {
        if (null != mBalanceUsd) {
            mBalanceUsd.setText(formatBalanceUsd(balance));
        }
    }

    @Override
    public void showBalanceUnconfirmed(long balance) {
        if (null != mBalanceUnconfirmed) {
            mBalanceUnconfirmed.setText(0 != balance ? formatUnconfirmedAmount(balance) : null);
        }
    }

    @Override
    public void showSync(long blockNumber) {
        HLog.debug(TAG, "WalletListener showSync " + blockNumber);
    }

    public void showTransactions(List<TransactionInfo> transactions) {
        if (null != mEmptyTransactions && null != mTransactions && null != mTransactionsAdapter) {
            if (null == transactions || transactions.isEmpty()) {
                mEmptyTransactions.setVisibility(VISIBLE);
                mTransactions.setVisibility(GONE);
            } else {
                mEmptyTransactions.setVisibility(GONE);
                mTransactions.setVisibility(VISIBLE);
                mTransactionsAdapter.setItems(transactions);
            }
        }
    }

    public void showTxProposals(boolean isMultisig, List<TxProposalsMeta> txProposalsMeta) {
        if (isMultisig) {
            mTxProposalsLayout.setVisibility(VISIBLE);
            if (null != mTxProposalsLayout && null != mTxProposalsAdapter) {
                if (null != txProposalsMeta && !txProposalsMeta.isEmpty()) {
                    mTxProposalsAdapter.setItems(txProposalsMeta);
                }

                mTxProposalsLayout.setVisibility(0 == mTxProposalsAdapter.getItemCount() ? GONE : VISIBLE);
            }
        } else {
            mTxProposalsLayout.setVisibility(GONE);
        }
    }

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }
}