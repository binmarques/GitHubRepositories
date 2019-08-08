package br.com.binmarques.githubrepositories.pullrequests;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.binmarques.githubrepositories.api.GitHubServiceApi;
import br.com.binmarques.githubrepositories.api.HandleUnexpectedError;
import br.com.binmarques.githubrepositories.data.PullRequestsLocalDataSource;
import br.com.binmarques.githubrepositories.model.ApiError;
import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import br.com.binmarques.githubrepositories.model.Item;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

/**
 * Created By Daniel Marques on 25/07/2018
 */

public class GitHubPullRequestPresenter implements GitHubPullRequestContract.Presenter {

    private GitHubServiceApi mGitHubServiceApi;
    private GitHubPullRequestContract.View mPullRequestView;
    private PullRequestsLocalDataSource mLocalDataSource;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private Item mRepository;

    public GitHubPullRequestPresenter(GitHubServiceApi api,
                                      GitHubPullRequestContract.View pullRequestView,
                                      PullRequestsLocalDataSource localDataSource) {
        this.mGitHubServiceApi = api;
        this.mPullRequestView = pullRequestView;
        this.mLocalDataSource = localDataSource;
        mPullRequestView.setPresenter(this);
    }

    @Override
    public void start() {
        loadPullRequests();
    }

    @Override
    public void setRepository(@NonNull Item repo) {
        this.mRepository = repo;
    }

    @Override
    public void loadLocalDataSource() {
        String repoName = mRepository.getName();
        String ownerLogin = mRepository.getOwner().getLogin();

        mDisposable.add(mLocalDataSource.findPullRequests(ownerLogin, repoName, gitHubPullRequests -> {
            mPullRequestView.showEmptyView(false);
            mPullRequestView.showProgress(false);
            mPullRequestView.hideRefreshing();
            mPullRequestView.hideConnectionError();

            if (gitHubPullRequests != null && !gitHubPullRequests.isEmpty()) {
                mPullRequestView.addItems(gitHubPullRequests);
            } else {
                mPullRequestView.showEmptyView(true);
                mPullRequestView.showConnectionError();
            }
        }));
    }

    @Override
    public void clearSubscriptions() {
        mDisposable.clear();
    }

    private void loadPullRequests() {
        String repoName = mRepository.getName();
        String ownerLogin = mRepository.getOwner().getLogin();

        mDisposable.add(mGitHubServiceApi.findPullRequests(ownerLogin, repoName,
        new GitHubServiceApi.GitHubServiceCallback<List<GitHubPullRequest>>() {
            @Override
            public void onLoaded(List<GitHubPullRequest> gitHubPullRequests) {
                mPullRequestView.showEmptyView(false);
                mPullRequestView.showProgress(false);
                mPullRequestView.hideRefreshing();

                if (gitHubPullRequests != null && !gitHubPullRequests.isEmpty()) {
                    for (GitHubPullRequest pullRequest : gitHubPullRequests) {
                        pullRequest.setItem(mRepository);
                    }

                    mPullRequestView.addItems(gitHubPullRequests);
                    mLocalDataSource.savePullRequests(gitHubPullRequests);
                } else {
                    mPullRequestView.showEmptyView(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                mPullRequestView.showProgress(false);
                mPullRequestView.showEmptyView(true);
                mPullRequestView.hideRefreshing();

                if (e instanceof HttpException) {
                    ApiError error = HandleUnexpectedError.parseError(((HttpException) e).response());

                    if (error != null) {
                        mPullRequestView.setErrorMessage(error.getMessage());
                    }

                } else {
                    Log.i(TAG, "onError: " + e.getMessage());
                    e.printStackTrace();
                    mPullRequestView.showConnectionError();
                }
            }
        }));
    }
}
