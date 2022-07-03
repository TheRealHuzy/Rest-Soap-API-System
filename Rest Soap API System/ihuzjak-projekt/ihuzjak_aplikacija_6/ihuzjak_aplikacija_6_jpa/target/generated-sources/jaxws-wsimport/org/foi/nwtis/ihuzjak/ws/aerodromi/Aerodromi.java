
package org.foi.nwtis.ihuzjak.ws.aerodromi;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 3.0.0
 * Generated source version: 3.0
 * 
 */
@WebServiceClient(name = "aerodromi", targetNamespace = "http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", wsdlLocation = "http://localhost:9090/ihuzjak_aplikacija_5/aerodromi?wsdl")
public class Aerodromi
    extends Service
{

    private final static URL AERODROMI_WSDL_LOCATION;
    private final static WebServiceException AERODROMI_EXCEPTION;
    private final static QName AERODROMI_QNAME = new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "aerodromi");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:9090/ihuzjak_aplikacija_5/aerodromi?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        AERODROMI_WSDL_LOCATION = url;
        AERODROMI_EXCEPTION = e;
    }

    public Aerodromi() {
        super(__getWsdlLocation(), AERODROMI_QNAME);
    }

    public Aerodromi(WebServiceFeature... features) {
        super(__getWsdlLocation(), AERODROMI_QNAME, features);
    }

    public Aerodromi(URL wsdlLocation) {
        super(wsdlLocation, AERODROMI_QNAME);
    }

    public Aerodromi(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, AERODROMI_QNAME, features);
    }

    public Aerodromi(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public Aerodromi(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns WsAerodromi
     */
    @WebEndpoint(name = "WsAerodromiPort")
    public WsAerodromi getWsAerodromiPort() {
        return super.getPort(new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "WsAerodromiPort"), WsAerodromi.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link jakarta.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WsAerodromi
     */
    @WebEndpoint(name = "WsAerodromiPort")
    public WsAerodromi getWsAerodromiPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "WsAerodromiPort"), WsAerodromi.class, features);
    }

    private static URL __getWsdlLocation() {
        if (AERODROMI_EXCEPTION!= null) {
            throw AERODROMI_EXCEPTION;
        }
        return AERODROMI_WSDL_LOCATION;
    }

}
