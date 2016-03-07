package com.mkhorie.okvolley;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mkhorie.okvolley.models.User;
import com.mkhorie.okvolley.restclient.OkVolleyResponse;
import com.mkhorie.okvolley.restclient.ResponseError;
import com.mkhorie.okvolley.restclient.ResponseCallback;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;
    private UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        adapter = new UserListAdapter();
        recyclerView.setAdapter(adapter);

        getApp().getRestClient().get().users().callback(new UsersCallback(this));
        // getApp().getRestClient().get().users(1).callback(...);
        // getApp().getRestClient().post().users().body(user).callback(...);
        // getApp().getRestClient().get().users().params(map).headers(map2).callback(...);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private App getApp() {
        return (App) getApplicationContext();
    }

    private static class UsersCallback extends ResponseCallback<User[], OkVolleyResponse<User[]>> {
        private WeakReference<MainActivity> parentRef;

        public UsersCallback(MainActivity parent) {
            parentRef = new WeakReference<>(parent);
        }

        @Override
        public void onSuccess(OkVolleyResponse<User[]> result) {
            MainActivity parent = parentRef.get();
            if (parent != null) {
                User[] users = result.getData();
                parent.adapter.setData(Arrays.asList(users));
            }
        }

        @Override
        public void onFailure(ResponseError error) {
            MainActivity parent = parentRef.get();
            if (parent != null) {
                Snackbar.make(parent.coordinatorLayout, "Error fetching data from server", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
