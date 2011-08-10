package wheelmap.org.domain.node.json;

import java.math.BigInteger;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonProperty;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@JsonAutoDetect
public class Meta {

	@JsonProperty( value = "num_pages" )
    protected BigInteger numPages;
	@JsonProperty( value = "item_count_total" )
    protected BigInteger itemCountTotal;
    protected BigInteger page;
    @JsonProperty( value = "item_count" )
    protected BigInteger itemCount;

    /**
     * Gets the value of the numPages property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumPages() {
        return numPages;
    }

    /**
     * Sets the value of the numPages property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumPages(BigInteger value) {
        this.numPages = value;
    }

    /**
     * Gets the value of the itemCountTotal property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getItemCountTotal() {
        return itemCountTotal;
    }

    /**
     * Sets the value of the itemCountTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setItemCountTotal(BigInteger value) {
        this.itemCountTotal = value;
    }

    /**
     * Gets the value of the page property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPage() {
        return page;
    }

    /**
     * Sets the value of the page property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPage(BigInteger value) {
        this.page = value;
    }

    /**
     * Gets the value of the itemCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getItemCount() {
        return itemCount;
    }

    /**
     * Sets the value of the itemCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setItemCount(BigInteger value) {
        this.itemCount = value;
    }

}
