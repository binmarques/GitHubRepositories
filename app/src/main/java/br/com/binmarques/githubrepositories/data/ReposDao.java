package br.com.binmarques.githubrepositories.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import br.com.binmarques.githubrepositories.model.Item;
import io.reactivex.Maybe;

/**
 * Created By Daniel Marques on 25/07/2018
 */

@Dao
public interface ReposDao {

    @Query("SELECT * FROM Repositories ORDER BY star_count DESC")
    Maybe<List<Item>> getRepositories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepositories(List<Item> repos);

    @Query("DELETE FROM Repositories")
    void deleteRepositories();

}
