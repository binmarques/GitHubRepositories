package br.com.binmarques.githubrepositories.repositories;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.binmarques.githubrepositories.R;
import br.com.binmarques.githubrepositories.api.GitHubServiceApi;
import br.com.binmarques.githubrepositories.api.HandleUnexpectedError;
import br.com.binmarques.githubrepositories.data.ReposLocalDataSource;
import br.com.binmarques.githubrepositories.model.ApiError;
import br.com.binmarques.githubrepositories.model.GitHubRepo;
import br.com.binmarques.githubrepositories.model.Item;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

/**
 * Create By Daniel Marques on 24/07/2018
 */

public class GitHubReposPresenter implements GitHubReposContract.Presenter {

    private GitHubServiceApi mGitHubServiceApi;
    private GitHubReposContract.View mReposView;
    private ReposLocalDataSource mLocalDataSource;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private static final int PER_PAGE = 10;

    public GitHubReposPresenter(GitHubServiceApi api,
                                GitHubReposContract.View reposView,
                                ReposLocalDataSource localDataSource) {
        this.mGitHubServiceApi = api;
        this.mReposView = reposView;
        this.mLocalDataSource = localDataSource;
        mReposView.setPresenter(this);
    }

    @Override
    public void start() {
        loadFirstPage();
    }

    private void loadFirstPage() {
        Log.i(TAG, "loadFirstPage() ");

        mDisposable.add(mGitHubServiceApi.findRepositories(getParams(),
        new GitHubServiceApi.GitHubServiceCallback<GitHubRepo>() {
            @Override
            public void onLoaded(GitHubRepo gitHubRepo) {
                mReposView.showEmptyView(false);
                mReposView.showProgress(false);
                mReposView.hideRefreshing();
                mReposView.hideConnectionError();

                if (gitHubRepo != null && gitHubRepo.getItems().length > 0) {
                    List<Item> items = Arrays.asList(gitHubRepo.getItems());
                    mReposView.addItems(items);
                    verifyCurrentPage();
                    mLocalDataSource.saveRepositories(items);
                } else {
                    mReposView.showEmptyView(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                mReposView.showProgress(false);
                mReposView.showEmptyView(true);
                mReposView.hideRefreshing();

                if (e instanceof HttpException) {
                    ApiError error = HandleUnexpectedError.parseError(((HttpException) e).response());

                    if (error != null) {
                        mReposView.setErrorMessage(error.getMessage());
                    }

                } else {
                    Log.i(TAG, "onError: " + e.getMessage());
                    e.printStackTrace();
                    mReposView.showConnectionError();
                }
            }
        }));
    }

    @Override
    public void loadNextPage() {
        Log.i(TAG, "loadNextPage() " + mReposView.getCurrentPage());

        mDisposable.add(mGitHubServiceApi.findRepositories(getParams(),
        new GitHubServiceApi.GitHubServiceCallback<GitHubRepo>() {
            @Override
            public void onLoaded(GitHubRepo gitHubRepo) {
                mReposView.showReload(false);
                mReposView.removeLoadingFooter();
                mReposView.setLoading(false);

                if (gitHubRepo != null && gitHubRepo.getItems().length > 0) {
                    List<Item> items = Arrays.asList(gitHubRepo.getItems());
                    mReposView.addItems(items);
                    verifyCurrentPage();
                    mLocalDataSource.saveRepositories(items);
                }
            }

            @Override
            public void onError(Throwable e) {
                mReposView.setLoading(false);
                mReposView.backPreviousPage();

                if (e instanceof HttpException) {
                    ApiError error = HandleUnexpectedError.parseError(((HttpException) e).response());

                    if (error != null) {
                        mReposView.showErrorMessage(error.getMessage());
                    }

                } else {
                    Log.i(TAG, "onError: " + e.getMessage());
                    e.printStackTrace();
                    mReposView.setErrorMessage(R.string.connection_failed_to_network);
                    mReposView.showReload(true);
                }
            }
        }));
    }

    @Override
    public void loadLocalDataSource() {
        mDisposable.add(mLocalDataSource.findRepositories(items -> {
            mReposView.showProgress(false);
            mReposView.hideRefreshing();

            if (!items.isEmpty()) {
                mReposView.addItems(items);
                verifyCurrentPage();
            } else {
                mReposView.showEmptyView(true);
                mReposView.showConnectionError();
            }
        }));
    }

    @Override
    public void clearSubscriptions() {
        mDisposable.clear();
    }

    private void verifyCurrentPage() {
        if (mReposView.getCurrentPage() <= TOTAL_PAGES) {
            mReposView.addLoadingFooter();
        } else {
            mReposView.setLastPage(true);
        }
    }

    private Map<String, String> getParams() {
        Map<String, String> map = new HashMap<>();
        map.put("q", "language:Java");
        map.put("sort", "stars");
        map.put("page", String.valueOf(mReposView.getCurrentPage()));
        map.put("per_page", String.valueOf(PER_PAGE));
        return map;
    }

}
