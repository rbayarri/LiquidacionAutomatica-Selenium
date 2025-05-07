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
        driver.findElement(By.id(INSTANCE.rootButton)).click();
        driver.findElement(By.id(INSTANCE.rootExpenses)).click();
        driver.findElement(By.id(INSTANCE.rootPurchases)).click();
        driver.findElement(By.id(INSTANCE.rootDevengadoPurchases)).click();
        driver.findElement(By.id(INSTANCE.rootNewDevengadoPurchases)).click();
        wait(INSTANCE.waitingTimeAfterScreenSelection);
    }

    @Override
    protected void completeSpecificData(Liquidation liquidation) throws NotMatchBeneficiary {
        driver.findElement(By.id(INSTANCE.changeTab)).click();
        wait(INSTANCE.waitingTimeToChangeTab);
        WebElement descriptionInput = driver.findElement(By.id(INSTANCE.descriptionInput ));
        String newDescription = descriptionInput.getText() + " " + liquidation.getDescription();
        descriptionInput.clear();
        descriptionInput.sendKeys(newDescription);

        String beneficiary = driver.findElement(By.id(INSTANCE.proveedorInput)).getAttribute("value");
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
        driver.findElement(By.id(INSTANCE.changeTab2)).click();
        wait(INSTANCE.waitingTimeToChangeTab);
        for (int i = 0; i < invoices.size(); i++) {
            driver.findElement(By.id(INSTANCE.addInvoiceButton)).click();
            WebElement invoiceTypeInput = driver.findElement(By.id(String.format(INSTANCE.invoiceTypeInput, 156 + i)));
            invoiceTypeInput.click();
            invoiceTypeInput.sendKeys(invoices.get(i).getType() + " " + invoices.get(i).getLetter());
            driver.findElement(By.id(String.format(INSTANCE.invoiceNumberInput, 156 + i))).sendKeys(invoices.get(i).getNumber());
            driver.findElement(By.id(String.format(INSTANCE.invoiceDateInput, 156 + i))).sendKeys(invoices.get(i).getDate());
            driver.findElement(By.id(String.format(INSTANCE.invoiceAmountInput, 156 + i))).sendKeys(invoices.get(i).getAmount());
            driver.findElement(By.id(String.format(INSTANCE.invoiceDescriptionInput, 156 + i))).sendKeys(invoices.get(i).getDescription());

        }
        driver.findElement(By.id(INSTANCE.changeTab3)).click();
    }

    @Override
    protected void markCvuCheckIfNeeded() {
        driver.findElement(By.id(INSTANCE.cvuCheckInput)).click();
    }

    @Override
    public void authorizeLevel4(Group group, Double amount2) {
        driver.findElement(By.id(INSTANCE.rootButton)).click();
        driver.findElement(By.id(INSTANCE.searchInput)).sendKeys("autorizac");
        driver.findElement(By.id(INSTANCE.level4Authorization)).click();
        wait(INSTANCE.waitingTimeAfterScreenSelection);

        for (Liquidation l : group.getLiquidacionesARetener()) {
            LiquidationContrato li = (LiquidationContrato) l;
            if (li.getSituationAFIP().equals("NA") || li.getSituationAFIP().equals("RI")) {
                WebElement opType = driver.findElement(By.id(INSTANCE.opTypeLevel4Authorization));
                opType.click();
                opType.sendKeys(group.getTypeOP());
                driver.findElement(By.id(INSTANCE.opNumberLevel4Authorization)).sendKeys(String.valueOf(li.getOp().getNumber()));
                driver.findElement(By.id(INSTANCE.opYearLevel4Authorization)).sendKeys(String.valueOf(li.getOp().getYear()));
                scrollToEndPage();
                driver.findElement(By.id(INSTANCE.filterButtonLevel4Authorization)).click();
                wait(INSTANCE.waitingTimeAfterScreenSelection);
                try {
                    driver.findElement(By.id(INSTANCE.opSelectionLevel4Authorization)).click();
                } catch (NoSuchElementException e) {
                    li.setResultAutorizacion("No se encuentra la liquidación a retener. Revisar si no fue previamente liquidada");
                    driver.findElement(By.id(INSTANCE.opNumberLevel4Authorization)).clear();
                    driver.findElement(By.id(INSTANCE.opYearLevel4Authorization)).clear();
                    continue;
                }
                driver.findElement(By.id(INSTANCE.authorizeButtonLevel4Authorization)).click();
                wait(INSTANCE.waitingTimeBeforeAddLineRetention);
                scrollToEndPage();
                WebElement addRetentionLine = driver.findElement(By.id(INSTANCE.addLineRetention));
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
                String retencionesString = driver.findElement(By.id(INSTANCE.retentionAmount)).getText();
                li.setAmountRetenciones(Validations.numberFormat(retencionesString));
                driver.findElement(By.id(INSTANCE.processRetention)).click();
                driver.findElement(By.id(INSTANCE.errorButton)).click();
                wait(INSTANCE.waitingTimeToProcessRetention);
                driver.findElement(By.id(INSTANCE.opNumberLevel4Authorization)).clear();
                driver.findElement(By.id(INSTANCE.opYearLevel4Authorization)).clear();
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
        WebElement cancelRetention = driver.findElement(By.id(INSTANCE.cancelRetentionButton));
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
            driver.findElement(By.id(INSTANCE.collapseRetentionFilter)).click();
            driver.findElement(By.id(INSTANCE.opNumberLevel4Authorization)).clear();
            driver.findElement(By.id(INSTANCE.opYearLevel4Authorization)).clear();
            return false;
        }

        WebElement taxConcept = driver.findElement(By.id(String.format(INSTANCE.taxConcept, 156 + order)));
        if (!taxConcept.getText().contains(tax.replace(" ", ""))) {
            li.setResultAutorizacion("No se encuentra cargado el impuesto a retener");
            cancelRetention.click();
            driver.findElement(By.id(INSTANCE.collapseRetentionFilter)).click();
            driver.findElement(By.id(INSTANCE.opNumberLevel4Authorization)).clear();
            driver.findElement(By.id(INSTANCE.opYearLevel4Authorization)).clear();
            return false;
        }
        boolean retenido = false;
        while (!retenido) {
            taxConcept.click();
            taxConcept.sendKeys(tax);
            WebElement retentionForm = driver.findElement(By.id(INSTANCE.retentionForm));
            retentionForm.click();
            wait(INSTANCE.waitingTimeAfterTaxSelection);
            WebElement taxRegimen = driver.findElement(By.id(String.format(INSTANCE.taxRegimen, 156 + order)));
            taxRegimen.click();
            taxRegimen.sendKeys(condition);

            WebElement taxBase = driver.findElement(By.id(String.format(INSTANCE.taxBase, 156 + order)));
            taxBase.clear();
            taxBase.sendKeys(String.valueOf(monto));
            driver.findElement(By.id(String.format(INSTANCE.taxCalculate, 156 + order))).click();

            try {
                driver.findElement(By.id(INSTANCE.errorButton)).click();
                scrollToEndPage();
                taxConcept.click();
                taxConcept.sendKeys("--");
                retentionForm.click();
                wait(INSTANCE.waitingTimeAfterTaxSelection);
                scrollToEndPage();
            } catch (Exception e) {
                retenido = true;
            }
        }
        scrollToEndPage();
        return true;
    }
}
