/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import java.util.ArrayList;

/**
 *
 * @author rbayarri
 */
public class LiquidacionContrato extends Liquidacion {

  private String CUIT;
  private String situationAFIP;
  private String beneficiary;
  private ArrayList<Invoice> invoices;
  private double amountRetenciones;

  public LiquidacionContrato() {
    this.invoices = new ArrayList();
  }

  public String getCUIT() {
    return CUIT;
  }

  public void setCUIT(String CUIT) {
    this.CUIT = CUIT;
  }

  public String getSituationAFIP() {
    return situationAFIP;
  }

  public void setSituationAFIP(String situationAFIP) {
    this.situationAFIP = situationAFIP;
  }

  public String getBeneficiary() {
    return beneficiary;
  }

  public void setBeneficiary(String LastName) {
    this.beneficiary = LastName;
  }

  public ArrayList<Invoice> getInvoices() {
    return invoices;
  }

  public void setInvoices(ArrayList<Invoice> invoices) {
    this.invoices = invoices;
  }

  public double getAmountRetenciones() {
    return amountRetenciones;
  }

  public void setAmountRetenciones(double amountRetenciones) {
    this.amountRetenciones = amountRetenciones;
  }

  @Override
  public String toString() {
    return "CUIT: " + this.CUIT + "\nApellido: " + this.beneficiary
            + "\nSituación AFIP: " + this.situationAFIP + "\nDescripción: " + this.description
            + "\nCompromiso: " + this.compromiso + "\nMonto total: " + this.totalAmount
            + "\nComprobantes: \n" + toStringInvoices() + "\nOP: " + this.op
            + "\nResultado liquidación: " + this.resultLiquidacion
            + ((this.resultAutorizacion != null) ? "\nResultado Autorización: " + this.resultAutorizacion : "");
  }

  public String toStringInvoices() {
    String message = "";
    for (int i = 0; i < this.invoices.size(); i++) {
      message += this.invoices.get(i).toString() + "\n";
    }
    return message;
  }

  public void addInvoice(Invoice invoice) {
    invoices.add(invoice);
    totalAmount += Double.parseDouble(invoice.getAmount());
  }

}
