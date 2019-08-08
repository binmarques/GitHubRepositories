package br.com.binmarques.githubrepositories.data;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.binmarques.githubrepositories.model.Item;
import io.reactivex.disposables.Disposable;

/**
 * Created By Daniel Marques on 25/07/2018
 */

public interface ReposLocalDataSource {

    void saveRepositories(@NonNull List<Item> items);

    Disposable findRepositories(@NonNull LocalDataSourceCallback<List<Item>> callback);

}
