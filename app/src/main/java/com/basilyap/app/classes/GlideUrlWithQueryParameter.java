package com.basilyap.app.classes;

import com.bumptech.glide.load.model.GlideUrl;

public class GlideUrlWithQueryParameter extends GlideUrl {
    private String mSourceUrl;

    public GlideUrlWithQueryParameter(String baseUrl, String key, String value) {
        super(buildUrl(baseUrl, key, value));

        mSourceUrl = baseUrl;
    }

    private static String buildUrl(String baseUrl, String key, String value) {
        StringBuilder stringBuilder = new StringBuilder(baseUrl);

        if (stringBuilder.toString().contains("?")) {
            stringBuilder.append("&");
        } else {
            stringBuilder.append("?");
        }

        stringBuilder.append(key);
        stringBuilder.append("=");
        stringBuilder.append(value);

        return stringBuilder.toString();
    }

    @Override
    public String getCacheKey() {
        return mSourceUrl;
    }

    @Override
    public String toString() {
        return super.getCacheKey();
    }
}
