package com.mkhorie.okvolley.restclient;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * Encapsulates error information about a failed API request.
 *
 * Created by Mauro on 16-02-29.
 */
public class ResponseError {
    private static final String TAG = ResponseError.class.getSimpleName();

    private String message;
    private VolleyError error;

    public ResponseError() {
    }

    public ResponseError(String message) {
        this.message = message;
    }

    public ResponseError(String message, VolleyError error) {
        this.message = message;
        this.error = error;
    }

    public ResponseError(VolleyError error) {
        this.message = getMessageFromNetworkResponse(error.networkResponse);
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public VolleyError getError() {
        return this.error;
    }

    protected String getMessageFromNetworkResponse(NetworkResponse networkResponse) {
        if (networkResponse != null && networkResponse.data != null) {
            try {
                return new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Error decoding response data", e);
            }
        }
        return null;
    }
}