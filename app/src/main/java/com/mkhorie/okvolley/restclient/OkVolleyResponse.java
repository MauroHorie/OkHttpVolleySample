package com.mkhorie.okvolley.restclient;

import com.android.volley.NetworkResponse;

/**
 * This base response class only binds the returned model to the T type. Most API responses contain
 * more data such as pagination, links, etc. Subclass as needed and ensure that your subclass to
 * {@link OkVolleyRequest} overrides {@link OkVolleyRequest#parseNetworkResponse(NetworkResponse)}
 * and parses the {@link NetworkResponse} into your response definition.
 *
 * Created by Mauro on 16-02-29.
 */
public class OkVolleyResponse<T> {

    private T data;

    public OkVolleyResponse() {}

    public OkVolleyResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
