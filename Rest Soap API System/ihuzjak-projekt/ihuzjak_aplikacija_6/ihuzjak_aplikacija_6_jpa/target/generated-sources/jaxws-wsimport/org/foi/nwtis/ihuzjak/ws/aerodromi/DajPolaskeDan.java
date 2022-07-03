
package org.foi.nwtis.ihuzjak.ws.aerodromi;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dajPolaskeDan complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dajPolaskeDan"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="korisnik" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="icao" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="danOd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="danDo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dajPolaskeDan", propOrder = {
    "korisnik",
    "token",
    "icao",
    "danOd",
    "danDo"
})
public class DajPolaskeDan {

    protected String korisnik;
    protected String token;
    protected String icao;
    protected String danOd;
    protected String danDo;

    /**
     * Gets the value of the korisnik property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKorisnik() {
        return korisnik;
    }

    /**
     * Sets the value of the korisnik property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKorisnik(String value) {
        this.korisnik = value;
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the icao property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcao() {
        return icao;
    }

    /**
     * Sets the value of the icao property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcao(String value) {
        this.icao = value;
    }

    /**
     * Gets the value of the danOd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDanOd() {
        return danOd;
    }

    /**
     * Sets the value of the danOd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDanOd(String value) {
        this.danOd = value;
    }

    /**
     * Gets the value of the danDo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDanDo() {
        return danDo;
    }

    /**
     * Sets the value of the danDo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDanDo(String value) {
        this.danDo = value;
    }

}
