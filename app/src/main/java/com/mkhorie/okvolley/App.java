package com.mkhorie.okvolley;

import android.app.Application;

import com.google.gson.Gson;
import com.mkhorie.okvolley.utils.GsonUtils;

/**
 * Created by Mauro on 16-02-29.
 */
public class App extends Application {

    private Gson gson;
    private FluidClient restClient;

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        gson = GsonUtils.getConfiguredGson();
        restClient = new FluidClient(this, getGson());
    }

    /**
     * @return the Gson instance.
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * @return the rest client.
     */
    public FluidClient getRestClient() {
        return restClient;
    }
}
