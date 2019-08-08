package br.com.binmarques.githubrepositories.api;

import java.util.List;
import java.util.Map;

import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import br.com.binmarques.githubrepositories.model.GitHubRepo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By Daniel Marques on 24/07/2018
 */

public class GitHubServiceApiImpl implements GitHubServiceApi {

    @Override
    public Disposable findRepositories(Map<String, String> map, final GitHubServiceCallback<GitHubRepo> callback) {
        return callService(map).subscribe(callback::onLoaded, callback::onError);
    }

    @Override
    public Disposable findPullRequests(String author, String repo,
                                       final GitHubServiceCallback<List<GitHubPullRequest>> callback) {
        return callService(author, repo).subscribe(callback::onLoaded, callback::onError);
    }

    private Observable<GitHubRepo> callService(Map<String, String> map) {
        return ServiceGenerator
                .createService(GitHubService.class)
                .getRepositories(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<GitHubPullRequest>> callService(String author, String repo) {
        return ServiceGenerator
                .createService(GitHubService.class)
                .getPullRequests(author, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
