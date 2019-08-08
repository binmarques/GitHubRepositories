package br.com.binmarques.githubrepositories.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

/**
 * Created by Daniel Marques on 24/07/2018
 */

public abstract class BaseFragment extends Fragment {

    private static Toast sToast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater
                .inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
    }

    protected abstract int getFragmentLayout();

    private void bindViews(final View view) {
        ButterKnife.bind(this, view);
    }

    protected void showMessage(String message) {
        if (sToast != null) {
            sToast.cancel();
        }

        sToast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        sToast.show();
    }

}
