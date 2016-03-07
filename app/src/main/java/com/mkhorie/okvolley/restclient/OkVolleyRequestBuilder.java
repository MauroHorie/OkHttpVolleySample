package com.mkhorie.okvolley.restclient;

import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.RetryPolicy;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mauro on 16-03-01.
 */
public abstract class OkVolleyRequestBuilder {

    private final String TAG = OkVolleyRequestBuilder.class.getSimpleName();
    protected OkVolleyClient okVolleyClient;
    protected final int method;
    protected final Map<String, String> headers;
    protected final Map<String, String> params;
    protected Uri environment;
    protected Uri url;
    protected String body;
    protected Class responseClass;

    public OkVolleyRequestBuilder(OkVolleyClient client, int method) {
        this.okVolleyClient = client;
        this.method = method;
        this.environment = client.getDefaultEnvironment();
        this.headers = new HashMap<>();
        this.params = new HashMap<>();

        this.headers.putAll(client.getDefaultHeaders());
    }

    public OkVolleyRequestBuilder headers(Map<String, String> headers) {
        if (headers == null) {
            return this;
        }
        this.headers.putAll(headers);
        return this;
    }

    public OkVolleyRequestBuilder params(Map<String, String> params) {
        if (params == null) {
            return this;
        }
        this.params.putAll(params);
        return this;
    }

    public OkVolleyRequestBuilder body(JSONObject body) {
        this.body = body.toString();
        return this;
    }

    public <T> OkVolleyRequestBuilder body(T entity) {
        this.body = okVolleyClient.getGson().toJson(entity);
        return this;
    }

    public OkVolleyRequestBuilder responseClass(Class responseClass) {
        this.responseClass = responseClass;
        return this;
    }

    protected OkVolleyRequestBuilder appendEntity(String resourcePath) {
        return appendEntity(resourcePath, null);
    }

    protected OkVolleyRequestBuilder appendEntity(String resourcePath, String id) {
        if (this.url == null) {
            this.url = Uri.parse(resourcePath);
        } else {
            this.url.buildUpon().appendPath(resourcePath).build();
        }
        if (!TextUtils.isEmpty(id)) {
            this.url.buildUpon().appendPath(id).build();
        }
        return this;
    }

    public <T> void callback(ResponseCallback<T, OkVolleyResponse<T>> callback) {
        callback(callback, null);
    }

    public <T> void callback(ResponseCallback<T, OkVolleyResponse<T>> callback, RetryPolicy retryPolicy) {
        OkVolleyRequest<T, OkVolleyResponse<T>> request = build(callback);
        if (retryPolicy != null) {
            request.setRetryPolicy(retryPolicy);
        }
        enqueue(request);
    }

    public void execute() {
        callback(null);
    }

    public OkVolleyClient getOkVolleyClient() {
        return okVolleyClient;
    }

    protected <T> void enqueue(OkVolleyRequest<T, OkVolleyResponse<T>> request) {
        okVolleyClient.addToRequestQueue(request, TAG);
    }

    protected abstract <T> OkVolleyRequest<T, OkVolleyResponse<T>> build(ResponseCallback<T, OkVolleyResponse<T>> callback);
}
