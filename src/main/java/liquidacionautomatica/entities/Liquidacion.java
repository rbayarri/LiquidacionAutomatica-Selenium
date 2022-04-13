/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

/**
 *
 * @author rbayarri
 */
public abstract class Liquidacion {

  protected Compromiso compromiso;
  protected String description;
  protected double totalAmount;
  protected OP op;
  protected String resultLiquidacion;
  protected String resultAutorizacion;

  public Compromiso getCompromiso() {
    return compromiso;
  }

  public void setCompromiso(Compromiso compromiso) {
    this.compromiso = compromiso;
  }

  public double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(double amount) {
    this.totalAmount = amount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public OP getOp() {
    return op;
  }

  public void setOp(OP op) {
    this.op = op;
  }

  public String getResultLiquidacion() {
    return resultLiquidacion;
  }

  public void setResultLiquidacion(String resultLiquidacion) {
    this.resultLiquidacion = resultLiquidacion;
  }

  public String getResultAutorizacion() {
    return resultAutorizacion;
  }

  public void setResultAutorizacion(String resultAutorizacion) {
    this.resultAutorizacion = resultAutorizacion;
  }
}
