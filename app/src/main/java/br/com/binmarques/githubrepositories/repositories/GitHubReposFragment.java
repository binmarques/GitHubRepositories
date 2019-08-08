package br.com.binmarques.githubrepositories.repositories;

import android.content.Intent;
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
import br.com.binmarques.githubrepositories.adapter.GitHubReposAdapter;
import br.com.binmarques.githubrepositories.base.BaseFragment;
import br.com.binmarques.githubrepositories.model.Item;
import br.com.binmarques.githubrepositories.pullrequests.GitHubPullRequestActivity;
import br.com.binmarques.githubrepositories.util.ActivityUtils;
import butterknife.BindView;

/**
 * Created by Daniel Marques on 24/07/2018
 */

public class GitHubReposFragment extends BaseFragment implements GitHubReposAdapter.OnItemClickListener,
                                                                 GitHubReposAdapter.PaginationCallback,
                                                                 SwipeRefreshLayout.OnRefreshListener,
                                                                 GitHubReposContract.View {

    @BindView(R.id.recyclerViewRepos) protected RecyclerView mRecyclerView;
    @BindView(R.id.tvEmptyView) protected TextView mEmptyView;
    @BindView(R.id.loadingProgress) protected ProgressBar mProgressBar;
    @BindView(R.id.containerSwipe) protected SwipeRefreshLayout mSwipeRefresh;

    private GitHubReposAdapter mAdapter;
    private GitHubReposContract.Presenter mPresenter;
    private Snackbar mSnackbar;
    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;

    private static final int PAGE_START = 1;
    private int mCurrentPage = PAGE_START;
    private static final String CURRENT_PAGE_KEY = "CURRENT_PAGE_KEY";
    private static final String REPOSITORIES_KEY = "REPOSITORIES_KEY";
    private static final String SHOULD_REMOVE_LOADING_FOOTER_KEY = "SHOULD_REMOVE_LOADING_FOOTER_KEY";
    public static final String EXTRA_REPO = "EXTRA_REPO";

    public GitHubReposFragment() {}

    public static GitHubReposFragment newInstance() {
        return new GitHubReposFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark
        );

        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new GitHubReposAdapter(getActivity(), new ArrayList<>(), this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new EndlessScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                if (getActivity() == null) {
                    return;
                }

                if (ActivityUtils.isNetworkAvailable(getActivity())) {
                    setLoading(true);
                    mCurrentPage++;
                    mPresenter.loadNextPage();
                } else if (!mAdapter.isShownReload()) {
                    showReload(true);
                }
            }

            @Override
            public boolean isLastPage() {
                return mIsLastPage;
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }
        });

        if (savedInstanceState == null) {
            loadData();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_repositories_layout;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!mAdapter.getItems().isEmpty()) {
            outState.putParcelableArrayList(REPOSITORIES_KEY, new ArrayList<Parcelable>(mAdapter.getItems()));
            outState.putBoolean(SHOULD_REMOVE_LOADING_FOOTER_KEY, mAdapter.isLoadingAdded());
            outState.putInt(CURRENT_PAGE_KEY, getCurrentPage());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(REPOSITORIES_KEY)) {
            List<Item> items = savedInstanceState.getParcelableArrayList(REPOSITORIES_KEY);
            mCurrentPage = savedInstanceState.getInt(CURRENT_PAGE_KEY);

            boolean shouldRemoveLoadingFooter =
                    savedInstanceState.getBoolean(SHOULD_REMOVE_LOADING_FOOTER_KEY);

            if (items != null && !items.isEmpty()) {
                showProgress(false);
                addItems(items);
                showReload(false);
                setLoading(false);

                if (shouldRemoveLoadingFooter) {
                    removeLoadingFooter();
                }

                if (getCurrentPage() <= mPresenter.TOTAL_PAGES) {
                    addLoadingFooter();
                } else {
                    setLastPage(true);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        int duration = getResources()
                .getInteger(android.R.integer.config_longAnimTime);

        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.postDelayed(() -> {
                mIsLastPage = false;
                mCurrentPage = PAGE_START;
                mAdapter.clearItems();
                loadData();
            }, duration);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.clearSubscriptions();
    }

    @Override
    public void setPresenter(@NonNull GitHubReposContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onItemClick(@NonNull View view, int position) {
        Item item = mAdapter.getItem(position);

        if (item != null) {
            startActivity(new Intent(getActivity(), GitHubPullRequestActivity.class)
                    .putExtra(EXTRA_REPO, item));
        }
    }

    @Override
    public void onPageReloaded() {
        mCurrentPage++;
        mPresenter.loadNextPage();
    }

    @Override
    public void addLoadingFooter() {
        mAdapter.addLoadingFooter();
    }

    @Override
    public void removeLoadingFooter() {
        mAdapter.removeLoadingFooter();
    }

    @Override
    public void showReload(final boolean show) {
        mRecyclerView.post(() -> mAdapter.showReload(show));
    }

    @Override
    public void setLastPage(boolean isLastPage) {
        mIsLastPage = isLastPage;
    }

    @Override
    public void setLoading(boolean isLoading) {
        mIsLoading = isLoading;
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
    public void showConnectionError() {
        if (getView() == null) {
            return;
        }

        mSnackbar = Snackbar.make(getView(),
                R.string.connection_failed_to_network_snackbar,
                Snackbar.LENGTH_INDEFINITE);

        if (!mSnackbar.isShown()) {
            mSnackbar.show();
        }

        mSnackbar.setAction(R.string.title_retry_snackbar, v -> {
            showEmptyView(false);

            if (mAdapter.getItemCount() <= 0) {
                showProgress(true);
                loadData();
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
    public void hideRefreshing() {
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void setErrorMessage(int errorMessage) {
        showMessage(getString(errorMessage));
    }

    @Override
    public void showErrorMessage(String message) {
        showMessage(message);
    }

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public void backPreviousPage() {
        mCurrentPage--;
    }

    @Override
    public void addItems(List<Item> items) {
        mAdapter.addItems(items);
    }

    public void setErrorMessage(String errorMessage) {
        String message = getString(R.string.title_fail) + errorMessage;
        mEmptyView.setText(message);
    }

    private void loadData() {
        if (getActivity() == null) {
            return;
        }

        if (ActivityUtils.isNetworkAvailable(getActivity())) {
            mPresenter.start();
        } else {
            mPresenter.loadLocalDataSource();
        }
    }
}
