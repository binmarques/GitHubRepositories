package br.com.binmarques.githubrepositories.pullrequests;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import br.com.binmarques.githubrepositories.R;
import br.com.binmarques.githubrepositories.api.GitHubServiceApiImpl;
import br.com.binmarques.githubrepositories.base.BaseActivity;
import br.com.binmarques.githubrepositories.data.AppDatabase;
import br.com.binmarques.githubrepositories.data.PullRequestsLocalDataSourceImpl;
import br.com.binmarques.githubrepositories.model.Item;
import br.com.binmarques.githubrepositories.repositories.GitHubReposFragment;
import br.com.binmarques.githubrepositories.util.ActivityUtils;
import butterknife.BindView;

/**
 * Created by Daniel Marques on 25/07/2018
 */

public class GitHubPullRequestActivity extends BaseActivity {

    @BindView(R.id.toolbar) protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHubPullRequestFragment fragment =
                (GitHubPullRequestFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (fragment == null) {
            fragment = GitHubPullRequestFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        AppDatabase db = AppDatabase.getInstance(this);

        GitHubPullRequestContract.Presenter presenter =
                new GitHubPullRequestPresenter(new GitHubServiceApiImpl(),
                        fragment, new PullRequestsLocalDataSourceImpl(db.pullRequestsDao()));

        if (getIntent().getParcelableExtra(GitHubReposFragment.EXTRA_REPO) != null) {
            Item repo = getIntent().getParcelableExtra(GitHubReposFragment.EXTRA_REPO);

            if (repo != null) {
                String title = getString(R.string.title_pull_requests) + " - " + repo.getName();
                setTitle(title);
                presenter.setRepository(repo);
            }
        }
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pull_request;
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return true;
    }
}
