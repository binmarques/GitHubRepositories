package br.com.binmarques.githubrepositories.repositories;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import br.com.binmarques.githubrepositories.R;
import br.com.binmarques.githubrepositories.api.GitHubServiceApiImpl;
import br.com.binmarques.githubrepositories.base.BaseActivity;
import br.com.binmarques.githubrepositories.data.AppDatabase;
import br.com.binmarques.githubrepositories.data.ReposLocalDataSourceImpl;
import br.com.binmarques.githubrepositories.util.ActivityUtils;
import butterknife.BindView;

/**
 * Created by Daniel Marques on 24/07/2018
 */

public class GitHubReposActivity extends BaseActivity {

    @BindView(R.id.toolbar) protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_repositories);
        ActivityUtils.updateAndroidSecurityProvider(this);

        GitHubReposFragment fragment =
                (GitHubReposFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (fragment == null) {
            fragment = GitHubReposFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        AppDatabase db = AppDatabase.getInstance(this);

        new GitHubReposPresenter(new GitHubServiceApiImpl(),
                fragment, new ReposLocalDataSourceImpl(db.reposDao()));
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_repos;
    }

    @Override
    protected boolean isDisplayHomeAsUpEnabled() {
        return false;
    }
}
