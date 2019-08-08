package br.com.binmarques.githubrepositories.api;

import java.util.List;
import java.util.Map;

import br.com.binmarques.githubrepositories.model.GitHubPullRequest;
import br.com.binmarques.githubrepositories.model.GitHubRepo;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Daniel Marques on 24/07/2018
 */

public interface GitHubService {

    @GET("search/repositories")
    Observable<GitHubRepo> getRepositories(@QueryMap Map<String, String> map);

    @GET("repos/{author}/{repo}/pulls")
    Observable<List<GitHubPullRequest>> getPullRequests(@Path("author") String author,
                                                        @Path("repo") String repo);

}
