/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DELL
 */

public class CustomerInvoiceGridModel extends AbstractTableModel {

    private List<InvoiceCustomer> invoicesList;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    public CustomerInvoiceGridModel(List<InvoiceCustomer> invoicesList) {
        this.invoicesList = invoicesList;
    }

    public List<InvoiceCustomer> getInvoicesList() {
        return invoicesList;
    }
    
    
    @Override
    public int getRowCount() {
        return invoicesList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Invoice Num";
            case 1:
                return "Customer Name";
            case 2:
                return "Invoice Date";
            case 3:
                return "Invoice Total";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Double.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceCustomer row = invoicesList.get(rowIndex);
        
        switch (columnIndex) {
            case 0:
                return row.getInvNum();
            case 1:
                return row.getCustomerName();
            case 2:
                return df.format(row.getInvDate());
            case 3:
                return row.getInvTotal();
            default:
                return "";
        }
        
    }
    
}
