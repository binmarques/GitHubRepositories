package br.com.binmarques.githubrepositories.model;

/**
 * Created by Daniel Marques on 25/07/2018
 */

public class ApiError {

    private String message;

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
