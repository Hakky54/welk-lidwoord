package nl.altindag.welklidwoord.service;

import static org.apache.http.HttpHeaders.USER_AGENT;

import java.io.IOException;
import javax.annotation.PostConstruct;
import nl.altindag.welklidwoord.model.ProxyModel;
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

public abstract class AbstractService<T> {

    private HttpClient client;
    private HttpGet request;

    @PostConstruct
    private void init() {
        client = HttpClientBuilder.create().build();
    }

    public abstract T get(String s) throws Exception;

    HttpGet createRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", USER_AGENT);
        return request;
    }

    public void setProxy(ProxyModel proxyModel) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(proxyModel.getUsername(), proxyModel.getPassword());
        provider.setCredentials(AuthScope.ANY, credentials);

        client = HttpClientBuilder.create()
                .setProxy(new HttpHost(proxyModel.getHost(), proxyModel.getPort()))
                .setDefaultCredentialsProvider(provider)
                .build();
    }

    HttpResponse getResponse(HttpGet request) throws IOException {
        return client.execute(request);
    }

    String parseResponse(HttpResponse response) throws IOException {
        String parsedResponse = EntityUtils.toString(response.getEntity());

        // Release after finishing reading HTML
        request.releaseConnection();

        return parsedResponse;
    }

    public void setRequest(HttpGet request) {
        this.request = request;
    }

    public HttpGet getRequest() {
        return request;
    }

}
