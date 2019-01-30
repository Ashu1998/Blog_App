package com.tinkerbyte.blog_app;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class GetPostApi {

    public static final String key = "AIzaSyCYUiWmdyq55LzbiiUyzG5S5-wCrNB667o";
    public static final String Url = "https://www.googleapis.com/blogger/v3/blogs/3869904152125771752/posts/";

    public static blogService mblogService = null;

    public static blogService getService()
    {
        if(mblogService==null)
        {
            Retrofit mRetrofit = new Retrofit.Builder()
                    .baseUrl(Url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mblogService = mRetrofit.create(blogService.class);

        }
        return mblogService;
    }
    public interface blogService
    {
        @GET
        Call<BlogPostList> getPostList(@Url String url);

    }
}
