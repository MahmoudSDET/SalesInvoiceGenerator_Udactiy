/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.InvoiceCustomer;
import model.CustomerInvoiceGridModel;
import model.InvoiceRecord;
import model.InvoiceRecordsGridModel;
import views.InvoiceFrame;
import views.InvoiceCustomerDialog;
import views.InvoiceRecordDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author DELL
 */
public class ProductsInvoiceListener implements ActionListener, ListSelectionListener  {
    private InvoiceFrame frame;
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    public ProductsInvoiceListener(InvoiceFrame frame) {
        this.frame = frame;
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "CreateNewInvoice":
                displayNewInvoiceDialog();
                break;
            case "DeleteInvoice":
                deleteInvoice();
                break;
            case "CreateNewLine":
                displayNewLineDialog();
                break;
            case "DeleteLine":
                deleteLine();
                break;
            case "LoadFile":
                loadFile();
                break;
            case "SaveFile":
                saveData();
                break;
            case "createInvCancel":
                createInvCancel();
                break;
            case "createInvOK":
                createInvOK();
                break;
            case "createLineCancel":
                createInvoiceCancel();
                break;
            case "createLineOK":
                createLineOK();
                break;
        }
    }

    
    private void loadFile() {
        JOptionPane.showMessageDialog(frame, "Please, select invoices List file!", "Attension", JOptionPane.WARNING_MESSAGE);
        JFileChooser openFile = new JFileChooser();
        int result = openFile.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = openFile.getSelectedFile();
            try {
                FileReader headerFr = new FileReader(headerFile);
                BufferedReader headerBr = new BufferedReader(headerFr);
                String headerLine = null;

                while ((headerLine = headerBr.readLine()) != null) {
                    String[] headerParts = headerLine.split(",");
                    String invNumStr = headerParts[0];
                    String invDateStr = headerParts[1];
                    String custName = headerParts[2];

                    int invNum = Integer.parseInt(invNumStr);
                    Date invDate = df.parse(invDateStr);

                    InvoiceCustomer inv = new InvoiceCustomer(invNum, custName, invDate);
                    frame.getInvoicesListing().add(inv);
                }

                JOptionPane.showMessageDialog(frame, "Please, select Item List Info file!", "Attension", JOptionPane.WARNING_MESSAGE);
                result = openFile.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = openFile.getSelectedFile();
                    BufferedReader linesBr = new BufferedReader(new FileReader(linesFile));
                    String linesLine = null;
                    while ((linesLine = linesBr.readLine()) != null) {
                        String[] lineParts = linesLine.split(",");
                        String invNumStr = lineParts[0];
                        String itemName = lineParts[1];
                        String itemPriceStr = lineParts[2];
                        String itemCountStr = lineParts[3];

                        int invNum = Integer.parseInt(invNumStr);
                        double itemPrice = Double.parseDouble(itemPriceStr);
                        int itemCount = Integer.parseInt(itemCountStr);
                        InvoiceCustomer header = findInvoiceByNum(invNum);
                        InvoiceRecord invLine = new InvoiceRecord(itemName, itemPrice, itemCount, header);
                        header.getLines().add(invLine);
                    }
                   
                    frame.setcustomerInvoiceTable(new CustomerInvoiceGridModel(frame.getInvoicesListing()));
                   //frame.getInvoicesTable.setModel(frame.getInvoiceHeaderTableModel());
                    frame.getInvoiceGrid().setModel(frame.getCustomerInvoiceGridModel());
                   
                    frame.getInvoiceGrid().validate();
                }
                System.out.println("Check");
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Date Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Number Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "File Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Read Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        displayInvoices();
    }
    public void defultinvoicesGrid() {
        
                   
                    frame.setcustomerInvoiceTable(new CustomerInvoiceGridModel(frame.getInvoicesListing()));
                   //frame.getInvoicesTable.setModel(frame.getInvoiceHeaderTableModel());
                    frame.getInvoiceGrid().setModel(frame.getCustomerInvoiceGridModel());
                   
                    frame.getInvoiceGrid().validate();
               
                    displayInvoices();
    }

    private void saveData() {
        String headers = "";
        String lines = "";
        for (InvoiceCustomer header : frame.getInvoicesListing()) {
            headers += header.getDataAsCSV();
            headers += "\n";
            for (InvoiceRecord line : header.getLines()) {
                lines += line.getDataAsCSV();
                lines += "\n";
            }
        }
        JOptionPane.showMessageDialog(frame, "Please, select file to save header data!", "Attension", JOptionPane.WARNING_MESSAGE);
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = fileChooser.getSelectedFile();
            try {
                FileWriter hFW = new FileWriter(headerFile);
                hFW.write(headers);
                hFW.flush();
                hFW.close();

                JOptionPane.showMessageDialog(frame, "Please, select file to save lines data!", "Attension", JOptionPane.WARNING_MESSAGE);
                result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = fileChooser.getSelectedFile();
                    FileWriter lFW = new FileWriter(linesFile);
                    lFW.write(lines);
                    lFW.flush();
                    lFW.close();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        JOptionPane.showMessageDialog(frame, "Data saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

    }

    private InvoiceCustomer findInvoiceByNum(int invNum) {
        InvoiceCustomer header = null;
        for (InvoiceCustomer inv : frame.getInvoicesListing()) {
            if (invNum == inv.getInvNum()) {
                header = inv;
                break;
            }
        }
        return header;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Invoice Selected!");
        invoicesTableRowSelected();
    }

    private void invoicesTableRowSelected() {
        int selectedRowIndex = frame.getInvoiceGrid().getSelectedRow();
        if (selectedRowIndex >= 0) {
            InvoiceCustomer row = frame.getCustomerInvoiceGridModel().getInvoicesList().get(selectedRowIndex);
            frame.getCustNameTF().setText(row.getCustomerName());
            frame.getInvDateTF().setText(df.format(row.getInvDate()));
            frame.getInvNumLbl().setText("" + row.getInvNum());
            frame.getInvTotalLbl().setText("" + row.getInvTotal());
            ArrayList<InvoiceRecord> lines = row.getLines();
            frame.setinvoiceRecordsGridModel(new InvoiceRecordsGridModel(lines));
            frame.getInvLinesTable().setModel(frame.getInvoiceLinesTableModel());
            frame.getInvoiceLinesTableModel().fireTableDataChanged();
        }
    }

    private void displayNewInvoiceDialog() {
        frame.setCustomerInvoiceDialog(new InvoiceCustomerDialog(frame));
        frame.getCustomerDialog().setVisible(true);
    }

    private void displayNewLineDialog() {
        frame.setInvoiceDialog(new InvoiceRecordDialog(frame));
        frame.getInvoiceDialog().setVisible(true);
    }

    private void createInvCancel() {
        frame.getCustomerDialog().setVisible(false);
        frame.getCustomerDialog().dispose();
        frame.setCustomerInvoiceDialog(null);
    }

    private void createInvOK() {
        
        
        String custName = frame.getCustomerDialog().getCustNameField().getText();
        
        
        
       
        String invDateStr = frame.getCustomerDialog().getInvDateField().getText();
        frame.getCustomerDialog().setVisible(false);
        frame.getCustomerDialog().dispose();
        frame.setCustomerInvoiceDialog(null);
        try {
            Date invDate = df.parse(invDateStr);
            int invNum = getNextInvoiceNumber();
            InvoiceCustomer invoiceHeader = new InvoiceCustomer(invNum, custName, invDate);
            
           if(custName.equals("") || custName.isEmpty()){
        
            JOptionPane.showMessageDialog(frame, "Please Enter Customer Name", "Warning", JOptionPane.WARNING_MESSAGE);
           
        
        
        }else{
            
            frame.getInvoicesListing().add(invoiceHeader);
            frame.getCustomerInvoiceGridModel().fireTableDataChanged();
            
           }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        displayInvoices();
    }

    private int getNextInvoiceNumber() {
        int max = 0;
        for (InvoiceCustomer header : frame.getInvoicesListing()) {
            if (header.getInvNum() > max) {
                max = header.getInvNum();
            }
        }
        return max + 1;
    }

    private void createInvoiceCancel() {
        frame.getInvoiceDialog().setVisible(false);
        frame.getInvoiceDialog().dispose();
        frame.setInvoiceDialog(null);
    }

    private void createLineOK() {
        
        
        try{
        String itemName = frame.getInvoiceDialog().getItemNameField().getText();
        String itemCountStr = frame.getInvoiceDialog().getItemCountField().getText();
        String itemPriceStr = frame.getInvoiceDialog().getItemPriceField().getText();
        frame.getInvoiceDialog().setVisible(false);
        frame.getInvoiceDialog().dispose();
        frame.setInvoiceDialog(null);
        int itemCount = Integer.parseInt(itemCountStr);
        double itemPrice = Double.parseDouble(itemPriceStr);
        int headerIndex = frame.getInvoiceGrid().getSelectedRow();
        InvoiceCustomer invoice = frame.getCustomerInvoiceGridModel().getInvoicesList().get(headerIndex);

        InvoiceRecord invoiceLine = new InvoiceRecord(itemName, itemPrice, itemCount, invoice);
        invoice.addInvLine(invoiceLine);
        frame.getInvoiceLinesTableModel().fireTableDataChanged();
        frame.getCustomerInvoiceGridModel().fireTableDataChanged();
        frame.getInvTotalLbl().setText("" + invoice.getInvTotal());
        
        }catch(Exception ex){
            
         JOptionPane.showMessageDialog(frame, "please select customer invoice to add the item", "Warning", JOptionPane.WARNING_MESSAGE);
         ex.printStackTrace();
        
        }
        displayInvoices();
    }

    private void deleteInvoice() {
        
        
        try{
        
        int invIndex = frame.getInvoiceGrid().getSelectedRow();
        InvoiceCustomer header = frame.getCustomerInvoiceGridModel().getInvoicesList().get(invIndex);
        frame.getCustomerInvoiceGridModel().getInvoicesList().remove(invIndex);
        frame.getCustomerInvoiceGridModel().fireTableDataChanged();
        frame.setinvoiceRecordsGridModel(new InvoiceRecordsGridModel(new ArrayList<InvoiceRecord>()));
        frame.getInvLinesTable().setModel(frame.getInvoiceLinesTableModel());
        frame.getInvoiceLinesTableModel().fireTableDataChanged();
        frame.getCustNameTF().setText("");
        frame.getInvDateTF().setText("");
        frame.getInvNumLbl().setText("");
        frame.getInvTotalLbl().setText("");
        displayInvoices();
        
        }catch(Exception ex){
            
            JOptionPane.showMessageDialog(frame, "please select the customer invoice which added to delete it", "Warning", JOptionPane.WARNING_MESSAGE);
             ex.printStackTrace();
        
        }
        
    }

    private void deleteLine() {
        
        
  try{
        int lineIndex = frame.getInvLinesTable().getSelectedRow();
        InvoiceRecord line = frame.getInvoiceLinesTableModel().getInvoiceLines().get(lineIndex);
        frame.getInvoiceLinesTableModel().getInvoiceLines().remove(lineIndex);
        frame.getInvoiceLinesTableModel().fireTableDataChanged();
        frame.getCustomerInvoiceGridModel().fireTableDataChanged();
        frame.getInvTotalLbl().setText("" + line.getHeader().getInvTotal());
       
  } catch (Exception ex){
        
        JOptionPane.showMessageDialog(frame, "please select the item which added to delete it", "Warning", JOptionPane.WARNING_MESSAGE);
         ex.printStackTrace();
        }
  
   displayInvoices();
    }

    private void displayInvoices() {
        System.out.println("***************************");
        for (InvoiceCustomer header : frame.getInvoicesListing()) {
            System.out.println(header);
        }
        System.out.println("***************************");
    }
    
}
