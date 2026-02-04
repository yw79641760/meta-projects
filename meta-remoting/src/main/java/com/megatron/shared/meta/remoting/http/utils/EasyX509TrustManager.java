package com.megatron.shared.meta.remoting.http.utils;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.swing.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * EasyX509TrustManager
 *
 * @author <a href="mailto:wei.yan@softmegatron.com">wei.yan</a>
 * @version 1.0.0
 * @since 5/4/20 3:22 PM
 */
public class EasyX509TrustManager implements X509TrustManager {

    private X509TrustManager standardTrustManager = null;

    public EasyX509TrustManager(KeyStore keyStore) throws NoSuchAlgorithmException,
                                                          KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keyStore);
        TrustManager[] trustManagers = factory.getTrustManagers();
        if (trustManagers.length == 0) {
            throw new NoSuchAlgorithmException("No such trust manager found.");
        }
        this.standardTrustManager = (X509TrustManager) trustManagers[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
        standardTrustManager.checkClientTrusted(x509Certificates, authType);
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
        if (x509Certificates != null && x509Certificates.length == 1) {
            x509Certificates[0].checkValidity();
        } else {
            standardTrustManager.checkServerTrusted(x509Certificates, authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.standardTrustManager.getAcceptedIssuers();
    }
}
