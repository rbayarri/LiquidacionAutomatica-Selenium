package liquidacionautomatica.entities;

import liquidacionautomatica.validations.Validations;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Objects;

public interface ExcelReader {

    String getGroupName(Sheet sheet);

    Expediente getExpediente(Sheet sheet);

    List<Liquidation> getLiquidaciones(Sheet sheet);

    int getOpColumn();

    default String readCell(Sheet sheet, int vRow, int vColumn) {
        Double aux = null;
        Row row = sheet.getRow(vRow);
        if (row == null) {
            return null;
        }
        Cell cell = row.getCell(vColumn - 1);

        if (cell == null) {
            return null;
        }

        if (cell.toString().isEmpty() && vColumn != 16) {
            JOptionPane.showMessageDialog(null, "Existen celdas vacías");
            System.exit(0);
        }

        try {
            aux = Double.parseDouble(cell.toString());
            return String.valueOf(aux.intValue());
        } catch (Exception e) {
        }

        try {
            return cell.toString();
        } catch (Exception e) {
            return null;
        }
    }

    default String readCellCuit(Sheet sheet, int vRow, int vColumn) {
        Row row = sheet.getRow(vRow);
        Cell cell = row.getCell(vColumn - 1);

        if (cell == null) {
            return null;
        }

        double prueba = cell.getNumericCellValue();
        BigDecimal bd = new BigDecimal(prueba);
        String cuit = bd.round(new MathContext(11)).toPlainString();

        //String cuit = cell.toString();
        if (cuit.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Existen celdas vacías");
            System.exit(0);
        }
        //String valor = cell.toString().replace(".", "").replace("E10", "");
        return cuit;
    }

    default String readCellDouble(Sheet sheet, int vRow, int vColumn) {
        Double aux = null;
        Row row = sheet.getRow(vRow);
        Cell cell = row.getCell(vColumn - 1);

        if (cell == null) {
            return null;
        }

        if (cell.toString().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Existen celdas vacías");
            System.exit(0);
        }

        try {
            aux = Double.parseDouble(cell.toString());
            return String.valueOf(aux);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "El importe no puede ser leido correctamente");
            System.exit(0);
        }
        return null;
    }

    default OP getOP(Sheet sheet, int vRow, int vColumn) {
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



    default void completeOP(Group group, XSSFWorkbook workbook, File file) {
        XSSFSheet sheet = workbook.getSheetAt(0);

        for (Liquidation liquidation : group.getLiquidaciones()) {

            if (Objects.isNull(liquidation.getOp())) {
                continue;
            }

            String opValue = liquidation.getOp().toString().replace(" ", "");

            for (int index : liquidation.getExcelRows()) {

                OP op = getOP(sheet, index, getOpColumn());
                if (Objects.nonNull(op)) {
                    continue;
                }

                // Get or create the row
                Row row = sheet.getRow(index);
                if (row == null) {
                    row = sheet.createRow(index);
                }

                // Get or create the cell
                Cell cell = row.getCell(getOpColumn() - 1);
                if (cell == null) {
                    cell = row.createCell(getOpColumn() - 1);
                }

                // Set the value
                cell.setCellValue(opValue);
            }
        }
        // Save the changes
        try {
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }
}
