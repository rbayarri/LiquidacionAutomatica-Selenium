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
    if (type.equalsIgnoreCase("Contratos")) {
      for (Liquidacion li : group.getLiquidaciones()) {
        if (li.getOp() == null) {
          message += "\n";
          message += formatoPesos.format(li.getTotalAmount());
          message += " - ";
          LiquidacionContrato liq = (LiquidacionContrato) li;
          message += String.format("%-50s", liq.getBeneficiary());
        }
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

  public void writeInstrucciones() {

    String message = "\n";
    message += "Instrucciones para resolver problemas\n\n";
    message += "Si el contratado no tiene asignado un número de OPCT,"
            + " quiere decir que no pudo ser liquidado\n";
    message += "Un mail automático se mandó a contratos@uncu.edu.ar,"
            + " msalinas@uncu.edu.ar y a rfernandez@uncu.edu.ar informando tal situación\n";
    message += "En el caso que el problema pueda ser solucionado por el liquidador,"
            + " notificar a los mismos destinatarios para que desestimen el mail automático\n";

    message += "Si este es el caso se debe proseguir de la siguiente manera:\n";
    message += "1) Realizar la liquidación manualmente\n";
    message += "2) Practicar las retenciones que correspondan\n";
    message += "3) Agregar la liquidación al grupo\n";
    message += "4) Modificar este archivo";
    message += "  4.1) Cantidad de registros\n";
    message += "  4.2) Total\n";
    message += "  4.3) Total con retenciones\n";
    message += "  4.4) Incorporar número de OP y nombre del contratado\n\n";

    message += "Si un contratado fue excluido por no tener CBU asignado, "
            + "se mandó un mail automático a lcalanoce@uncu.edu.ar con los datos necesarios para la consulta en el banco\n";
    message += "El liquidador debe esperar un mail de parte de Luisina indicando la forma de proseguir.\n\n";
    message += "Si el contratado tiene cuenta bancaria:\n";
    message += "1) Practicar las retenciones que correspondan\n";
    message += "2) Agregar la liquidación al grupo\n";
    message += "3) Modificar este archivo";
    message += "  3.1) Cantidad de registros\n";
    message += "  3.2) Total\n";
    message += "  3.3) Total con retenciones\n";
    message += "  3.4) Incorporar número de OP y nombre del contratado\n\n";

    message += "Si el contratado no tiene cuenta bancaria:\n";
    message += "1) Anular la OPCT generada\n";
    this.write(message);
  }
}
