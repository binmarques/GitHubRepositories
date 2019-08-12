package br.com.binmarques.githubrepositories.data;

import androidx.annotation.NonNull;

import java.util.List;

import br.com.binmarques.githubrepositories.model.Item;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created By Daniel Marques on 25/07/2018
 */

public class ReposLocalDataSourceImpl implements ReposLocalDataSource {

    private ReposDao mReposDao;

    public ReposLocalDataSourceImpl(ReposDao reposDao) {
        this.mReposDao = reposDao;
    }

    @Override
    public void saveRepositories(@NonNull List<Item> items) {
        insert(items)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void deleteRepositories() {
        delete()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public Disposable findRepositories(@NonNull final LocalDataSourceCallback<List<Item>> callback) {
        return mReposDao
                .getRepositories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::onLoaded);
    }

    private Completable insert(List<Item> items) {
        return Completable.fromAction(() -> mReposDao.insertRepositories(items));
    }

    private Completable delete() {
        return Completable.fromAction(() -> mReposDao.deleteRepositories());
    }

}
