package br.com.binmarques.githubrepositories.pullrequests;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.binmarques.githubrepositories.base.BasePresenter;
import br.com.binmarques.githubrepositories.base.BaseView;
import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import br.com.binmarques.githubrepositories.model.Item;

/**
 * Created By Daniel Marques on 25/07/2018
 */

public interface GitHubPullRequestContract {

    interface View extends BaseView<Presenter> {

        void showEmptyView(boolean visible);

        void showProgress(boolean visible);

        void addItems(List<GitHubPullRequest> items);

        void hideRefreshing();

        void setErrorMessage(String errorMessage);

        void showConnectionError();

        void hideConnectionError();

        boolean hasNetwork();

    }

    interface Presenter extends BasePresenter {

        void setRepository(@NonNull Item repo);

        void loadLocalDataSource();

        void clearSubscriptions();

    }

}
