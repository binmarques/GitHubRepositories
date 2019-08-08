package br.com.binmarques.githubrepositories.data;

/**
 * Created By Daniel Marques on 30/07/2018
 */

public interface LocalDataSourceCallback<T> {

    void onLoaded(T t);

}
