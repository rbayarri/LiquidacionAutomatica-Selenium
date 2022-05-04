/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author rbayarri
 */
public class Writer {

  private String fileName;
  private DecimalFormat formatoPesos;

  public Writer(Group group, String directory) {
    this.fileName = directory + "\\resultado liquidacion " + group.getGroupName() + ".txt";
    this.formatoPesos = new DecimalFormat("$ ###,###.00");
  }

  private void write(String message) {
    try {
      FileWriter fileWriter = new FileWriter(this.fileName, true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      bufferedWriter.write(message);
      bufferedWriter.close();
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "No se pudo escribir en el archivo de resultados");
    }
  }

  public String writeFirstReadConfirmation(Group group, String type) {

    String message = "Resumen de las liquidaciones a realizar...\n";
    message += "Nombre del grupo: " + group.getGroupName()
            + "\nExpediente: " + group.getFile().toString();
    for (Liquidacion li : group.getLiquidaciones()) {
      message += "\n";
      message += formatoPesos.format(li.getTotalAmount());
      message += " - ";
      if (type.equalsIgnoreCase("Contratos")) {
        LiquidacionContrato liq = (LiquidacionContrato) li;
        message += String.format("%-50s", liq.getBeneficiary());
      } else {
        Liquidacion36 liq = (Liquidacion36) li;
        message += liq.getCompromiso();

      }
    }
    message += "\nTotal: ";
    message += formatoPesos.format(group.getTotalAmount());

    return message;
  }

  public void writeFirstRead(Group group) {

    String message = String.format("%-30s", "Nombre del grupo:");
    message += group.getGroupName() + "\n";
    message += String.format("%-30s", "Expediente:");
    message += group.getFile().toString() + "\n";
    message += String.format("%-30s", "Liquidaciones a realizar:");
    message += group.getLiquidaciones().size() + "\n";
    message += String.format("%-30s", "Importe total a liquidar");
    message += formatoPesos.format(group.getTotalAmount());
    message += "\n\n\n";

    this.write(message);
  }

  public void writeResultLiquidacion(Group group) {
    String message = "Resultados de la liquidación:\n\n";
    for (Liquidacion l : group.getLiquidaciones()) {
      if (l.getResultLiquidacion() == null) {
        l.setResultLiquidacion("Liquidada con anterioridad");
      }
      message += l.getResultLiquidacion();
      message += "\n";
    }
    this.write(message);
  }

  public void writeLiquidacionesExcluidas(Group group) {
    String message = "\n\nLiquidaciones excluidas por no tener CBU:\n\n";
    for (Liquidacion li : group.getLiquidacionesLiquidadas()) {
      if (li.getResultAutorizacion() != null) {
        LiquidacionContrato l = (LiquidacionContrato) li;
        message += l.getOp().toString();
        message += " - ";
        message += l.getBeneficiary();
        message += " - ";
        message += l.getCUIT();
        message += "\n";
      }
    }
    this.write(message);
    SendEmailTLS.sendMessage("Liquidaciones excluidas por no tener CBU cargado - " + group.getGroupName(), message, true);
  }

  public void writeLiquidacionNoRetenidas(Group group) {
    String message = "\n\nLiquidaciones no retenidas:\n\n";
    for (Liquidacion li : group.getLiquidacionesARetener()) {
      if (li.getResultAutorizacion() != null) {
        LiquidacionContrato l = (LiquidacionContrato) li;
        message += l.getOp().toString() + " - ";
        message += String.format("%-50s", l.getBeneficiary()) + " - ";
        message += l.getResultAutorizacion();
        message += "\n";
      }
    }
    if (message.equals("\n\nLiquidaciones no retenidas:\n\n")) {
      message += "Todas las liquidaciones fueron retenidos correctamente";
    }
    this.write(message);
  }

  public void writeForExp(Group group) {
    String message = "\n\nResumen de la liquidación (copiar y pegar en borrador de COMDOC):\n\n";

    if (group.getTypeOP().equals("OPCT")) {
      message += "OPCT " + group.getGroupName();
      message += group.getCUIT0();
      message += String.format("%-25s", "Cantidad de registros:");
      message += group.getLiquidacionesRetenidas().size() + "\n";
      message += String.format("%-25s", "Total:");
      message += formatoPesos.format(group.getAmountRetenidas()) + "\n";
      message += String.format("%-25s", "Total con retenciones:");
      message += formatoPesos.format(group.getAmountRetenidas() - group.getAmountRetenciones()) + "\n\n";
      for (Liquidacion li : group.getLiquidacionesRetenidas()) {
        if (li.getResultAutorizacion() == null) {
          LiquidacionContrato l = (LiquidacionContrato) li;
          message += String.format("%-15s", l.getOp().toString());
          message += " - " + l.getBeneficiary();
          message += "\n";
        }
      }
      this.write(message);
    }
  }
}
