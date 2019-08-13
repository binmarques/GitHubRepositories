package br.com.binmarques.githubrepositories.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import io.reactivex.Flowable;

/**
 * Created By Daniel Marques on 30/07/2018
 */

@Dao
public interface PullRequestsDao {

    @Query("SELECT * FROM PullRequests WHERE owner_login = :author AND name = :repo ORDER BY created_at DESC")
    Flowable<List<GitHubPullRequest>> findPullRequests(String author, String repo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPullRequests(List<GitHubPullRequest> gitHubPullRequests);

}
