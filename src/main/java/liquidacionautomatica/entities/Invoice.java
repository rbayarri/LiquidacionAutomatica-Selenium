/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rbayarri
 */
@Getter
@Setter
public class Invoice {

  private String type;
  private char letter;
  private String number;
  private String date;
  private String amount;
  private String description;

  @Override
  public String toString() {
    return "Tipo: " + this.type + "\nLetra: " + this.letter + "\nNumero: " + this.number + "\nFecha: "
            + this.date + "\nMonto: " + this.amount + "\nDescripcion: " + this.description;
  }
}
