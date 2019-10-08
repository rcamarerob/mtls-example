package com.rcb.mtls.poc;

import lombok.Getter;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class SSLConfiguration {

    @Value("${client.ssl.trust-store-password}")
    private String trustStorePassword;
    @Value("${client.ssl.trust-store}")
    private Resource trustStore;
    @Value("${client.ssl.key-store-password}")
    private String keyStorePassword;
    @Value("${client.ssl.key-password}")
    private String keyPassword;
    @Value("${client.ssl.key-store}")
    private Resource keyStore;

    @Bean
    public RestTemplate restTemplateMTLS() {
        try{
            SSLContext sslcontext =
                SSLContexts.custom().loadTrustMaterial(trustStore.getFile(), trustStorePassword.toCharArray())
                    .loadKeyMaterial(keyStore.getFile(), keyStorePassword.toCharArray(),
                        keyPassword.toCharArray()).build();
            SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslcontext, new NoopHostnameVerifier());
            ClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build());

            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
            restTemplate.setErrorHandler(
                new DefaultResponseErrorHandler() {
                    @Override
                    protected boolean hasError(HttpStatus statusCode) {
                        return false;
                    }
                });

            return restTemplate;

        } catch (Exception e) {
            return new RestTemplateBuilder().build();
        }
    }
}
