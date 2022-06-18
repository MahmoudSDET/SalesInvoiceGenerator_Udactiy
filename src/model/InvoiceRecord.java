
package model;

public class InvoiceRecord {
    private String itemName;
    private double itemPrice;
    private int itemCount;
    private InvoiceCustomer header;

    public InvoiceRecord(String itemName, double itemPrice, int itemCount, InvoiceCustomer header) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCount = itemCount;
        this.header = header;
    }
    
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public InvoiceCustomer getHeader() {
        return header;
    }

    public void setHeader(InvoiceCustomer header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" + "itemName=" + itemName + ", itemprice=" + itemPrice + ", itemCount=" + itemCount + '}';
    }
    
    public double getLineTotal() {
        return itemCount * itemPrice;
    }
    
    public String getDataAsCSV() {
        return "" + getHeader().getInvNum() + "," + getItemName() + "," + getItemPrice() + "," + getItemCount();
    }
}
