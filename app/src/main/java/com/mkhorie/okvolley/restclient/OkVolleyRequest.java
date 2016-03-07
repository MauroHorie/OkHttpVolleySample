package com.mkhorie.okvolley.restclient;

import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mauro on 16-02-29.
 */
public abstract class OkVolleyRequest<T, R extends OkVolleyResponse<T>> extends Request<R> {

    private static final Map<String, String> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<String, String>());

    private Map<String, String> params;
    private Map<String, String> headers;
    private ResponseCallback<T, R> callback;
    private String contentBody;

    /**
     *
     * @param method the REST method. Use Request.Method.X
     * @param url the URL
     * @param headers the request headers
     * @param params the query params
     * @param contentBody the content body
     * @param callback
     */
    public OkVolleyRequest(int method, String url, Map<String, String> headers, Map<String, String> params, String contentBody, ResponseCallback<T, R> callback) {
        super(method, url, callback);
        this.params = params;
        this.headers = headers;
        this.contentBody = contentBody;
        this.callback = callback;
    }

    /**
     *
     * @param method
     * @param url
     * @param headers
     * @param params
     * @param contentBody
     * @param callback
     */
    public OkVolleyRequest(int method, String url, Map<String, String> headers, Map<String, String> params, Bitmap contentBody, ResponseCallback<T, R> callback) {
        super(method, url, callback);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        contentBody.compress(Bitmap.CompressFormat.PNG, 100, baos);
        this.contentBody = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        this.params = params;
        this.headers = headers;
        this.callback = callback;
    }

    /**
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    public byte[] getBody() throws AuthFailureError {
        return contentBody == null ? super.getBody() : contentBody.getBytes();
    }

    /**
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params == null ? EMPTY_MAP : params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers == null ? EMPTY_MAP : headers;
    }

    /**
     *
     * @param response
     */
    @Override
    protected void deliverResponse(R response) {
        if (callback != null) {
            callback.onResponse(response);
        }
    }

    /**
     *
     * @param response
     * @return
     */
    @Override
    protected abstract Response<R> parseNetworkResponse(NetworkResponse response);
}
