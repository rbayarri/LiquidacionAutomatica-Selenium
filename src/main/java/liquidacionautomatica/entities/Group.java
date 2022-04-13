/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import liquidacionautomatica.validations.Validations;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author rbayarri
 */
public class Group {

  private String groupName;
  private String typeOP;
  private Expediente file;
  private ArrayList<Liquidacion> liquidaciones;
  private ArrayList<Liquidacion> liquidacionesLiquidadas;
  private ArrayList<Liquidacion> liquidacionesARetener;
  private ArrayList<Liquidacion> liquidacionesRetenidas;

  public Group() {
    this.liquidaciones = new ArrayList();
    this.liquidacionesLiquidadas = new ArrayList();
    this.liquidacionesARetener = new ArrayList();
    this.liquidacionesRetenidas = new ArrayList();
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getTypeOP() {
    return typeOP;
  }

  public void setTypeOP(String typeOP) {
    this.typeOP = typeOP;
  }

  public Expediente getFile() {
    return file;
  }

  public void setFile(Expediente file) {
    this.file = file;
  }

  public ArrayList<Liquidacion> getLiquidaciones() {
    return liquidaciones;
  }

  public void setLiquidaciones(ArrayList<Liquidacion> liquidaciones) {
    this.liquidaciones = liquidaciones;
  }

  public ArrayList<Liquidacion> getLiquidacionesLiquidadas() {
    return liquidacionesLiquidadas;
  }

  public void setLiquidacionesLiquidadas(ArrayList<Liquidacion> liquidacionesLiquidadas) {
    this.liquidacionesLiquidadas = liquidacionesLiquidadas;
  }

  public ArrayList<Liquidacion> getLiquidacionesARetener() {
    return liquidacionesARetener;
  }

  public void setLiquidacionesARetener(ArrayList<Liquidacion> liquidacionesARetener) {
    for (Liquidacion l : liquidacionesARetener) {
      this.liquidacionesARetener.add(l);
    }
  }

  public ArrayList<Liquidacion> getLiquidacionesRetenidas() {
    return liquidacionesRetenidas;
  }

  public void setLiquidacionesRetenidas(ArrayList<Liquidacion> liquidacionesRetenidas) {
    this.liquidacionesRetenidas = liquidacionesRetenidas;
  }

  public void addLiquidacionLiquidada(Liquidacion liquidacion) {
    if (liquidacion.getOp() != null) {
      this.liquidacionesLiquidadas.add(liquidacion);
    }
  }

  public void addLiquidacionRetenida(Liquidacion liquidacion) {
    if (liquidacion.getOp() != null) {
      this.liquidacionesRetenidas.add(liquidacion);
    }
  }

  public void addRetenidasResto() {
    for (Liquidacion liquidacion : this.liquidacionesARetener) {
      if (liquidacion.getResultAutorizacion() == null) {
        addLiquidacionRetenida(liquidacion);
      }
    }
  }

  @Override
  public String toString() {
    return "Nombre del grupo: " + this.groupName
        + "\nExpediente: " + this.file
        + "\nTipo OP: " + this.typeOP;
  }

  public String toStringLiquidaciones() {
    String message = "";
    for (int i = 0; i < this.liquidaciones.size(); i++) {
      message += this.liquidaciones.get(i).toString();
      message += "---------------------------------\n";
    }
    return message;
  }

  public String getCUIT0() {
    String message = "\n\nDOCUMENTO 0 - ";
    for (Liquidacion li : this.getLiquidacionesRetenidas()) {
      LiquidacionContrato l = (LiquidacionContrato) li;
      if (l.getCUIT().charAt(2) == '0') {
        message += l.getBeneficiary() + " - ";
      }
    }
    if (message.equals("\n\nDOCUMENTO 0 - ")) {
      message = "\n\n";
    } else {
      message = message.substring(0, message.length() - 2) + "\n";
    }
    return message;
  }

  public double getTotalAmount() {
    double amount = 0;
    for (Liquidacion li : liquidaciones) {
      amount += li.getTotalAmount();
    }
    return amount;
  }

  public double getAmountLiquidado() {
    double amount = 0;
    for (Liquidacion li : liquidacionesLiquidadas) {
      amount += li.getTotalAmount();
    }
    return amount;
  }

  public double getAmountRetenidas() {
    double amount = 0;
    for (Liquidacion li : liquidacionesRetenidas) {
      LiquidacionContrato l = (LiquidacionContrato) li;
      amount += l.getTotalAmount();
    }
    return amount;
  }

  public double getAmountRetenciones() {
    double amount = 0;
    for (Liquidacion li : liquidacionesRetenidas) {
      LiquidacionContrato l = (LiquidacionContrato) li;
      amount += l.getAmountRetenciones();
    }
    return amount;
  }

  public String readCell(Sheet sheet, int vRow, int vColumn) {
    Row row = sheet.getRow(vRow);
    Cell cell = row.getCell(vColumn - 1);
    String cellValue = null;
    try {
      cellValue = cell.toString();
    } catch (Exception e) {
      return null;
    }
    return cellValue;
  }

  public String readCell(Sheet sheet, int vRow, int vColumn, char type) {
    Double aux = null;
    String cellValue = readCell(sheet, vRow, vColumn);
    if (cellValue != null && !cellValue.isEmpty()) {
      if (type == 'i') {
        aux = Double.parseDouble(cellValue);
        return String.valueOf(aux.intValue());
      }
      return cellValue;
    }
    return null;
  }

  public void readingHeaders(Sheet sheet, String type) {
    String groupName = null;
    String typeFile = null;
    String numberFile = null;
    String yearFile = null;
    if (type.equals("Contratos")) {
      String month = readCell(sheet, 0, 2, 'i').toUpperCase();
      String year = readCell(sheet, 1, 2, 'i');
      String listing = readCell(sheet, 2, 2, 'i');
      Validations.isValidMonth(month);
      Validations.isYear(year);
      Validations.isNumber(listing);
      groupName = "HONORARIOS " + month + " " + listing + " " + year;
      typeFile = readCell(sheet, 3, 2).toUpperCase();
      if (!typeFile.equals("E_EX")) {
        JOptionPane.showMessageDialog(null, "No se reconoce el tipo de expediente: " + typeFile);
        System.exit(0);
      }
      numberFile = readCell(sheet, 4, 2,'i');
      Validations.isNumber(numberFile);
      yearFile = readCell(sheet, 5, 2,'i');
      Validations.isYear(yearFile);
    } else {
      String period = readCell(sheet, 0, 1);

      groupName = "INCENTIVOS " + period.substring(period.indexOf(' ') + 1);
      typeFile = "E_EX";
      numberFile = JOptionPane.showInputDialog("Ingrese número de expediente");
      yearFile = JOptionPane.showInputDialog("Ingrese año de expediente");

    }
    Expediente expediente = new Expediente();
    expediente.setType(typeFile);
    expediente.setNumber(Integer.parseInt(numberFile));
    expediente.setYear(Integer.parseInt(yearFile));
    setGroupName(groupName);
    setFile(expediente);
  }

  public void readingContratosExcel(Sheet sheet) {
    int i = 8;
    while (true) {
      String afip = readCell(sheet, i, 1);
      if (afip == null || afip.isEmpty()) {
        return;
      }
      Validations.validAFIP(afip);
      String CUIT = readCell(sheet, i, 2);
      Validations.validCUIT(CUIT);
      String beneficiary = readCell(sheet, i, 3) + ", " + readCell(sheet, i, 4);
      String typeNUI = "NUI";
      String numberNUI = readCell(sheet, i, 13,'i');
      Validations.isNumber(numberNUI);
      String yearNUI = readCell(sheet, i, 14,'i');
      Validations.isYear(yearNUI);

      Compromiso compromiso = new Compromiso();
      compromiso.setType(typeNUI);
      compromiso.setNumber(Integer.parseInt(numberNUI));
      compromiso.setYear(Integer.parseInt(yearNUI));

      OP op = getOP(sheet, i, 16);

      LiquidacionContrato liquidacion = new LiquidacionContrato();
      liquidacion.setCUIT(CUIT);
      liquidacion.setSituationAFIP(afip);
      liquidacion.setBeneficiary(beneficiary);
      liquidacion.setCompromiso(compromiso);

      liquidacion.setOp(op);

      String liquidacionDescription = "";
      int quantityInvoices = determinerQuantityInvoices(sheet, CUIT, numberNUI, yearNUI, i);
      for (int j = 0; j < quantityInvoices; j++) {
        String type = readCell(sheet, i + j, 9);
        Validations.validTypeInvoice(type);
        char letter = readCell(sheet, i + j, 10).toUpperCase().charAt(0);
        Validations.validLetterInvoice(letter, afip);
        String number = readCell(sheet, i + j, 11);
        if (number == null || number.isEmpty()) {
          JOptionPane.showMessageDialog(null, "No se indica número de comprobante");
          System.exit(0);
        }
        String date = readCell(sheet, i + j, 12);
        String amount = readCell(sheet, i + j, 8);
        Validations.validAmount(amount);
        String description = readCell(sheet, i + j, 6) + " - " + readCell(sheet, i + j, 7);
        Validations.validDescription(description);
        liquidacionDescription += description + " - ";

        Invoice invoice = new Invoice();
        invoice.setType(type);
        invoice.setLetter(letter);
        invoice.setNumber(number);
        invoice.setDate(date);
        invoice.setAmount(amount);
        invoice.setDescription(description);
        liquidacion.addInvoice(invoice);
      }
      i += quantityInvoices;
      liquidacion.setDescription(liquidacionDescription);
      liquidaciones.add(liquidacion);
    }
  }

  public int determinerQuantityInvoices(Sheet sheet, String CUIT, String numberNUI, String yearNUI, int index) {
    int quantityInvoices = 1;
    int i = 1;
    while (true) {
      if (CUIT.equals(readCell(sheet, index + i, 2))
          && numberNUI.equals(readCell(sheet, index + i, 13,'i'))
          && yearNUI.equals(readCell(sheet, index + i, 14,'i'))) {
        quantityInvoices++;
        i++;
      } else {
        break;
      }
    }
    return quantityInvoices;
  }

  public void reading36Excel(Sheet sheet) {

    int i = 2;
    while (true) {
      String NUI = readCell(sheet, i, 1);
      if (NUI == null || NUI.isEmpty()) {
        return;
      }
      String typeNUI = "NUI";
      String numberNUI = NUI.substring(0, NUI.indexOf('/'));
      Validations.isNumber(numberNUI);
      String yearNUI = NUI.substring(NUI.indexOf('/') + 1);
      Validations.isYear(yearNUI);
      Compromiso compromiso = new Compromiso();
      compromiso.setType(typeNUI);
      compromiso.setNumber(Integer.parseInt(numberNUI));
      compromiso.setYear(Integer.parseInt(yearNUI));

      String dependency = readCell(sheet, i, 2,'i');
      while (dependency.length() != 3) {
        dependency = "0" + dependency;
      }
      Validations.validDependency(dependency);
      String amount = readCell(sheet, i, 3);
      Validations.validAmount(amount);

      OP op = getOP(sheet, i, 4);

      Liquidacion36 liquidacion = new Liquidacion36();
      liquidacion.setCompromiso(compromiso);
      liquidacion.setDependency(dependency);
      liquidacion.setTotalAmount(Double.parseDouble(amount));
      liquidacion.setDescription(this.groupName);
      liquidacion.setOp(op);

      this.liquidaciones.add(liquidacion);
      i++;
    }
  }

  public OP getOP(Sheet sheet, int vRow, int vColumn) {
    String opTotal = readCell(sheet, vRow, vColumn);
    OP op = null;
    if (opTotal != null && !opTotal.isEmpty()) {
      String typeOP = opTotal.substring(0, 4);
      String numberOP = opTotal.substring(5, opTotal.indexOf('/'));
      String yearOP = opTotal.substring(opTotal.indexOf('/') + 1, opTotal.length());
      Validations.isNumber(numberOP);
      Validations.isYear(yearOP);
      op = new OP();
      op.setType(typeOP);
      op.setNumber(Integer.parseInt(numberOP));
      op.setYear(Integer.parseInt(yearOP));
      return op;
    }
    return op;
  }
}
