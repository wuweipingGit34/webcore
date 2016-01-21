//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.21 at 01:32:30 PM CST 
//


package org.yiwan.webcore.locator;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for locatorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="locatorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="WebElement"/>
 *     &lt;enumeration value="WebEdit"/>
 *     &lt;enumeration value="WebButton"/>
 *     &lt;enumeration value="WebCheckBox"/>
 *     &lt;enumeration value="WebRadioButton"/>
 *     &lt;enumeration value="WebFile"/>
 *     &lt;enumeration value="WebList"/>
 *     &lt;enumeration value="WebTable"/>
 *     &lt;enumeration value="WebTableRow"/>
 *     &lt;enumeration value="WebTableColumn"/>
 *     &lt;enumeration value="WebTableCell"/>
 *     &lt;enumeration value="Image"/>
 *     &lt;enumeration value="Frame"/>
 *     &lt;enumeration value="Link"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "locatorType")
@XmlEnum
public enum LocatorType {

    @XmlEnumValue("WebElement")
    WEB_ELEMENT("WebElement"),
    @XmlEnumValue("WebEdit")
    WEB_EDIT("WebEdit"),
    @XmlEnumValue("WebButton")
    WEB_BUTTON("WebButton"),
    @XmlEnumValue("WebCheckBox")
    WEB_CHECK_BOX("WebCheckBox"),
    @XmlEnumValue("WebRadioButton")
    WEB_RADIO_BUTTON("WebRadioButton"),
    @XmlEnumValue("WebFile")
    WEB_FILE("WebFile"),
    @XmlEnumValue("WebList")
    WEB_LIST("WebList"),
    @XmlEnumValue("WebTable")
    WEB_TABLE("WebTable"),
    @XmlEnumValue("WebTableRow")
    WEB_TABLE_ROW("WebTableRow"),
    @XmlEnumValue("WebTableColumn")
    WEB_TABLE_COLUMN("WebTableColumn"),
    @XmlEnumValue("WebTableCell")
    WEB_TABLE_CELL("WebTableCell"),
    @XmlEnumValue("Image")
    IMAGE("Image"),
    @XmlEnumValue("Frame")
    FRAME("Frame"),
    @XmlEnumValue("Link")
    LINK("Link");
    private final String value;

    LocatorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LocatorType fromValue(String v) {
        for (LocatorType c: LocatorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
