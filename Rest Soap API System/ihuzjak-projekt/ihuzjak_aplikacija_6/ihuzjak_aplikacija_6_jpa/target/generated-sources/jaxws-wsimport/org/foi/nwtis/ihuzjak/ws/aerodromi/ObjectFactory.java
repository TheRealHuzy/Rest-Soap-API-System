
package org.foi.nwtis.ihuzjak.ws.aerodromi;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.foi.nwtis.ihuzjak.ws.aerodromi package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DajPolaskeDan_QNAME = new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "dajPolaskeDan");
    private final static QName _DajPolaskeDanResponse_QNAME = new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "dajPolaskeDanResponse");
    private final static QName _DajPolaskeVrijeme_QNAME = new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "dajPolaskeVrijeme");
    private final static QName _DajPolaskeVrijemeResponse_QNAME = new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "dajPolaskeVrijemeResponse");
    private final static QName _DodajAerodromPreuzimanje_QNAME = new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "dodajAerodromPreuzimanje");
    private final static QName _DodajAerodromPreuzimanjeResponse_QNAME = new QName("http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", "dodajAerodromPreuzimanjeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.foi.nwtis.ihuzjak.ws.aerodromi
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DajPolaskeDan }
     * 
     */
    public DajPolaskeDan createDajPolaskeDan() {
        return new DajPolaskeDan();
    }

    /**
     * Create an instance of {@link DajPolaskeDanResponse }
     * 
     */
    public DajPolaskeDanResponse createDajPolaskeDanResponse() {
        return new DajPolaskeDanResponse();
    }

    /**
     * Create an instance of {@link DajPolaskeVrijeme }
     * 
     */
    public DajPolaskeVrijeme createDajPolaskeVrijeme() {
        return new DajPolaskeVrijeme();
    }

    /**
     * Create an instance of {@link DajPolaskeVrijemeResponse }
     * 
     */
    public DajPolaskeVrijemeResponse createDajPolaskeVrijemeResponse() {
        return new DajPolaskeVrijemeResponse();
    }

    /**
     * Create an instance of {@link DodajAerodromPreuzimanje }
     * 
     */
    public DodajAerodromPreuzimanje createDodajAerodromPreuzimanje() {
        return new DodajAerodromPreuzimanje();
    }

    /**
     * Create an instance of {@link DodajAerodromPreuzimanjeResponse }
     * 
     */
    public DodajAerodromPreuzimanjeResponse createDodajAerodromPreuzimanjeResponse() {
        return new DodajAerodromPreuzimanjeResponse();
    }

    /**
     * Create an instance of {@link AvionLeti }
     * 
     */
    public AvionLeti createAvionLeti() {
        return new AvionLeti();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DajPolaskeDan }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DajPolaskeDan }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", name = "dajPolaskeDan")
    public JAXBElement<DajPolaskeDan> createDajPolaskeDan(DajPolaskeDan value) {
        return new JAXBElement<DajPolaskeDan>(_DajPolaskeDan_QNAME, DajPolaskeDan.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DajPolaskeDanResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DajPolaskeDanResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", name = "dajPolaskeDanResponse")
    public JAXBElement<DajPolaskeDanResponse> createDajPolaskeDanResponse(DajPolaskeDanResponse value) {
        return new JAXBElement<DajPolaskeDanResponse>(_DajPolaskeDanResponse_QNAME, DajPolaskeDanResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DajPolaskeVrijeme }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DajPolaskeVrijeme }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", name = "dajPolaskeVrijeme")
    public JAXBElement<DajPolaskeVrijeme> createDajPolaskeVrijeme(DajPolaskeVrijeme value) {
        return new JAXBElement<DajPolaskeVrijeme>(_DajPolaskeVrijeme_QNAME, DajPolaskeVrijeme.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DajPolaskeVrijemeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DajPolaskeVrijemeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", name = "dajPolaskeVrijemeResponse")
    public JAXBElement<DajPolaskeVrijemeResponse> createDajPolaskeVrijemeResponse(DajPolaskeVrijemeResponse value) {
        return new JAXBElement<DajPolaskeVrijemeResponse>(_DajPolaskeVrijemeResponse_QNAME, DajPolaskeVrijemeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DodajAerodromPreuzimanje }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DodajAerodromPreuzimanje }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", name = "dodajAerodromPreuzimanje")
    public JAXBElement<DodajAerodromPreuzimanje> createDodajAerodromPreuzimanje(DodajAerodromPreuzimanje value) {
        return new JAXBElement<DodajAerodromPreuzimanje>(_DodajAerodromPreuzimanje_QNAME, DodajAerodromPreuzimanje.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DodajAerodromPreuzimanjeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DodajAerodromPreuzimanjeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://ws.aplikacija_5.ihuzjak.nwtis.foi.org/", name = "dodajAerodromPreuzimanjeResponse")
    public JAXBElement<DodajAerodromPreuzimanjeResponse> createDodajAerodromPreuzimanjeResponse(DodajAerodromPreuzimanjeResponse value) {
        return new JAXBElement<DodajAerodromPreuzimanjeResponse>(_DodajAerodromPreuzimanjeResponse_QNAME, DodajAerodromPreuzimanjeResponse.class, null, value);
    }

}
