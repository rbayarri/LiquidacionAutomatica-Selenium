/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import liquidacionautomatica.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author rbayarri
 */
public class Writer {

  private final String fileName;

  public Writer(Group group, String directory) {
    this.fileName = directory + "\\resultado liquidacion " + group.getGroupName() + ".txt";
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

  public String writeFirstReadConfirmation(Group group) {

    StringBuilder sb = new StringBuilder();

    sb.append("Resumen de las liquidaciones a realizar...\n");
    sb.append("Nombre del grupo: " + group.getGroupName()
            + "\nExpediente: " + group.getFile().toString());
      for (Liquidation li : group.getLiquidaciones()) {
        sb.append(li.getMessage());
      }
    sb.append("\nTotal: ");
    sb.append(Utils.toCurrencyFormat(group.getTotalAmount()));

    return sb.toString();
  }

  public void writeFirstRead(Group group) {

    String message = String.format("%-30s", "Nombre del grupo:");
    message += group.getGroupName() + "\n";
    message += String.format("%-30s", "Expediente:");
    message += group.getFile().toString() + "\n";
    message += String.format("%-30s", "Liquidaciones a realizar:");
    message += group.getLiquidaciones().size() + "\n";
    message += String.format("%-30s", "Importe total a liquidar");
    message += Utils.toCurrencyFormat(group.getTotalAmount());
    message += "\n\n\n";

    this.write(message);
  }

  public void writeResultLiquidacion(Group group) {
    StringBuilder message = new StringBuilder("Resultados de la liquidación:\n\n");
    for (Liquidation l : group.getLiquidaciones()) {
      if (l.getResultLiquidacion() == null) {
        l.setResultLiquidacion("Liquidada con anterioridad");
      }
      message.append(l.getResultLiquidacion());
      message.append("\n");
    }
    this.write(message.toString());
  }

  public void writeExcludedLiquidations(Group group) {
    StringBuilder message = new StringBuilder("\n\nLiquidaciones excluidas por no tener CBU:\n\n");
    for (Liquidation li : group.getLiquidacionesLiquidadas()) {
      if (li.getResultAutorizacion() != null) {
        LiquidationContrato l = (LiquidationContrato) li;
        message.append(l.getOp().toString());
        message.append(" - ");
        message.append(l.getBeneficiary());
        message.append(" - ");
        message.append(l.getSituationAFIP());
        message.append(" - ");
        message.append(l.getCUIT());
        message.append("\n");
      }
    }
    this.write(message.toString());
  }

  public void writeLiquidacionNoRetenidas(Group group) {
    StringBuilder message = new StringBuilder("\n\nLiquidaciones no retenidas:\n\n");
    for (Liquidation li : group.getLiquidacionesARetener()) {
      if (li.getResultAutorizacion() != null) {
        LiquidationContrato l = (LiquidationContrato) li;
        message.append(l.getOp().toString()).append(" - ").append(l.getSituationAFIP()).append(" - ");
        message.append(String.format("%-50s", l.getBeneficiary())).append(" - ");
        message.append(l.getResultAutorizacion());
        message.append("\n");
      }
    }
    if (message.toString().equals("\n\nLiquidaciones no retenidas:\n\n")) {
      message.append("Todas las liquidaciones fueron retenidos correctamente");
    }
    this.write(message.toString());
  }

  public void writeForExp(Group group) {
    StringBuilder message = new StringBuilder("\n\nResumen de la liquidación (copiar y pegar en borrador de COMDOC):\n\n");

    if (group.getTypeOP().equals("OPCT")) {
      message.append("OPCT ").append(group.getGroupName());
      message.append(group.getCUIT0());
      message.append(String.format("%-25s", "Cantidad de registros:"));
      message.append(group.getLiquidacionesRetenidas().size()).append("\n");
      message.append(String.format("%-25s", "Total:"));
      message.append(Utils.toCurrencyFormat(group.getAmountRetenidas())).append("\n");
      message.append(String.format("%-25s", "Total con retenciones:"));
      message.append(Utils.toCurrencyFormat(group.getAmountRetenidas() - group.getAmountRetenciones())).append("\n\n");
      for (Liquidation li : group.getLiquidacionesRetenidas()) {
        if (li.getResultAutorizacion() == null) {
          LiquidationContrato l = (LiquidationContrato) li;
          message.append(String.format("%-15s", l.getOp().toString()));
          message.append(" - ").append(l.getBeneficiary());
          message.append("\n");
        }
      }
      this.write(message.toString());
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
    message += "4) Modificar este archivo\n";
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
    message += "3) Modificar este archivo\n";
    message += "  3.1) Cantidad de registros\n";
    message += "  3.2) Total\n";
    message += "  3.3) Total con retenciones\n";
    message += "  3.4) Incorporar número de OP y nombre del contratado\n\n";

    message += "Si el contratado no tiene cuenta bancaria:\n";
    message += "1) Anular la OPCT generada\n";
    this.write(message);
  }
}
