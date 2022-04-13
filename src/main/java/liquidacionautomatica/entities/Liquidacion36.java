/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

/**
 *
 * @author rbayarri
 */
public class Liquidacion36 extends Liquidacion {

  private String dependency;

  public Liquidacion36() {
  }

  public String getDependency() {
    return dependency;
  }

  public void setDependency(String dependency) {
    this.dependency = dependency;
  }

  public String toString() {
    return this.compromiso + "\nDependencia: " + this.dependency
            + "\nDescripcion: " + this.description
            + "\nImporte: " + this.totalAmount + "\nOP: " + this.op;
  }

}
