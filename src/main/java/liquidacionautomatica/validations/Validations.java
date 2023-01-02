/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.validations;

import java.time.Year;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * @author rbayarri
 */
public class Validations {

  public static void isNumber(String numberString) {
    try {
      Double.valueOf(numberString);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "El archivo no tiene el formato necesario para ser procesado");
      System.exit(0);
    }
  }

  public static void isValidMonth(String month) {
    ArrayList<String> months = new ArrayList();
    months.add("ENERO");
    months.add("FEBRERO");
    months.add("MARZO");
    months.add("ABRIL");
    months.add("MAYO");
    months.add("JUNIO");
    months.add("JULIO");
    months.add("AGOSTO");
    months.add("SEPTIEMBRE");
    months.add("OCTUBRE");
    months.add("NOVIEMBRE");
    months.add("DICIEMBRE");
    if (!months.contains(month)) {
      JOptionPane.showMessageDialog(null, "El mes ingresado es incorrecto");
      System.exit(0);
    }
  }

  public static void isYear(String yearString) {
    int year = 0;
    try {
      year = Integer.parseInt(yearString);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "El archivo no tiene el formato necesario para ser procesado");
      System.exit(0);
    }
    if (year != Year.now().getValue() && year != Year.now().getValue() - 1 && year != Year.now().getValue() - 2) {
      JOptionPane.showMessageDialog(null, "El año debe ser igual al año actual o al año anterior");
      System.exit(0);
    }
  }

  public static void validCUIT(String CUIT) {
    if (CUIT == null || CUIT.isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se indica CUIT");
      System.exit(0);
    }
    if (CUIT.length() != 11) {
      JOptionPane.showMessageDialog(null, "El CUIT no tiene el formato correcto: " + CUIT);
      System.exit(0);
    }
    try {
      Double.parseDouble(CUIT);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "El CUIT no tiene el formato correcto: " + CUIT);
      System.exit(0);
    }
  }

  public static void validAFIP(String afip) {
    if (afip == null || afip.isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se encuentra la situación fiscal");
      System.exit(0);
    }
    if (!afip.equals("AC") && !afip.equals("NA") && !afip.equals("EX") && !afip.equals("RI")) {
      JOptionPane.showMessageDialog(null, "No se reconoce la situación de afip: " + afip);
      System.exit(0);
    }
  }

  public static void validNUI(String type) {
    if (type == null || type.isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se encuentra el tipo de compromiso");
      System.exit(0);
    }
    if (!type.equals("NUI") && !type.equals("BEHU")) {
      JOptionPane.showMessageDialog(null, "No se reconoce el tipo de compromiso: " + type);
      System.exit(0);
    }

  }

  public static void validDescription(String description) {
    if (description == null || description.isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se indica descripción");
      System.exit(0);
    }
  }

  public static void validInvoices(int quantity) {
    if (quantity < 1) {
      JOptionPane.showMessageDialog(null, "La cantidad de comprobantes debe ser mayor a 0");
      System.exit(0);
    }
  }

  public static void validTypeInvoice(String type) {
    if (type == null || type.isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se indica tipo de comprobante");
      System.exit(0);
    }
    if (!type.equalsIgnoreCase("Factura") && !type.equalsIgnoreCase("Nota de Credito")) {
      JOptionPane.showMessageDialog(null, "No se reconoce el tipo de comprobante: " + type);
      System.exit(0);
    }
  }

  public static void validLetterInvoice(char letter, String afip) {
    if (letter != 'A' && letter != 'B' && letter != 'C') {
      JOptionPane.showMessageDialog(null, "No se reconoce la letra del comprobante: " + letter);
      System.exit(0);
    }
    if (afip.equals("AC") && letter != 'C') {
      JOptionPane.showMessageDialog(null, "No corresponde la letra del comprobante con la situación fiscal indicada\n"
              + "Situación fiscal: " + afip + "\nLetra del comprobante: " + letter);
      System.exit(0);
    }
    if (afip.equals("RI") && letter == 'C') {
      JOptionPane.showMessageDialog(null, "No corresponde la letra del comprobante con la situación fiscal indicada\n"
              + "Situación fiscal: " + afip + "\nLetra del comprobante: " + letter);
      System.exit(0);
    }
  }

  public static void validAmount(String number) {
    if (number == null || number.isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se indica importe del comprobante");
      System.exit(0);
    }
    try {
      Double.parseDouble(number);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "El importe del comprobante no es vàlido: " + number);
      System.exit(0);
    }
  }

  public static void validDependency(String dependency) {
    if (dependency == null || dependency.isEmpty()) {
      JOptionPane.showMessageDialog(null, "No se indica dependencia");
      System.exit(0);
    }
    if (dependency.length() != 3) {
      JOptionPane.showMessageDialog(null, "La dependencia no tiene el formato requerido: " + dependency);
      System.exit(0);
    }
    try {
      Integer.parseInt(dependency);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "La dependencia no tiene el formato requerido: " + dependency);
      System.exit(0);
    }
  }

  public static double numberFormat(String number) {
    return Double.parseDouble(number.trim().replace("$ ", "")
            .replace(".", "").replace(",", "."));
  }
}
