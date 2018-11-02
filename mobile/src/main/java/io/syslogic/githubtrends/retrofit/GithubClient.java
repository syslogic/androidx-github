package io.syslogic.githubtrends.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import io.syslogic.githubtrends.constants.Constants;

public class GithubClient {

    private static Retrofit retrofit;

    public static Retrofit init() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
            .baseUrl(Constants.GITHUB_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        }
        return retrofit;
    }
}
