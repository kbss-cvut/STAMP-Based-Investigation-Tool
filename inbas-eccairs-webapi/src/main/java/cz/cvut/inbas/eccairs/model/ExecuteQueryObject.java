
package cz.cvut.inbas.eccairs.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="queryObject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="options" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="pageSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="batchSize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userToken",
    "queryObject",
    "options",
    "pageSize",
    "batchSize"
})
@XmlRootElement(name = "ExecuteQueryObject")
public class ExecuteQueryObject {

    @XmlElementRef(name = "userToken", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> userToken;
    @XmlElementRef(name = "queryObject", namespace = "http://tempuri.org/", type = JAXBElement.class, required = false)
    protected JAXBElement<String> queryObject;
    protected Integer options;
    protected Integer pageSize;
    protected Integer batchSize;

    /**
     * Gets the value of the userToken property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUserToken() {
        return userToken;
    }

    /**
     * Sets the value of the userToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUserToken(JAXBElement<String> value) {
        this.userToken = value;
    }

    /**
     * Gets the value of the queryObject property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getQueryObject() {
        return queryObject;
    }

    /**
     * Sets the value of the queryObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setQueryObject(JAXBElement<String> value) {
        this.queryObject = value;
    }

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOptions(Integer value) {
        this.options = value;
    }

    /**
     * Gets the value of the pageSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPageSize(Integer value) {
        this.pageSize = value;
    }

    /**
     * Gets the value of the batchSize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBatchSize() {
        return batchSize;
    }

    /**
     * Sets the value of the batchSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBatchSize(Integer value) {
        this.batchSize = value;
    }

}
