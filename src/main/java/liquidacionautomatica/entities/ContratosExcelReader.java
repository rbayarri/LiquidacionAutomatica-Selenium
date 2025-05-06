package liquidacionautomatica.entities;


import liquidacionautomatica.validations.Validations;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ContratosExcelReader implements ExcelReader {

    private final int opColumn = 16;

    @Override
    public String getGroupName(Sheet sheet) {
        String month = readCell(sheet, 0, 2).toUpperCase();
        String year = readCell(sheet, 1, 2);
        String listing = readCell(sheet, 2, 2);
        Validations.isValidMonth(month);
        Validations.isYear(year);
        Validations.isNumber(listing);
        return "HONORARIOS " + month + " " + listing + " " + year;
    }

    @Override
    public Expediente getExpediente(Sheet sheet) {
        String typeFile = readCell(sheet, 3, 2).toUpperCase();
        if (!typeFile.equals("E_EX") && !typeFile.equals("TRAM")) {
            JOptionPane.showMessageDialog(null, "No se reconoce el tipo de expediente: " + typeFile);
            System.exit(0);
        }
        String numberFile = readCell(sheet, 4, 2);
        Validations.isNumber(numberFile);
        String yearFile = readCell(sheet, 5, 2);
        Validations.isYear(yearFile);

        Expediente expediente = new Expediente();
        expediente.setType(typeFile);
        expediente.setNumber(Integer.parseInt(numberFile));
        expediente.setYear(Integer.parseInt(yearFile));
        return expediente;
    }

    @Override
    public List<Liquidation> getLiquidaciones(Sheet sheet) {

        List<Liquidation> liquidaciones = new ArrayList<>();

        int i = 8;
        while (true) {
            String afip = readCell(sheet, i, 1);
            if (StringUtils.isBlank(afip)) {
                return liquidaciones;
            }
            Validations.validAFIP(afip);
            String CUIT = readCellCuit(sheet, i, 2);
            Validations.validCUIT(CUIT);
            String beneficiary = readCell(sheet, i, 3) + ", " + readCell(sheet, i, 4);
            String typeNUI = "NUI";
            String numberNUI = readCell(sheet, i, 13);
            Validations.isNumber(numberNUI);
            String yearNUI = readCell(sheet, i, 14);
            Validations.isYear(yearNUI);

            Compromiso compromiso = new Compromiso();
            compromiso.setType(typeNUI);
            compromiso.setNumber(Integer.parseInt(numberNUI));
            compromiso.setYear(Integer.parseInt(yearNUI));

            OP op = getOP(sheet, i, opColumn);

            LiquidationContrato liquidacion = new LiquidationContrato();
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
                String date = readCellDate(sheet, i + j, 12);
                String amount = readCellDouble(sheet, i + j, 8);
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
                liquidacion.getExcelRows().add(i + j);
            }
            i += quantityInvoices;
            liquidacion.setDescription(liquidacionDescription);
            liquidaciones.add(liquidacion);
        }
    }

    private String readCellDate(Sheet sheet, int vRow, int vColumn) {
        Row row = sheet.getRow(vRow);
        Cell cell = row.getCell(vColumn - 1);

        if (cell == null) {
            return null;
        }

        if (cell.toString().isEmpty() && vColumn != 16) {
            JOptionPane.showMessageDialog(null, "Existen celdas vacías");
            System.exit(0);
        }
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
        } catch (Exception e) {
            return null;
        }
    }

    private int determinerQuantityInvoices(Sheet sheet, String CUIT, String numberNUI, String yearNUI, int index) {
        int quantityInvoices = 1;
        int i = 1;
        while (true) {
            if (CUIT.equals(readCellCuit(sheet, index + i, 2))
                    && numberNUI.equals(readCell(sheet, index + i, 13))
                    && yearNUI.equals(readCell(sheet, index + i, 14))) {
                quantityInvoices++;
                i++;
            } else {
                break;
            }
        }
        return quantityInvoices;
    }
}
