/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rbayarri
 */
@Data
public class Group {

    private String groupName;
    private String typeOP;
    private Expediente file;
    private List<Liquidation> liquidaciones;
    private List<Liquidation> liquidacionesLiquidadas = new ArrayList<>();
    private List<Liquidation> liquidacionesARetener = new ArrayList<>();
    private List<Liquidation> liquidacionesRetenidas = new ArrayList<>();

    public void setLiquidacionesARetener(List<Liquidation> liquidacionesARetener) {
        this.liquidacionesARetener.addAll(liquidacionesARetener);
    }

    public void addLiquidacionLiquidada(Liquidation liquidation) {
        if (liquidation.getOp() != null) {
            this.liquidacionesLiquidadas.add(liquidation);
        }
    }

    public void addLiquidacionRetenida(Liquidation liquidation) {
        if (liquidation.getOp() != null) {
            this.liquidacionesRetenidas.add(liquidation);
        }
    }

    public void addRetenidasResto() {
        for (Liquidation liquidation : this.liquidacionesARetener) {
            if (liquidation.getResultAutorizacion() == null) {
                addLiquidacionRetenida(liquidation);
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

    public String toStringConfirmacion(String type) {
        String message = "Resumen de las liquidaciones a realizar...\n";
        message += "Nombre del grupo: " + this.groupName
                + "\nExpediente: " + this.file;
        for (Liquidation li : this.getLiquidaciones()) {
            message += "\n";
            if (type.equalsIgnoreCase("Contratos")) {
                LiquidationContrato liq = (LiquidationContrato) li;
                message += String.format("%-30s - ", liq.getBeneficiary());
                //message += " - ";
            } else {
                Liquidation36 liq = (Liquidation36) li;
                message += liq.getCompromiso();
                message += " - ";
            }
            message += li.getTotalAmount();
        }
        message += "\nTotal: ";
        message += this.getTotalAmount();

        return message;
    }

    public String getCUIT0() {
        String message = "\n\nDOCUMENTO 0 - ";
        for (Liquidation li : this.getLiquidacionesRetenidas()) {
            LiquidationContrato l = (LiquidationContrato) li;
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
        for (Liquidation li : liquidaciones) {
            amount += li.getTotalAmount();
        }
        return amount;
    }

    public double getAmountLiquidado() {
        double amount = 0;
        for (Liquidation li : liquidacionesLiquidadas) {
            amount += li.getTotalAmount();
        }
        return amount;
    }

    public double getAmountRetenidas() {
        double amount = 0;
        for (Liquidation li : liquidacionesRetenidas) {
            LiquidationContrato l = (LiquidationContrato) li;
            amount += l.getTotalAmount();
        }
        return amount;
    }

    public double getAmountRetenciones() {
        double amount = 0;
        for (Liquidation li : liquidacionesRetenidas) {
            LiquidationContrato l = (LiquidationContrato) li;
            amount += l.getAmountRetenciones();
        }
        return amount;
    }
}
