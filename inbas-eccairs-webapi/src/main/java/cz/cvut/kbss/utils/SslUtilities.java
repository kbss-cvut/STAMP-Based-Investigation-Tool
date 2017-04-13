package cz.cvut.kbss.utils;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * This class provide various static methods that relax X509 certificate and
 * hostname verification while using the SSL over the HTTP protocol.
 *
 * @author Francis Labrie
 */
public final class SslUtilities {

  /**
   * Hostname verifier for the Sun's deprecated API.
   *
   * @deprecated see {@link #_hostnameVerifier}.
   */
  private static HostnameVerifier __hostnameVerifier;
  /**
   * Thrust managers for the Sun's deprecated API.
   *
   * @deprecated see {@link #_trustManagers}.
   */
  private static TrustManager[] __trustManagers;
  /**
   * Hostname verifier.
   */
  private static HostnameVerifier _hostnameVerifier;
  /**
   * Thrust managers.
   */
  private static TrustManager[] _trustManagers;

  /**
   * Set the default Hostname Verifier to an instance of a fake class that
   * trust all hostnames. This method uses the old deprecated API from the
   * com.sun.ssl package.
   *
   * @deprecated see {@link #trustAllHostnamesSun()}.
   */
  private static void trustAllHostnamesSun_() {
    // Create a trust manager that does not validate certificate chains
    if (__hostnameVerifier == null) {
      __hostnameVerifier = new FakeHostnameVerifierSun();
    } // if
    // Install the all-trusting host name verifier
    HttpsURLConnection.setDefaultHostnameVerifier(__hostnameVerifier);
  } // trustAllHttpsCertificatesSun_

  /**
   * Set the default X509 Trust Manager to an instance of a fake class that
   * trust all certificates, even the self-signed ones. This method uses the
   * old deprecated API from the com.sun.ssl package.
   *
   * @deprecated see {@link #trustAllHttpsCertificatesSun()}.
   */
  private static void trustAllHttpsCertificatesSun_() {
    SSLContext context;

    // Create a trust manager that does not validate certificate chains
    if (__trustManagers == null) {
      __trustManagers = new TrustManager[] {new FakeX509TrustManagerSun()};
    } // if
    // Install the all-trusting trust manager
    try {
      context = SSLContext.getInstance("SSL");
      context.init(null, __trustManagers, new SecureRandom());
    } catch (GeneralSecurityException gse) {
      throw new IllegalStateException(gse.getMessage());
    } // catch
    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
  } // trustAllHttpsCertificatesSun_

  /**
   * Return true if the protocol handler property java.
   * protocol.handler.pkgs is set to the Sun's com.sun.net.ssl.
   * internal.www.protocol deprecated one, false
   * otherwise.
   *
   * @return true if the protocol handler property is set to the Sun's deprecated one, false
   *         otherwise.
   */
  private static boolean isDeprecatedSslProtocol() {
    return ("com.sun.net.ssl.internal.www.protocol"
        .equals(System.getProperty("java.protocol.handler.pkgs")));
  } // isDeprecatedSslProtocol

  /**
   * Set the default Hostname Verifier to an instance of a fake class that
   * trust all hostnames.
   */
  private static void trustAllHostnamesSun() {
    // Create a trust manager that does not validate certificate chains
    if (_hostnameVerifier == null) {
      _hostnameVerifier = new FakeHostnameVerifier();
    } // if
    // Install the all-trusting host name verifier:
    HttpsURLConnection.setDefaultHostnameVerifier(_hostnameVerifier);
  } // trustAllHttpsCertificatesSun

  /**
   * Set the default X509 Trust Manager to an instance of a fake class that
   * trust all certificates, even the self-signed ones.
   */
  private static void trustAllHttpsCertificatesSun() {
    SSLContext context;

    // Create a trust manager that does not validate certificate chains
    if (_trustManagers == null) {
      _trustManagers = new TrustManager[] {new FakeX509TrustManager()};
    } // if
    // Install the all-trusting trust manager:
    try {
      context = SSLContext.getInstance("SSL");
      context.init(null, _trustManagers, new SecureRandom());
    } catch (GeneralSecurityException gse) {
      throw new IllegalStateException(gse.getMessage());
    } // catch
    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
  } // trustAllHttpsCertificatesSun

  /**
   * Set the default Hostname Verifier to an instance of a fake class that
   * trust all hostnames.
   */
  public static void trustAllHostnames() {
    // Is the deprecated protocol setted?
    if (isDeprecatedSslProtocol()) {
      trustAllHostnamesSun_();
    } else {
      trustAllHostnamesSun();
    } // else
  } // trustAllHostnames

  /**
   * Set the default X509 Trust Manager to an instance of a fake class that
   * trust all certificates, even the self-signed ones.
   */
  public static void trustAllHttpsCertificates() {
    // Is the deprecated protocol setted?
    if (isDeprecatedSslProtocol()) {
      trustAllHttpsCertificatesSun_();
    } else {
      trustAllHttpsCertificatesSun();
    } // else
  } // trustAllHttpsCertificates

  /**
   * This class implements a fake hostname verificator, trusting any host
   * name. This class uses the old deprecated API from the com.sun.
   * ssl package.
   *
   * @author Francis Labrie
   * @deprecated see {@link SslUtilities.FakeHostnameVerifier}.
   */
  public static class FakeHostnameVerifierSun
      implements HostnameVerifier {

    /**
     * Always return true, indicating that the host name is an
     * acceptable match with the server's authentication scheme.
     *
     * @param hostname the host name.
     * @param session  the SSL session used on the connection to
     *                 host.
     * @return the true boolean value indicating the host name is trusted.
     */
    public boolean verify(String hostname, SSLSession session) {
      return (true);
    }
  } // FakeHostnameVerifierSun

  /**
   * This class allow any X509 certificates to be used to authenticate the
   * remote side of a secure socket, including self-signed certificates. This
   * class uses the old deprecated API from the com.sun.ssl
   * package.
   *
   * @author Francis Labrie
   * @deprecated see {@link SslUtilities.FakeX509TrustManager}.
   */
  public static class FakeX509TrustManagerSun
      implements X509TrustManager {

    /**
     * Empty array of certificate authority certificates.
     */
    private static final X509Certificate[] _AcceptedIssuers =
        new X509Certificate[] {};

    /**
     * Always return true, trusting for client SSL
     * chain peer certificate chain.
     *
     * @param chain the peer certificate chain.
     * @return the true boolean value indicating the chain is trusted.
     */
    public boolean isClientTrusted(X509Certificate[] chain) {
      return (true);
    } // checkClientTrusted

    /**
     * Always return true, trusting for server SSL
     * chain peer certificate chain.
     *
     * @param chain the peer certificate chain.
     * @return the true boolean value indicating the chain is trusted.
     */
    public boolean isServerTrusted(X509Certificate[] chain) {
      return (true);
    } // checkServerTrusted

    /**
     * Return an empty array of certificate authority certificates which
     * are trusted for authenticating peers.
     *
     * @return a empty array of issuer certificates.
     */
    public X509Certificate[] getAcceptedIssuers() {
      return (_AcceptedIssuers);
    } // getAcceptedIssuers

    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
        throws CertificateException {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
        throws CertificateException {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  } // FakeX509TrustManagerSun

  /**
   * This class implements a fake hostname verificator, trusting any host
   * name.
   *
   * @author Francis Labrie
   */
  public static class FakeHostnameVerifier implements HostnameVerifier {

    /**
     * Always return true, indicating that the host name is
     * an acceptable match with the server's authentication scheme.
     *
     * @param hostname the host name.
     * @param session  the SSL session used on the connection to
     *                 host.
     * @return the true boolean value indicating the host name is trusted.
     */
    public boolean verify(String hostname,
                          SSLSession session) {
      return (true);
    } // verify
  } // FakeHostnameVerifier

  /**
   * This class allow any X509 certificates to be used to authenticate the
   * remote side of a secure socket, including self-signed certificates.
   *
   * @author Francis Labrie
   */
  public static class FakeX509TrustManager implements X509TrustManager {

    /**
     * Empty array of certificate authority certificates.
     */
    private static final X509Certificate[] _AcceptedIssuers =
        new X509Certificate[] {};

    /**
     * Always trust for client SSL chain peer certificate
     * chain with any authType authentication types.
     *
     * @param chain    the peer certificate chain.
     * @param authType the authentication type based on the client
     *                 certificate.
     */
    public void checkClientTrusted(X509Certificate[] chain,
                                   String authType) {
    } // checkClientTrusted

    /**
     * Always trust for server SSL chain peer certificate
     * chain with any authType exchange algorithm types.
     *
     * @param chain    the peer certificate chain.
     * @param authType the key exchange algorithm used.
     */
    public void checkServerTrusted(X509Certificate[] chain,
                                   String authType) {
    } // checkServerTrusted

    /**
     * Return an empty array of certificate authority certificates which
     * are trusted for authenticating peers.
     *
     * @return a empty array of issuer certificates.
     */
    public X509Certificate[] getAcceptedIssuers() {
      return (_AcceptedIssuers);
    } // getAcceptedIssuers
  } // FakeX509TrustManager
} // SslUtilities

