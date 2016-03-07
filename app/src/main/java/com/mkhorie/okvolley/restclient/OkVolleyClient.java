package com.mkhorie.okvolley.restclient;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mauro on 16-02-29.
 */
public class OkVolleyClient {

    private static final String TAG = OkVolleyClient.class.getSimpleName();

    private RequestQueue requestQueue;
    private DefaultRetryPolicy defaultRetryPolicy;
    private Gson gson;
    private Uri defaultEnvironment;
    private final Map<String, String> defaultHeaders;

    protected OkVolleyClient(Context context, Gson gson) {
        this.requestQueue = Volley.newRequestQueue(context.getApplicationContext(), new OkHttp3Stack());
        this.defaultHeaders = new HashMap<>();
        this.gson = gson;
    }

    protected OkVolleyClient(RequestQueue requestQueue, Gson gson) {
        this.requestQueue = requestQueue;
        this.defaultHeaders = new HashMap<>();
        this.gson = gson;
    }

    /**
     *
     * @param defaultRetryPolicy
     */
    public void setRetryPolicy(DefaultRetryPolicy defaultRetryPolicy) {
        this.defaultRetryPolicy = defaultRetryPolicy;
    }

    public void setEnvironment(Uri defaultEnvironment) {
        this.defaultEnvironment = defaultEnvironment;
    }

    public void setHeaders(Map<String, String> defaultHeaders) {
        this.defaultHeaders.clear();
        this.defaultHeaders.putAll(defaultHeaders);
    }

    /**
     * @return the request queue.
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     *
     * @return
     */
    public DefaultRetryPolicy getDefaultRetryPolicy() {
        return defaultRetryPolicy;
    }

    public Uri getDefaultEnvironment() {
        return this.defaultEnvironment;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    /**
     * Adds a request to the Volley request queue with a given tag
     *
     * @param request is the request to be added
     * @param tag is the tag identifying the request
     */
    public void addToRequestQueue(OkVolleyRequest<?, ?> request, String tag) {
        request.setTag(tag == null || TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    /**
     * Adds a request to the Volley request queue
     *
     * @param request is the request to add to the Volley queue
     */
    public void addToRequestQueue(OkVolleyRequest<?, ?> request) {
        addToRequestQueue(request, null);
    }

    /**
     * Cancels all the request in the Volley queue for a given tag
     *
     * @param tag associated with the Volley requests to be cancelled
     */
    public void cancelAllRequests(String tag) {
        if (requestQueue!= null) {
            requestQueue.cancelAll(tag);
        }
    }
}
