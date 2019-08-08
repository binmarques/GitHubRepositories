package br.com.binmarques.githubrepositories.data;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By Daniel Marques on 30/07/2018
 */

public class PullRequestsLocalDataSourceImpl implements PullRequestsLocalDataSource {

    private PullRequestsDao mPullRequestsDao;

    public PullRequestsLocalDataSourceImpl(PullRequestsDao pullRequestsDao) {
        this.mPullRequestsDao = pullRequestsDao;
    }

    @Override
    public void savePullRequests(@NonNull final List<GitHubPullRequest> gitHubPullRequests) {
        insert(gitHubPullRequests)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public Disposable findPullRequests(@NonNull final String author,
                                       @NonNull final String repo,
                                       @NonNull final LocalDataSourceCallback<List<GitHubPullRequest>> callback) {
        return mPullRequestsDao
                .findPullRequests(author, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded);
    }

    private Completable insert(final List<GitHubPullRequest> gitHubPullRequests) {
        return Completable.fromAction(() -> mPullRequestsDao.insertPullRequests(gitHubPullRequests));
    }
}
