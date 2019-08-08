package br.com.binmarques.githubrepositories.model;

import java.util.Arrays;

/**
 * Created by Daniel Marques on 24/07/2018
 */

public class GitHubRepo {

    private Item[] items;

    public GitHubRepo() {}

    public GitHubRepo(Item[] items) {
        this.items = items;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "GitHubRepo{" +
                "items=" + Arrays.toString(items) +
                '}';
    }
}
