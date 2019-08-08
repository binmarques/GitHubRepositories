package br.com.binmarques.githubrepositories.api;

import java.lang.annotation.Annotation;

import br.com.binmarques.githubrepositories.model.ApiError;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Daniel Marques on 24/07/2018
 */

public class HandleUnexpectedError {

    public static ApiError parseError(Response<?> response) {
        Converter<ResponseBody, ApiError> converter =
                ServiceGenerator.getConverter(ApiError.class, new Annotation[0]);

        ApiError error = null;

        try {
            ResponseBody errorBody = response.errorBody();

            if (errorBody != null) {
                error = converter.convert(errorBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return error;
    }

}
