
package cz.cvut.inbas.eccairs.model;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.7-b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ewaWebServer", targetNamespace = "http://tempuri.org/", wsdlLocation = "file:///home/kremep1/fel/projects/14tacr-inbas/impl/inbas-system/inbas-eccairs-webapi/src/main/resources/wsdl/webAPI.svc.single.wsdl")
public class EwaWebServer
    extends Service
{

    private final static URL EWAWEBSERVER_WSDL_LOCATION;
    private final static WebServiceException EWAWEBSERVER_EXCEPTION;
    private final static QName EWAWEBSERVER_QNAME = new QName("http://tempuri.org/", "ewaWebServer");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:///home/kremep1/fel/projects/14tacr-inbas/impl/inbas-system/inbas-eccairs-webapi/src/main/resources/wsdl/webAPI.svc.single.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        EWAWEBSERVER_WSDL_LOCATION = url;
        EWAWEBSERVER_EXCEPTION = e;
    }

    public EwaWebServer() {
        super(__getWsdlLocation(), EWAWEBSERVER_QNAME);
    }

    public EwaWebServer(WebServiceFeature... features) {
        super(__getWsdlLocation(), EWAWEBSERVER_QNAME, features);
    }

    public EwaWebServer(URL wsdlLocation) {
        super(wsdlLocation, EWAWEBSERVER_QNAME);
    }

    public EwaWebServer(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, EWAWEBSERVER_QNAME, features);
    }

    public EwaWebServer(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public EwaWebServer(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns EwaIWebServer
     */
    @WebEndpoint(name = "BasicHttpBinding_ewaIWebServer")
    public EwaIWebServer getBasicHttpBindingEwaIWebServer() {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_ewaIWebServer"), EwaIWebServer.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns EwaIWebServer
     */
    @WebEndpoint(name = "BasicHttpBinding_ewaIWebServer")
    public EwaIWebServer getBasicHttpBindingEwaIWebServer(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "BasicHttpBinding_ewaIWebServer"), EwaIWebServer.class, features);
    }

    private static URL __getWsdlLocation() {
        if (EWAWEBSERVER_EXCEPTION!= null) {
            throw EWAWEBSERVER_EXCEPTION;
        }
        return EWAWEBSERVER_WSDL_LOCATION;
    }

}