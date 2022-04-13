/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

/**
 *
 * @author rbayarri
 */
public class Invoice {

  private String type;
  private char letter;
  private String number;
  private String date;
  private String amount;
  private String description;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public char getLetter() {
    return letter;
  }

  public void setLetter(char letter) {
    this.letter = letter;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Tipo: " + this.type + "\nLetra: " + this.letter + "\nNumero: " + this.number + "\nFecha: "
            + this.date + "\nMonto: " + this.amount + "\nDescripcion: " + this.description;
  }
}
