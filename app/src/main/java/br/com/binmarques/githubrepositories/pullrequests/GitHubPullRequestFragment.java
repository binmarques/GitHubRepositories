package br.com.binmarques.githubrepositories.pullrequests;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import br.com.binmarques.githubrepositories.R;
import br.com.binmarques.githubrepositories.adapter.GitHubPullRequestsAdapter;
import br.com.binmarques.githubrepositories.base.BaseFragment;
import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import br.com.binmarques.githubrepositories.util.ActivityUtils;
import butterknife.BindView;

/**
 * Created by Daniel Marques on 25/07/2018
 */

public class GitHubPullRequestFragment extends BaseFragment implements GitHubPullRequestsAdapter.OnItemClickListener,
                                                                       SwipeRefreshLayout.OnRefreshListener,
                                                                       GitHubPullRequestContract.View {

    @BindView(R.id.recyclerViewPull) protected RecyclerView mRecyclerView;
    @BindView(R.id.tvEmptyView) protected TextView mEmptyView;
    @BindView(R.id.loadingProgress) protected ProgressBar mProgressBar;
    @BindView(R.id.containerSwipe) protected SwipeRefreshLayout mSwipeRefresh;

    private Snackbar mSnackbar;
    private GitHubPullRequestsAdapter mAdapter;
    private GitHubPullRequestContract.Presenter mPresenter;

    private static final String PULL_REQUESTS_KEY = "PULL_REQUESTS_KEY";
    private static final int BROWSER_DELAY = 200;

    public GitHubPullRequestFragment() {}

    public static GitHubPullRequestFragment newInstance() {
        return new GitHubPullRequestFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.setProgressBackgroundColorSchemeColor(getResources()
                .getColor(R.color.colorPrimary));

        mSwipeRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimaryLight,
                R.color.background
        );

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GitHubPullRequestsAdapter(getActivity(), new ArrayList<>());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState == null) {
            mPresenter.loadLocalDataSource();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_pull_requests_layout;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!mAdapter.getItems().isEmpty()) {
            outState.putParcelableArrayList(PULL_REQUESTS_KEY,
                    new ArrayList<Parcelable>(mAdapter.getItems()));
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(PULL_REQUESTS_KEY)) {
            List<GitHubPullRequest> items = savedInstanceState.getParcelableArrayList(PULL_REQUESTS_KEY);

            if (items != null && !items.isEmpty()) {
                showProgress(false);
                addItems(items);
            }
        }
    }

    @Override
    public void onRefresh() {
        if (getActivity() == null) {
            return;
        }

        int duration = getResources()
                .getInteger(android.R.integer.config_longAnimTime);

        if (mSwipeRefresh.isRefreshing()) {
            if (ActivityUtils.isNetworkAvailable(getActivity())) {
                mSwipeRefresh.postDelayed(() -> {
                    mAdapter.clearItems();
                    mPresenter.start();
                }, duration);
            } else {
                hideRefreshing();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.clearSubscriptions();
    }

    @Override
    public void setPresenter(@NonNull GitHubPullRequestContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onItemClick(@NonNull View view, int position) {
        final GitHubPullRequest item = mAdapter.getItem(position);

        if (item != null) {
            view.postDelayed(() -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(item.getHtmlUrl()));
                startActivity(i);
            }, BROWSER_DELAY);
        }
    }

    @Override
    public void showEmptyView(boolean visible) {
        mEmptyView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgress(boolean visible) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void addItems(List<GitHubPullRequest> items) {
        mAdapter.addItems(items);
    }

    @Override
    public void hideRefreshing() {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        String message = getString(R.string.title_fail) + errorMessage;
        mEmptyView.setText(message);
    }

    @Override
    public void showConnectionError() {
        if (getView() == null) {
            return;
        }

        mSnackbar = Snackbar.make(getView(),
                getString(R.string.connection_failed_to_network_snackbar),
                Snackbar.LENGTH_INDEFINITE);

        if (!mSnackbar.isShown()) {
            mSnackbar.show();
        }

        mSnackbar.setAction(R.string.title_retry_snackbar, v -> {
            showEmptyView(false);

            if (mAdapter.getItemCount() <= 0) {
                showProgress(true);
                mPresenter.start();
            }
        });
    }

    @Override
    public void hideConnectionError() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    @Override
    public boolean hasNetwork() {
        if (getActivity() == null) {
            return false;
        }

        return ActivityUtils.isNetworkAvailable(getActivity());
    }

}
