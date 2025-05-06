package liquidacionautomatica.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LiquidationType {

    CONTRATOS("Contratos", new ContratosExcelReader(), new BrowserContratos()),
    INCENTIVOS("Incentivos", new Ordenanza36ExcelReader(), new BrowserIncentivos());

    private String description;
    private ExcelReader excelReader;
    private Browser browser;

    public static LiquidationType fromDescription(String description) {
        for (LiquidationType type : LiquidationType.values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        return null;
    }
}
