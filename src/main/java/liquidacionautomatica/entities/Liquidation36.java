/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author rbayarri
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Liquidation36 extends Liquidation {

  private String dependency;

  @Override
  public String getCompromisoNotFoundMessage() {
    return "No se encuentra el " + compromiso;
  }

  @Override
  public String getNotEnoughMoneyMessage(double available) {
    return "Saldo insuficiente en el "
            + compromiso.toString() + ". Disponible: " + available
            + ". A liquidar: " + totalAmount
            + ". Diferencia: " + (totalAmount - available);
  }

  @Override
  public String getErrorLiquitationMessage(String error) {
    return compromiso.toString() + " - " + error;
  }

  @Override
  public String getSuccessLiquidationMessage() {
    String result = String.format("%-15s", compromiso.toString());
    result += " - " + op.toString();
    return result;
  }

  public String toString() {
    return this.compromiso + "\nDependencia: " + this.dependency
            + "\nDescripcion: " + this.description
            + "\nImporte: " + this.totalAmount + "\nOP: " + this.op;
  }

}
