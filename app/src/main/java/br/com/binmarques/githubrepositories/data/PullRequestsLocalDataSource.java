package br.com.binmarques.githubrepositories.data;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import io.reactivex.disposables.Disposable;

/**
 * Created By Daniel Marques on 30/07/2018
 */

public interface PullRequestsLocalDataSource {

    void savePullRequests(@NonNull List<GitHubPullRequest> gitHubPullRequests);

    Disposable findPullRequests(@NonNull String author,
                                @NonNull String repo,
                                @NonNull LocalDataSourceCallback<List<GitHubPullRequest>> callback);

}
