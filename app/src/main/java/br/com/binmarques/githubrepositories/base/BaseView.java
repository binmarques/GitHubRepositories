package br.com.binmarques.githubrepositories.base;

import androidx.annotation.NonNull;

/**
 * Created By Daniel Marques 24/07/2018
 */

public interface BaseView<T> {

    void setPresenter(@NonNull T presenter);

}
