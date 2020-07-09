package top.vergessen.blog.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * {@RestTemplate}配置类
 * @author Vergessen
 * @date 2020/7/8 20:18.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 配置 {@RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new RestTemplate(generateHttpRequestFactory());
    }

    /**
     * 禁用ssl证书校验
     */
    private HttpComponentsClientHttpRequestFactory generateHttpRequestFactory()
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(
                null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory connectionSocketFactory =
                new SSLConnectionSocketFactory(sslContext,
                new NoopHostnameVerifier());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        return factory;
    }
}
