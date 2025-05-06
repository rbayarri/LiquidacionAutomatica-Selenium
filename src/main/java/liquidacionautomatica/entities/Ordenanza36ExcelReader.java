package liquidacionautomatica.entities;

import liquidacionautomatica.validations.Validations;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Ordenanza36ExcelReader implements ExcelReader {

    private final int opColumn = 4;

    @Override
    public String getGroupName(Sheet sheet) {
        String period = readCell(sheet, 0, 1);
        if (StringUtils.isBlank(period)) {
            JOptionPane.showMessageDialog(null, "No se reconoce el periodo");
            System.exit(0);
        }
        return "INCENTIVOS " + period.substring(period.indexOf(' ') + 1);
    }

    @Override
    public Expediente getExpediente(Sheet sheet) {
        String typeFile = "TRAM";
        String numberFile = JOptionPane.showInputDialog("Ingrese número de expediente");
        String yearFile = JOptionPane.showInputDialog("Ingrese año de expediente");

        Expediente expediente = new Expediente();
        expediente.setType(typeFile);
        expediente.setNumber(Integer.parseInt(numberFile));
        expediente.setYear(Integer.parseInt(yearFile));
        return expediente;
    }

    @Override
    public List<Liquidation> getLiquidaciones(Sheet sheet) {

        List<Liquidation> liquidaciones = new ArrayList<>();

        int i = 2;
        while (true) {
            String NUI = readCell(sheet, i, 1);
            if (StringUtils.isBlank(NUI)) {
                return liquidaciones;
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

            String dependency = readCell(sheet, i, 2);
            while (dependency.length() != 3) {
                dependency = "0" + dependency;
            }
            Validations.validDependency(dependency);
            String amount = readCellDouble(sheet, i, 3);
            Validations.validAmount(amount);

            OP op = getOP(sheet, i, opColumn);

            Liquidation36 liquidacion = new Liquidation36();
            liquidacion.setCompromiso(compromiso);
            liquidacion.setDependency(dependency);
            liquidacion.setTotalAmount(Double.parseDouble(amount));
            liquidacion.setDescription(getGroupName(sheet));
            liquidacion.setOp(op);
            liquidacion.getExcelRows().add(i);

            liquidaciones.add(liquidacion);
            i++;
        }

    }
}
