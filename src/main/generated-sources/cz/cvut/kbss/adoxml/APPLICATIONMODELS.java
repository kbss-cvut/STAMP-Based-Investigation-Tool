//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.07.25 at 06:09:39 PM CEST 
//


package cz.cvut.kbss.adoxml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "applicationmodel"
})
@XmlRootElement(name = "APPLICATIONMODELS")
public class APPLICATIONMODELS {

    @XmlElement(name = "APPLICATIONMODEL", required = true)
    protected List<APPLICATIONMODEL> applicationmodel;

    /**
     * Gets the value of the applicationmodel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the applicationmodel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAPPLICATIONMODEL().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link APPLICATIONMODEL }
     * 
     * 
     */
    public List<APPLICATIONMODEL> getAPPLICATIONMODEL() {
        if (applicationmodel == null) {
            applicationmodel = new ArrayList<APPLICATIONMODEL>();
        }
        return this.applicationmodel;
    }

}
