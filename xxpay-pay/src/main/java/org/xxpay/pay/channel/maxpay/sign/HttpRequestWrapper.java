package org.xxpay.pay.channel.maxpay.sign;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class HttpRequestWrapper implements Request<HttpRequest> {

    private final HttpRequestBase originalRequest;
    private final byte[] body;
    private final String contentType;
    private final String contentLength;

    public HttpRequestWrapper(HttpRequestBase originalRequest)
            throws IllegalStateException, IOException {
        this.originalRequest = originalRequest;
        HttpEntity entity = null;
        if (originalRequest instanceof HttpEntityEnclosingRequest
                && (entity = ((HttpEntityEnclosingRequest) originalRequest)
                .getEntity()) != null) {
            body = IOUtils.toByteArray(entity.getContent());
            this.contentType = entity.getContentType() == null ? "" : entity
                    .getContentType().getValue();
            this.contentLength = String.valueOf(body.length);

            ByteArrayEntity newEntity = new ByteArrayEntity(body);
            newEntity.setContentType(entity.getContentType());
            ((HttpEntityEnclosingRequest) originalRequest).setEntity(newEntity);
        } else {
            body = new byte[0];
            contentType = "";
            contentLength = "";
        }
    }

    @Override
    public String getMethod() {
        return originalRequest.getRequestLine().getMethod().toLowerCase();
    }

    @Override
    public String getRequestUriPath(){
        URI uri = this.originalRequest.getURI();
        return StringUtils.isBlank(uri.getPath()) ? "" : uri.getPath();
    }

    @Override
    public String getRequestQueryString(){
        URI uri = this.originalRequest.getURI();
        return StringUtils.isBlank(uri.getQuery()) ? "" : uri.getQuery();
    }

    @Override
    public InputStream getRequestBody() {
        return new ByteArrayInputStream(body);
    }

    @Override
    public String getHeaderValue(String name) {
        Header header = this.originalRequest.getFirstHeader(name);
        if (header == null) {
            return "";
        } else {
            return header.getValue();
        }
    }
}
