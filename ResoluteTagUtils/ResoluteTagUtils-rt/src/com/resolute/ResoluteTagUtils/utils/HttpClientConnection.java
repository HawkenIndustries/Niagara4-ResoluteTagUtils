package com.resolute.ResoluteTagUtils.utils;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.function.Consumer;

public class HttpClientConnection {
    private final HashMap<String, String> headers;
    private final URL urlTarget;
    private final String httpMethod;
    private final Integer connTimeout;
    private final Integer readTimeout;

    public static Builder builder () {
        return new Builder();
    }

    public static Builder builder (HttpClientConnection httpClientConnection) {
        return new Builder(httpClientConnection);
    }

    private HttpClientConnection(Builder builder) {
        this.headers = builder.headers;
        this.urlTarget = builder.urlTarget;
        this.httpMethod = builder.httpMethod;
        this.connTimeout = builder.connTimeout;
        this.readTimeout = builder.readTimeout;
    }

    public HashMap getHeaders() {
        return headers;
    }

    public URL getUrlTarget() {
        return urlTarget;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public Integer getConnTimeout() {
        return connTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public static class Builder {
        private HashMap headers;
        private URL urlTarget;
        private String httpMethod;
        private Integer connTimeout;
        private Integer readTimeout;

        private Builder() {}

        private Builder(HttpClientConnection httpClientConnection) {
            requireNonNull(httpClientConnection, "httpClient cannot be null");
            this.headers = httpClientConnection.headers;
            this.urlTarget = httpClientConnection.urlTarget;
            this.httpMethod = httpClientConnection.httpMethod;
            this.connTimeout = httpClientConnection.connTimeout;
            this.readTimeout = httpClientConnection.readTimeout;
        }

        public Builder with(Consumer<Builder> consumer) {
            requireNonNull(consumer, "consumer cannot be null");
            consumer.accept(this);
            return this;
        }

        public Builder withHeaders(HashMap<String, String> headers) {
            requireNonNull(headers, "headers cannot be null");
            this.headers = headers;
            return this;
        }

        public Builder withUrlTarget(URL urlTarget) {
            requireNonNull(urlTarget, "urlTarget cannot be null");
            this.urlTarget = urlTarget;
            return this;
        }

        public Builder withHttpMethod(String httpMethod) {
            requireNonNull(httpMethod, "httpMethod cannot be null");
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder withConnTimeout(int connTimeout) {
            this.connTimeout = connTimeout;
            return this;
        }

        public Builder withReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public HttpClientConnection build() {
            requireNonNull(headers, "headers cannot be null");
            requireNonNull(urlTarget, "urlTarget cannot be null");
            requireNonNull(httpMethod, "httpMethod cannot be null");
            if(readTimeout == null) readTimeout = 5000;
            if(connTimeout == null) connTimeout = 5000;
            return new HttpClientConnection(this);
        }
    }

    @SuppressWarnings("unchecked")
    public HttpResponse doGet(){
        HttpResponse response = new HttpResponse();
        try{
            HttpURLConnection conn =
                    (HttpURLConnection) getUrlTarget().openConnection();
            conn.setRequestMethod(getHttpMethod());
            conn.setReadTimeout(getReadTimeout());
            conn.setConnectTimeout(getConnTimeout());
            conn.setDoInput(true);
            getHeaders().forEach( (key, value) -> {
                conn.setRequestProperty((String)key, (String)value);
            });
            int status = conn.getResponseCode();
            if(status == 200){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                response.setStatus(status);
                response.setMsg(content.toString());
            }else{
                BufferedReader err = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while((inputLine = err.readLine()) != null){
                    content.append(inputLine);
                }
                err.close();
                response.setStatus(status);
                response.setMsg(content.toString());
            }
            conn.disconnect();
        }catch(MalformedURLException mue){
            mue.printStackTrace();
            return new HttpResponse(-1, mue.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            return new HttpResponse(-1, e.getMessage());
        }

        return response;
    }
}