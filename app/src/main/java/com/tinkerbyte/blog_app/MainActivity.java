package com.tinkerbyte.blog_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Boolean scroll_cond = false;
    int total_items, visible_items,scrolled_items;
    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle myDrawerToggle;
    NavigationView mNavigationView;
    RecyclerView postRecyclerView;
    LinearLayoutManager manager;
    postAdapter madapter;
    List<Item> items = new ArrayList<>();
    String mtoken = "";
    SpinKitView progressBar;
    String new_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar();
        progressBar = (SpinKitView)findViewById(R.id.spin_kit);
        manager = new LinearLayoutManager(this);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_menu);
        postRecyclerView = (RecyclerView)findViewById(R.id.postRecylerView);
        postRecyclerView.setLayoutManager(manager);
        madapter = new postAdapter(this, items);
        postRecyclerView.setAdapter(madapter);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home :
                        Toast.makeText(MainActivity.this,"Home Activity",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        //Toast.makeText(MainActivity.this,"Profile Activity",Toast.LENGTH_SHORT).show();
                        Intent proIntent = new Intent(MainActivity.this,ProfileActivity.class);
                        startActivity(proIntent);
                        break;
                    case R.id.settings:
                        Toast.makeText(MainActivity.this,"Setting Activity",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.aboutUs:
                        Toast.makeText(MainActivity.this,"About Us Activity",Toast.LENGTH_SHORT).show();
                        break;

                }
                return false;
            }
        });

        postRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    scroll_cond = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visible_items = manager.getChildCount();
                scrolled_items = manager.findFirstVisibleItemPosition();
                total_items = manager.getItemCount();
                if (scroll_cond&&(visible_items+scrolled_items == total_items))
                {
                    scroll_cond = false;
                    getData();
                }

            }
        });

 getData();
    }

    private void myToolbar()
    {
        drawer=(DrawerLayout)findViewById(R.id.drawer);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar, R.string.app_name, R.string.company);
        drawer.addDrawerListener(myDrawerToggle);
        myDrawerToggle.syncState();
    }
    private void getData()
    {

        new_url = GetPostApi.Url + "?key=" + GetPostApi.key;
        if(mtoken != ""){
            new_url = new_url+ "&pageToken="+ mtoken;
        }
        if(mtoken == null){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Call<BlogPostList> mBlogPostList = GetPostApi.getService().getPostList(new_url);
        mBlogPostList.enqueue(new Callback<BlogPostList>() {
            @Override
            public void onResponse(Call<BlogPostList> call, Response<BlogPostList> response) {
                BlogPostList list = response.body();
                mtoken = list.getNextPageToken();
                items.addAll(list.getItems());
                madapter.notifyDataSetChanged();

                //postRecyclerView.setAdapter(new postAdapter(MainActivity.this,list.getItems()));
               // Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<BlogPostList> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
