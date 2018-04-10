package nl.altindag.welklidwoord.service;

import nl.altindag.welklidwoord.model.Proxy;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static nl.altindag.welklidwoord.App.executor;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.HttpHeaders.USER_AGENT;

public abstract class AbstractService<T> {

    private HttpClient client;
    public static final Supplier<HttpClient> HTTP_CLIENT_SUPPLIER = () -> HttpClientBuilder.create().build();


    @PostConstruct
    private void init() {
        setClient(HTTP_CLIENT_SUPPLIER);
    }

    public abstract T get(String s) throws Exception;

    HttpGet createRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);
        return request;
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    public void setClient(Supplier<HttpClient> httpClientSupplier) {
        setClient(httpClientSupplier.get());
    }

    public void setClient(Proxy proxy) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setProxy(new HttpHost(proxy.getHost(), proxy.getPort()));

        if (isNotBlank(proxy.getUsername()) || isNotBlank(proxy.getPassword())) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword());
            provider.setCredentials(AuthScope.ANY, credentials);
            httpClientBuilder.setDefaultCredentialsProvider(provider);
        }

        client = httpClientBuilder.build();
    }

    Future<HttpResponse> getResponse(HttpGet request) throws IOException {
        return executor.submit(() -> client.execute(request));
    }

    String parseResponse(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }

}
