package utils;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HTTPUtils {

    private static SSLConnectionSocketFactory getSslConnectionSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException
    {
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true)
                .build();

        return new SSLConnectionSocketFactory(sslContext);
    }

    public static CloseableHttpClient getClientNoRedirect() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException
    {
        SSLConnectionSocketFactory sslsf = getSslConnectionSocketFactory();
        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setSSLSocketFactory(sslsf)
                .disableRedirectHandling()
                .setRedirectStrategy(new DefaultRedirectStrategy())
                .disableCookieManagement();

        return clientBuilder.build();
    }

    public static CloseableHttpClient getClientForLinkServiceRedirects() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException
    {
        SSLConnectionSocketFactory sslsf = getSslConnectionSocketFactory();
        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setSSLSocketFactory(sslsf)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .disableCookieManagement();

        return clientBuilder.build();
    }



    public static Boolean redirectSuccess(URI redirectLink, List<URI> redirectLinks, Integer responseCode)
    {
        int size = redirectLinks.size();
        if(!redirectLinks.get(size-1).equals(redirectLink))
            return false;
        else
            return !(responseCode != 301 && responseCode != 200);
    }

    public static Boolean accessSuccess(Integer responseCode)
    {
        return responseCode == 200;
    }
}
