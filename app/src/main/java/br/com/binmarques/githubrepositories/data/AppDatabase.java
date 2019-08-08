package br.com.binmarques.githubrepositories.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import br.com.binmarques.githubrepositories.model.Item;
import br.com.binmarques.githubrepositories.model.Owner;
import br.com.binmarques.githubrepositories.model.User;

/**
 * Created By Daniel Marques on 25/07/2018
 */

@Database(
        entities = {Item.class, Owner.class, GitHubPullRequest.class, User.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public abstract ReposDao reposDao();

    public abstract PullRequestsDao pullRequestsDao();

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = Room
                        .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "GitHubRepos.db")
                        .build();
            }
            return sInstance;
        }
    }

}
