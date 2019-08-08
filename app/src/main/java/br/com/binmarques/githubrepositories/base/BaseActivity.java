package br.com.binmarques.githubrepositories.base;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import br.com.binmarques.githubrepositories.R;
import butterknife.ButterKnife;

/**
 * Created by Daniel Marques on 24/07/2018
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        bindViews();

        if (getToolbar() != null) {
            Toolbar toolbar = getToolbar();
            setSupportActionBar(toolbar);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Window window = getWindow();
            window.setStatusBarColor(ContextCompat
                    .getColor(this, R.color.colorPrimaryDark));
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isDisplayHomeAsUpEnabled());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    protected abstract Toolbar getToolbar();

    protected abstract int getLayoutResource();

    protected abstract boolean isDisplayHomeAsUpEnabled();

    private void bindViews() {
        ButterKnife.bind(this);
    }

}
