package com.mkhorie.okvolley.restclient;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Mauro on 16-02-29.
 */
public abstract class ResponseCallback<T, R extends OkVolleyResponse<T>> implements Response.Listener<R>, Response.ErrorListener {
    public abstract void onSuccess(R response);
    public abstract void onFailure(ResponseError responseError);

    @Override
    public void onResponse(R response) {
        onSuccess(response);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        onFailure(new ResponseError(error));
    }
}