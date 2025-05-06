/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import liquidacionautomatica.Utils;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rbayarri
 */

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LiquidationContrato extends Liquidation {

  private String CUIT;
  private String situationAFIP;
  private String beneficiary;
  private List<Invoice> invoices = new ArrayList<>();
  private double amountRetenciones;

  @Override
  public String getMessage() {

    StringBuilder message = new StringBuilder();

    if (this.getOp() == null) {
      message.append("\n");
      message.append(Utils.toCurrencyFormat(totalAmount));
      message.append(" - ");
      message.append(String.format("%-50s", beneficiary));
    }
    return message.toString();
  }

  @Override
  public String getCompromisoNotFoundMessage() {
    return beneficiary + " - No se encuentra el " + compromiso.toString();
  }

  @Override
  public String getNotEnoughMoneyMessage(double available) {
    return beneficiary + " - Saldo insuficiente en el "
            + compromiso.toString() + ". Disponible: " + available
            + ". A liquidar: " + totalAmount
            + ". Diferencia: " + (totalAmount - available);
  }

  @Override
  public String getErrorLiquitationMessage(String error) {
    return beneficiary + " - " + error;
  }

  @Override
  public String getSuccessLiquidationMessage() {
    String result = String.format("%-15s", op.toString());
    result += " - " + beneficiary;
    return result;
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
