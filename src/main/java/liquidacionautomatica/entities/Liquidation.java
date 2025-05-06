/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rbayarri
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Liquidation {

  protected Compromiso compromiso;
  protected String description;
  protected double totalAmount;
  protected OP op;
  protected String resultLiquidacion;
  protected String resultAutorizacion;
  protected List<Integer> excelRows = new ArrayList<>();

  public String getMessage () {
    return "";
  }

  public abstract String getCompromisoNotFoundMessage();

  public abstract String getNotEnoughMoneyMessage(double available);

  public abstract String getErrorLiquitationMessage(String error);

  public abstract String getSuccessLiquidationMessage();
}
