package com.mkhorie.okvolley;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mkhorie.okvolley.models.User;
import com.mkhorie.okvolley.restclient.OkVolleyClient;
import com.mkhorie.okvolley.restclient.OkVolleyRequest;
import com.mkhorie.okvolley.restclient.OkVolleyRequestBuilder;
import com.mkhorie.okvolley.restclient.OkVolleyResponse;
import com.mkhorie.okvolley.restclient.ResponseCallback;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Mauro on 16-02-29.
 */
public class FluidClient extends OkVolleyClient {

    private static final String TAG = FluidClient.class.getSimpleName();
    private static final String ENVIRONMENT = "http://jsonplaceholder.typicode.com";
    private static final String USERS = "users";

    private GetRequestBuilder getRequestBuilder;
    private PostRequestBuilder postRequestBuilder;
    private PutRequestBuilder putRequestBuilder;
    private PatchRequestBuilder patchRequestBuilder;
    private DeleteRequestBuilder deleteRequestBuilder;

    public FluidClient(Context context, Gson gson) {
        super(context, gson);
        this.withEnvironment(Uri.parse(ENVIRONMENT));
        this.withGetRequestBuilder(new FluidClient.GetRequestBuilder(this));
        this.withPostRequestBuilder(new FluidClient.PostRequestBuilder(this));
        this.withPutRequestBuilder(new FluidClient.PutRequestBuilder(this));
        this.withPatchRequestBuilder(new FluidClient.PatchRequestBuilder(this));
        this.withDeleteRequestBuilder(new FluidClient.DeleteRequestBuilder(this));
    }

    public FluidClient withRetryPolicy(DefaultRetryPolicy defaultRetryPolicy) {
        this.setRetryPolicy(defaultRetryPolicy);
        return this;
    }

    public FluidClient withEnvironment(Uri defaultEnvironment) {
        this.setEnvironment(defaultEnvironment);
        return this;
    }

    public FluidClient withHeaders(Map<String, String> defaultHeaders) {
        this.setHeaders(defaultHeaders);
        return this;
    }

    public FluidClient withGetRequestBuilder(GetRequestBuilder builder) {
        this.getRequestBuilder = builder;
        return this;
    }

    public FluidClient withPostRequestBuilder(PostRequestBuilder builder) {
        this.postRequestBuilder = builder;
        return this;
    }

    public FluidClient withPutRequestBuilder(PutRequestBuilder builder) {
        this.putRequestBuilder = builder;
        return this;
    }

    public FluidClient withPatchRequestBuilder(PatchRequestBuilder builder) {
        this.patchRequestBuilder = builder;
        return this;
    }

    public FluidClient withDeleteRequestBuilder(DeleteRequestBuilder builder) {
        this.deleteRequestBuilder = builder;
        return this;
    }

    public GetRequestBuilder get() {
        return getRequestBuilder;
    }

    public PostRequestBuilder post() {
        return postRequestBuilder;
    }

    public PutRequestBuilder put() {
        return putRequestBuilder;
    }

    public PatchRequestBuilder patch() {
        return patchRequestBuilder;
    }

    public DeleteRequestBuilder delete() {
        return deleteRequestBuilder;
    }

    public static abstract class BaseRequestBuilder extends OkVolleyRequestBuilder {
        public BaseRequestBuilder(OkVolleyClient client, int method) {
            super(client, method);
        }

        public OkVolleyRequestBuilder user(String id) {
            this.responseClass = User[].class;
            return appendEntity(USERS, id);
        }

        public OkVolleyRequestBuilder users() {
            this.responseClass = User[].class;
            return appendEntity(USERS);
        }

        @Override
        protected <T> OkVolleyRequest<T, OkVolleyResponse<T>> build(ResponseCallback<T, OkVolleyResponse<T>> callback) {
            if (environment == null || url == null || responseClass == null) {
                throw new IllegalArgumentException("");
            }

            OkVolleyClient client = getOkVolleyClient();
            Gson gson = client.getGson();
            String resourceUrl = environment.buildUpon().appendPath(url.toString()).build().toString();

            return new AppRestRequest<T>(gson, method, resourceUrl, headers, params, body, responseClass, callback);
        }
    }

    public static class GetRequestBuilder extends BaseRequestBuilder {
        public GetRequestBuilder(OkVolleyClient client) {
            super(client, Request.Method.GET);
        }
    }

    public static class PostRequestBuilder extends BaseRequestBuilder {
        public PostRequestBuilder(OkVolleyClient client) {
            super(client, Request.Method.POST);
        }
    }

    public static class PutRequestBuilder extends BaseRequestBuilder {
        public PutRequestBuilder(OkVolleyClient client) {
            super(client, Request.Method.PUT);
        }
    }

    public static class PatchRequestBuilder extends BaseRequestBuilder {
        public PatchRequestBuilder(OkVolleyClient client) {
            super(client, Request.Method.PATCH);
        }
    }

    public static class DeleteRequestBuilder extends BaseRequestBuilder {
        public DeleteRequestBuilder(OkVolleyClient client) {
            super(client, Request.Method.DELETE);
        }
    }

    public static class AppRestRequest<T> extends OkVolleyRequest<T, OkVolleyResponse<T>> {

        private Class responseClass;
        private Gson gson;

        public AppRestRequest(Gson gson, int method, String url, Map<String, String> headers, Map<String, String> params, String contentBody, Class responseClass, ResponseCallback<T, OkVolleyResponse<T>> listener) {
            super(method, url, headers, params, contentBody, listener);
            this.gson = gson;
            this.responseClass = responseClass;
        }

        public AppRestRequest(Gson gson, int method, String url, Map<String, String> headers, Map<String, String> params, Bitmap contentBody, Class responseClass, ResponseCallback<T, OkVolleyResponse<T>> listener) {
            super(method, url, headers, params, contentBody, listener);
            this.gson = gson;
            this.responseClass = responseClass;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Response<OkVolleyResponse<T>> parseNetworkResponse(NetworkResponse response) {
            OkVolleyResponse<T> result;
            try {
                String responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                if (responseString.isEmpty()) {
                    result = new OkVolleyResponse<>();
                } else if (responseClass == String.class) {
                    result = new OkVolleyResponse<>((T) responseString);
                } else {
                    T data = (T) gson.fromJson(responseString, responseClass);
                    result = new OkVolleyResponse<>(data);
                }
                return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));

            } catch (UnsupportedEncodingException | JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }
}
