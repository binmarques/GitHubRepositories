package br.com.binmarques.githubrepositories.repositories;

import java.util.List;

import br.com.binmarques.githubrepositories.base.BasePresenter;
import br.com.binmarques.githubrepositories.base.BaseView;
import br.com.binmarques.githubrepositories.model.Item;

/**
 * Created By Daniel Marques on 24/07/2018
 */

public interface GitHubReposContract {

    interface View extends BaseView<Presenter> {

        void addLoadingFooter();

        void removeLoadingFooter();

        void showReload(boolean show);

        void setLastPage(boolean isLastPage);

        void setLoading(boolean isLoading);

        void showEmptyView(boolean visible);

        void showProgress(boolean visible);

        void showConnectionError();

        void hideConnectionError();

        void hideRefreshing();

        void setErrorMessage(String errorMessage);

        void setErrorMessage(int errorMessage);

        void showErrorMessage(String message);

        int getCurrentPage();

        void backPreviousPage();

        void addItems(List<Item> items);

    }

    interface Presenter extends BasePresenter {

        int TOTAL_PAGES = 50;

        void loadNextPage();

        void loadLocalDataSource();

        void clearSubscriptions();
    }

}
