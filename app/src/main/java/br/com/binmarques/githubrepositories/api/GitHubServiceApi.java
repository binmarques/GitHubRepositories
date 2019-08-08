package br.com.binmarques.githubrepositories.api;

import java.util.List;
import java.util.Map;

import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import br.com.binmarques.githubrepositories.model.GitHubRepo;
import io.reactivex.disposables.Disposable;


/**
 * Created By Daniel Marques on 24/07/2018
 */

public interface GitHubServiceApi {

    interface GitHubServiceCallback<T> {

        void onLoaded(T t);

        void onError(Throwable e);

    }

    Disposable findRepositories(Map<String, String> map, GitHubServiceCallback<GitHubRepo> callback);

    Disposable findPullRequests(String author, String repo, GitHubServiceCallback<List<GitHubPullRequest>> callback);

}
