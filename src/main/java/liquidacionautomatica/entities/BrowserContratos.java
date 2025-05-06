package liquidacionautomatica.entities;

import liquidacionautomatica.exceptions.NotMatchBeneficiary;
import liquidacionautomatica.validations.Validations;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

import static liquidacionautomatica.HtmlElement.*;

@Getter
public class BrowserContratos extends Browser {

    private final String typeOp = "OPCT";
    protected final String form1 = "2282";
    protected final String form2 = "84000213";
    protected final String form3 = "2035";

    @Override
    public void goToNew() {
        driver.findElement(By.id(ROOT_BUTTON)).click();
        driver.findElement(By.id(ROOT_EXPENSES)).click();
        driver.findElement(By.id(ROOT_PURCHASES)).click();
        driver.findElement(By.id(ROOT_DEVENGADO_PURCHASES)).click();
        driver.findElement(By.id(ROOT_NEW_DEVENGADO_PURCHASES)).click();
        wait(WAITING_TIME_AFTER_SCREEN_SELECTION);
    }

    @Override
    protected void completeSpecificData(Liquidation liquidation) throws NotMatchBeneficiary {
        driver.findElement(By.id(CHANGE_TAB)).click();
        wait(WAITING_TIME_TO_CHANGE_TAB);
        WebElement descriptionInput = driver.findElement(By.id(DESCRIPTION_INPUT));
        String newDescription = descriptionInput.getText() + " " + liquidation.getDescription();
        descriptionInput.clear();
        descriptionInput.sendKeys(newDescription);

        String beneficiary = driver.findElement(By.id(PROVEEDOR_INPUT)).getAttribute("value");
        beneficiary = beneficiary.replace("-", "");

        LiquidationContrato liquidationContrato = (LiquidationContrato) liquidation;
        if (!beneficiary.contains(liquidationContrato.getCUIT())) {
            throw new NotMatchBeneficiary("Los beneficiarios no coinciden. CUIT a liquidar: "
                    + liquidationContrato.getCUIT() + ". CUIT del " + liquidationContrato.getCompromiso().toString()
                    + ": " + beneficiary);
        }
        completeInvoices(liquidationContrato);
    }

    private void completeInvoices(LiquidationContrato liquidation) {
        List<Invoice> invoices = liquidation.getInvoices();
        driver.findElement(By.id(CHANGE_TAB_2)).click();
        wait(WAITING_TIME_TO_CHANGE_TAB);
        for (int i = 0; i < invoices.size(); i++) {
            driver.findElement(By.id(ADD_INVOICE_BUTTON)).click();
            WebElement invoiceTypeInput = driver.findElement(By.id(String.format(INVOICE_TYPE_INPUT, 156 + i)));
            invoiceTypeInput.click();
            invoiceTypeInput.sendKeys(invoices.get(i).getType() + " " + invoices.get(i).getLetter());
            driver.findElement(By.id(String.format(INVOICE_NUMBER_INPUT, 156 + i))).sendKeys(invoices.get(i).getNumber());
            driver.findElement(By.id(String.format(INVOICE_DATE_INPUT, 156 + i))).sendKeys(invoices.get(i).getDate());
            driver.findElement(By.id(String.format(INVOICE_AMOUNT_INPUT, 156 + i))).sendKeys(invoices.get(i).getAmount());
            driver.findElement(By.id(String.format(INVOICE_DESCRIPTION_INPUT, 156 + i))).sendKeys(invoices.get(i).getDescription());

        }
        driver.findElement(By.id(CHANGE_TAB_3)).click();
    }

    @Override
    protected void markCvuCheckIfNeeded() {
        driver.findElement(By.id(CVU_CHECK_INPUT)).click();
    }

    @Override
    public void authorizeLevel4(Group group, Double amount2) {
        driver.findElement(By.id(ROOT_BUTTON)).click();
        driver.findElement(By.id(SEARCH_INPUT)).sendKeys("autorizac");
        driver.findElement(By.id(LEVEL_4_AUTHORIZATION)).click();
        wait(WAITING_TIME_AFTER_SCREEN_SELECTION);

        for (Liquidation l : group.getLiquidacionesARetener()) {
            LiquidationContrato li = (LiquidationContrato) l;
            if (li.getSituationAFIP().equals("NA") || li.getSituationAFIP().equals("RI")) {
                WebElement opType = driver.findElement(By.id(OP_TYPE_LEVEL_4_AUTHORIZATION));
                opType.click();
                opType.sendKeys(group.getTypeOP());
                driver.findElement(By.id(OP_NUMBER_LEVEL_4_AUTHORIZATION)).sendKeys(String.valueOf(li.getOp().getNumber()));
                driver.findElement(By.id(OP_YEAR_LEVEL_4_AUTHORIZATION)).sendKeys(String.valueOf(li.getOp().getYear()));
                scrollToEndPage();
                driver.findElement(By.id(FILTER_BUTTON_LEVEL_4_AUTHORIZATION)).click();
                wait(WAITING_TIME_AFTER_SCREEN_SELECTION);
                try {
                    driver.findElement(By.id(OP_SELECTION_LEVEL_4_AUTHORIZATION)).click();
                } catch (NoSuchElementException e) {
                    li.setResultAutorizacion("No se encuentra la liquidación a retener. Revisar si no fue previamente liquidada");
                    driver.findElement(By.id(OP_NUMBER_LEVEL_4_AUTHORIZATION)).clear();
                    driver.findElement(By.id(OP_YEAR_LEVEL_4_AUTHORIZATION)).clear();
                    continue;
                }
                driver.findElement(By.id(AUTHORIZE_BUTTON_LEVEL_4_AUTHORIZATION)).click();
                wait(WAITING_TIME_BEFORE_ADD_LINE_RETENTION);
                scrollToEndPage();
                WebElement addRetentionLine = driver.findElement(By.id(ADD_LINE_RETENTION));
                addRetentionLine.click();
                scrollToEndPage();

                if (!retentionLine("Ing. Brutos", li, amount2)) {
                    continue;
                }
                if (li.getSituationAFIP().equalsIgnoreCase("RI")) {
                    addRetentionLine.click();
                    scrollToEndPage();
                    if (!retentionLine("Ganancias", li, amount2)) {
                        continue;
                    }
                    addRetentionLine.click();
                    scrollToEndPage();
                    if (!retentionLine("Iva", li, amount2)) {
                        continue;
                    }
                    scrollToEndPage();
                }
                String retencionesString = driver.findElement(By.id(RETENTION_AMOUNT)).getText();
                li.setAmountRetenciones(Validations.numberFormat(retencionesString));
                driver.findElement(By.id(PROCESS_RETENTION)).click();
                driver.findElement(By.id(ERROR_BUTTON)).click();
                wait(WAITING_TIME_TO_PROCESS_RETENTION);
                driver.findElement(By.id(OP_NUMBER_LEVEL_4_AUTHORIZATION)).clear();
                driver.findElement(By.id(OP_YEAR_LEVEL_4_AUTHORIZATION)).clear();
            }
            group.addLiquidacionRetenida(li);
        }

        goToLevel4GroupAuthorization();
        filterGroup(group);
        makeGroupRetention();
    }

    private boolean retentionLine(String tax, LiquidationContrato li, Double amount2) {
        String condition = null;
        int order = -1;
        double monto = li.getTotalAmount();
        WebElement cancelRetention = driver.findElement(By.id(CANCEL_RETENTION_BUTTON));
        if (tax.equals("Ganancias")) {
            order = 1;
            condition = "Locacion de Obra o Servicio";
            if (li.getInvoices().get(0).getLetter() == 'A') {
                monto = monto / 1.21;
            }
        } else if (tax.equals("Iva")) {
            order = 2;
            condition = "Loc. o prest. serv. no incl.en inc.a)yb)";
        } else if (tax.equals("Ing. Brutos")) {
            order = 0;
            if (li.getInvoices().get(0).getLetter() == 'A') {
                monto = monto / 1.21;
            }

            if (Objects.nonNull(amount2)) {
                if (monto > amount2) {
                    condition = "Contratados del Estado Nacional (4%)";
                } else {
                    condition = "CONTRATADOS ESTADO NACIONAL 2%";
                }
            } else {
                condition = "Contratados del Estado Nacional (4%)";
            }

        } else {
            li.setResultAutorizacion("No se reconoce el impuesto a retener");
            cancelRetention.click();
            driver.findElement(By.id(COLLAPSE_RETENTION_FILTER)).click();
            driver.findElement(By.id(OP_NUMBER_LEVEL_4_AUTHORIZATION)).clear();
            driver.findElement(By.id(OP_YEAR_LEVEL_4_AUTHORIZATION)).clear();
            return false;
        }

        WebElement taxConcept = driver.findElement(By.id(String.format(TAX_CONCEPT, 156 + order)));
        if (!taxConcept.getText().contains(tax.replace(" ", ""))) {
            li.setResultAutorizacion("No se encuentra cargado el impuesto a retener");
            cancelRetention.click();
            driver.findElement(By.id(COLLAPSE_RETENTION_FILTER)).click();
            driver.findElement(By.id(OP_NUMBER_LEVEL_4_AUTHORIZATION)).clear();
            driver.findElement(By.id(OP_YEAR_LEVEL_4_AUTHORIZATION)).clear();
            return false;
        }
        boolean retenido = false;
        while (!retenido) {
            taxConcept.click();
            taxConcept.sendKeys(tax);
            WebElement retentionForm = driver.findElement(By.id(RETENTION_FORM));
            retentionForm.click();
            wait(WAITING_TIME_AFTER_TAX_SELECTION);
            WebElement taxRegimen = driver.findElement(By.id(String.format(TAX_REGIMEN, 156 + order)));
            taxRegimen.click();
            taxRegimen.sendKeys(condition);

            WebElement taxBase = driver.findElement(By.id(String.format(TAX_BASE, 156 + order)));
            taxBase.clear();
            taxBase.sendKeys(String.valueOf(monto));
            driver.findElement(By.id(String.format(TAX_CALCULATE, 156 + order))).click();

            try {
                driver.findElement(By.id(ERROR_BUTTON)).click();
                scrollToEndPage();
                taxConcept.click();
                taxConcept.sendKeys("--");
                retentionForm.click();
                wait(WAITING_TIME_AFTER_TAX_SELECTION);
                scrollToEndPage();
            } catch (Exception e) {
                retenido = true;
            }
        }
        scrollToEndPage();
        return true;
    }
}
