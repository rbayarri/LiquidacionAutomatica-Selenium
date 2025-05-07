/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import io.github.bonigarcia.wdm.WebDriverManager;
import liquidacionautomatica.exceptions.NotMatchBeneficiary;
import liquidacionautomatica.validations.Validations;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.*;

import java.util.*;

import static liquidacionautomatica.HtmlElement.*;

/**
 * @author rbayarri
 */
public abstract class Browser {

    protected final Map<String, Object> vars = new HashMap<>();
    protected WebDriver driver;
    protected String originalWindow;
    private JavascriptExecutor js;

    public abstract String getTypeOp();

    protected abstract String getForm1();

    protected abstract String getForm2();

    protected abstract String getForm3();

    public abstract void goToNew();

    protected abstract void completeSpecificData(Liquidation liquidation) throws NotMatchBeneficiary;

    protected abstract void markCvuCheckIfNeeded();

    public abstract void authorizeLevel4(Group group, Double amount2);

    public void open() {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("force-device-scale-factor=0.70");
        options.addArguments("--force-display-scale-factor=0.7");
        this.driver = new ChromeDriver();

        this.driver.manage()
                .window().maximize();

        this.js = (JavascriptExecutor) driver;
    }

    private void scrollTo(int y) {
        this.js.executeScript("window.scrollTo(0,window.scrollY+" + y);
    }

    private void scrollToStart() {
        this.js.executeScript("window.scrollTo(0, 0)");
    }

    protected void scrollToEndPage() {
        this.js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    protected String waitForWindow(int timeout) {
        wait(timeout);
        Set<String> whNow = driver.getWindowHandles();
        for (String windowHandle : whNow) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                return windowHandle;
            }
        }
        return null;
    }

    protected void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void goToPilaga() {
        driver.get(INSTANCE.pilagaUrl);
        this.originalWindow = driver.getWindowHandle();
    }

    public void loginPilaga(User user) {
        WebElement userInput = driver.findElement(By.id(INSTANCE.loginUserInput));
        userInput.click();
        userInput.sendKeys(user.getPilagaUser());
        WebElement passwordInput = driver.findElement(By.id(INSTANCE.loginPasswordInput));
        passwordInput.click();
        passwordInput.sendKeys(user.getPilagaPassword());
        driver.findElement(By.cssSelector(INSTANCE.cssLoginButton)).click();
        try {
            driver.findElement(By.cssSelector(INSTANCE.cssLoginOtherEntity)).click();
        } catch (NoSuchElementException ignored) {
        }
        try {
            driver.findElement(By.id(INSTANCE.errorButton)).click();
            JOptionPane.showMessageDialog(null, "El usuario y/o clave de pilagá son incorrectos");
            System.exit(0);
        } catch (NoSuchElementException ignored) {
        }
        wait(INSTANCE.waitingTimeAfterLogin);
        this.js.executeScript("document.body.style.zoom='70%'");
    }

    public void liquidate(Group group) {
        for (Liquidation liquidation : group.getLiquidaciones()) {
            System.out.println("Liquidando " + liquidation.getCompromiso().toString());
            System.out.println("Total amount: " + liquidation.getTotalAmount());
            if (liquidation.getOp() == null) {
                lookForCompromiso(liquidation.getCompromiso());
                wait(INSTANCE.waitingTimeAfterLookForCompromiso);
                int index = findIndex(liquidation.getCompromiso());
                if (index == -1) {
                    liquidation.setResultLiquidacion(liquidation.getCompromisoNotFoundMessage());
                    otherFilter();
                    continue;
                }
                if (!isThereEnoughBalance(liquidation, index)) {
                    otherFilter();
                    continue;
                }
                selectRow(index);
                wait(INSTANCE.waitingTimeAfterRowSelection);
                completeData(group);
                try {
                    completeSpecificData(liquidation);
                } catch (NotMatchBeneficiary e) {
                    liquidation.setResultLiquidacion(e.getMessage());
                    scrollToEndPage();
                    driver.findElement(By.id(INSTANCE.cancelNew)).click();
                    otherFilter();
                    continue;
                }
                wait(INSTANCE.waitingTimeToCompletePartidas);
                completePartidas(liquidation);
                finishOP(liquidation);
                wait(INSTANCE.waitingTimeAfterFinishOp);
                otherFilter();
            }

            if (liquidation.getOp() != null) {
                liquidation.setResultLiquidacion(liquidation.getSuccessLiquidationMessage());
                group.addLiquidacionLiquidada(liquidation);
            }
        }
    }

    private void lookForCompromiso(Compromiso compromiso) {
        WebElement document = driver.findElement(By.id(String.format(INSTANCE.documentInput, getForm1())));
        document.click();
        document.sendKeys(compromiso.getType());
        driver.findElement(By.id(String.format(INSTANCE.documentNumberInput, getForm1())))
                .sendKeys(String.valueOf(compromiso.getNumber()));
        driver.findElement(By.id(String.format(INSTANCE.documentYearInput, getForm1())))
                .sendKeys(String.valueOf(compromiso.getYear()));
        driver.findElement(By.id(INSTANCE.tobaForm)).click();
        driver.findElement(By.id(String.format(INSTANCE.filterButton, getForm1()))).click();
    }

    private int findIndex(Compromiso compromiso) {
        List<WebElement> filas = driver.findElements(By.cssSelector(INSTANCE.cssElementsFound));
        System.out.println("Filas: " + filas.size());
        if (!filas.isEmpty()) {
            for (int i = 2; i < filas.size() + 2; i++) {
                String nuiFound = driver.findElement(By.cssSelector(String.format(INSTANCE.cssCompromisosFoundRow, i))).getText();
                System.out.println("NUI encontrado: " + nuiFound);
                System.out.println("NUI buscado: " + compromiso.toString());
                if (nuiFound.equals(compromiso.toString())) {
                    System.out.println("Encontrado");
                    return i;
                }
            }
        }
        System.out.println("No encontrado");
        return -1;
    }

    private boolean isThereEnoughBalance(Liquidation liquidation, int index) {

        double available = Validations.numberFormat(driver.findElement(By
                        .cssSelector(String.format(INSTANCE.cssCompromisoRow, index)))
                .getText());

        if (available < liquidation.getTotalAmount()) {
            liquidation.setResultLiquidacion(liquidation.getNotEnoughMoneyMessage(available));
            return false;
        }
        return true;
    }

    private void selectRow(int index) {
        driver.findElement(By.id(String.format(INSTANCE.rowSelection, index - 2))).click();
    }

    private void completeData(Group group) {
        WebElement opType = driver.findElement(By.id(INSTANCE.opTypeInput));
        opType.click();
        opType.sendKeys(group.getTypeOP());
        WebElement fileType = driver.findElement(By.id(INSTANCE.fileTypeInput));
        fileType.click();
        fileType.sendKeys(group.getFile().getType());
        WebElement fileNumber = driver.findElement(By.id(INSTANCE.fileNumberInput));
        fileNumber.clear();
        fileNumber.sendKeys(String.valueOf(group.getFile().getNumber()));
        WebElement fileYear = driver.findElement(By.id(INSTANCE.fileYearInput));
        fileYear.clear();
        fileYear.sendKeys(String.valueOf(group.getFile().getYear()));
    }

    private void completePartidas(Liquidation liquidation) {

        List<WebElement> rowsPartidas = driver.findElements(By
                .cssSelector(String.format(INSTANCE.cssPartidasRows, getForm2())));

        double leftAmount = liquidation.getTotalAmount();

        for (int i = 0; i < rowsPartidas.size(); i++) {

            double rowAvailable = Validations.numberFormat(driver.
                    findElement(By.id(String.format(INSTANCE.availablePartidaRow, i, getForm2())))
                    .getAttribute("value"));

            WebElement amountPartidaRow = driver.findElement(By.id(String.format(INSTANCE.amountPartidaRow, i, getForm2())));
            amountPartidaRow.clear();

            if (rowAvailable >= leftAmount) {
                amountPartidaRow.sendKeys(String.valueOf(leftAmount));
                leftAmount = 0;
                break;
            } else {
                amountPartidaRow.sendKeys(String.valueOf(rowAvailable));
                leftAmount -= rowAvailable;
                leftAmount = Math.round(leftAmount * 100.0) / 100.0;
            }
        }
    }

    private void finishOP(Liquidation liquidation) {
        driver.findElement(By.id(INSTANCE.tobaForm)).click();
        scrollToEndPage();
        driver.findElement(By.id(String.format(INSTANCE.processButton, getForm3()))).click();

        try {
            String error = driver.findElement(By.cssSelector(INSTANCE.cssErrorProcess)).getText().replace("\\n", "").replace("\\", "");
            driver.findElement(By.id(INSTANCE.errorButton)).click();
            liquidation.setResultLiquidacion(liquidation.getErrorLiquitationMessage(error));
            scrollToEndPage();
            driver.findElement(By.id(String.format(INSTANCE.cancelButton, getForm3()))).click();
            return;
        } catch (NoSuchElementException ignored) {
        }

        wait(INSTANCE.waitingTimeAfterProcessLiquidation);

        String opGenerated = driver.findElement(By.id(INSTANCE.mainDoc)).getText();
        String typeOP = opGenerated.substring(3, 7);
        String numberOP = opGenerated.substring(8, opGenerated.indexOf("/"));
        String yearOP = opGenerated.substring(opGenerated.indexOf("/") + 1);

        Validations.isNumber(numberOP);
        Validations.isYear(yearOP);
        OP op = new OP();
        op.setType(typeOP);
        op.setNumber(Integer.parseInt(numberOP));
        op.setYear(Integer.parseInt(yearOP));

        liquidation.setOp(op);
        scrollToEndPage();
        driver.findElement(By.id(String.format(INSTANCE.finishButton, getForm3()))).click();
    }

    private void otherFilter() {
        driver.findElement(By.id(String.format(INSTANCE.collapseFilterButton, getForm1()))).click();
        driver.findElement(By.id(String.format(INSTANCE.documentNumberInput, getForm1()))).clear();
        driver.findElement(By.id(String.format(INSTANCE.documentYearInput, getForm1()))).clear();
    }

    public void createGroup(Group group) {

        goToCreateGroup();
        completeDataFileCreateGroup(group);
        markCvuCheckIfNeeded();

        scrollToEndPage();
        driver.findElement(By.id(INSTANCE.liquidationFilterGroupButton)).click();
        wait(INSTANCE.waitingTimeAfterFiltering);
        driver.findElement(By.id(INSTANCE.collapseFilterGroupButton)).click();
        try {
            driver.findElement(By.id(INSTANCE.groupNameInput)).sendKeys(group.getGroupName());
            driver.findElement(By.id(INSTANCE.selectAllGroupCheck)).click();
            driver.findElement(By.id(INSTANCE.addLiquidationToGroup)).click();
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "No existen liquidaciones en nivel 4 para realizar el grupo");
            System.exit(0);
        }
        try {
            driver.findElement(By.id(INSTANCE.errorButton)).click();
            JOptionPane.showMessageDialog(null, "El grupo ya se encuentra creado");
            System.exit(0);
        } catch (NoSuchElementException ignored) {
        }
    }

    private void goToCreateGroup() {
        driver.findElement(By.id(INSTANCE.rootButton)).click();
        driver.findElement(By.id(INSTANCE.searchInput)).sendKeys("grupo");
        driver.findElement(By.id(INSTANCE.newGroupButton)).click();
        wait(INSTANCE.waitingTimeAfterProcessLiquidation);
    }

    private void completeDataFileCreateGroup(Group group) {
        WebElement opTypeInput = driver.findElement(By.id(INSTANCE.typeOpGroupInput));
        opTypeInput.click();
        opTypeInput.sendKeys(group.getTypeOP());
        WebElement fileTypeInput = driver.findElement(By.id(INSTANCE.fileTypeGroupInput));
        fileTypeInput.click();
        fileTypeInput.sendKeys(group.getFile().getType());
        driver.findElement(By.id(INSTANCE.fileNumberGroupInput)).sendKeys(String.valueOf(group.getFile().getNumber()));
        driver.findElement(By.id(INSTANCE.fileYearGroupInput)).sendKeys(String.valueOf(group.getFile().getYear()));
        WebElement opLevelInput = driver.findElement(By.id(INSTANCE.levelFromGroupInput));
        opLevelInput.click();
        opLevelInput.sendKeys("Nivel 4");
    }

    public void goToLevel4GroupAuthorization() {
        driver.findElement(By.id(INSTANCE.rootButton)).click();
        driver.findElement(By.id(INSTANCE.searchInput)).sendKeys("grupo");
        driver.findElement(By.id(INSTANCE.level4GroupAuthorization)).click();
        wait(INSTANCE.waitingTimeAfterProcessLiquidation);
    }

    public void goToLevel7GroupAuthorization() {
        driver.findElement(By.id(INSTANCE.rootButton)).click();
        driver.findElement(By.id(INSTANCE.searchInput)).sendKeys("grupo");
        driver.findElement(By.id(INSTANCE.level7GroupAuthorization)).click();
        wait(INSTANCE.waitingTimeAfterProcessLiquidation);
    }

    public void filterGroup(Group group) {
        driver.findElement(By.id(INSTANCE.groupSelectionAuthorizationInput)).click();
        vars.put("groupWindow", waitForWindow(INSTANCE.waitingTimeToFilterGroupLiquidationsLevel4));
        driver.switchTo().window(vars.get("groupWindow").toString());

        driver.findElement(By.id(INSTANCE.groupNameInputSelectionGroupWindow)).sendKeys(group.getGroupName());
        driver.findElement(By.id(INSTANCE.filterGroupWindowButton)).click();
        driver.findElement(By.id(INSTANCE.selectGroupWindowButton)).click();

        driver.switchTo().window(originalWindow);
        scrollToEndPage();
        driver.findElement(By.id(INSTANCE.filterButtonLevel4GroupAuthorization)).click();
    }

    public boolean sameTotal(Group group, char level) {

        scrollToEndPage();
        double totalPilaga = Validations.numberFormat(driver.findElement(By.cssSelector(INSTANCE.cssLevelTotal)).getText());
        double montoGrupo = 0;
        List<Liquidation> liquidaciones;
        if (level == '4') {
            liquidaciones = group.getLiquidacionesARetener();
        } else {
            liquidaciones = group.getLiquidacionesRetenidas();
        }

        for (Liquidation liquidation : liquidaciones) {
            montoGrupo += liquidation.getTotalAmount();
        }

        if (totalPilaga != montoGrupo && Math.abs(totalPilaga - montoGrupo) >= 0.01) {
            scrollToStart();
            return false;
        }
        scrollToStart();
        return true;
    }

    public void differences(Group group) {

        goToCreateGroup();
        completeDataFileCreateGroup(group);
        scrollToEndPage();
        driver.findElement(By.id(INSTANCE.liquidationFilterGroupButton)).click();
        driver.findElement(By.id(INSTANCE.collapseFilterGroupButton)).click();

        List<Integer> op = new ArrayList<>();

        List<WebElement> opRows = driver.findElements(By.cssSelector(INSTANCE.cssElementsFound));

        for (int i = 0; i < opRows.size(); i++) {
            boolean found = false;
            while (!found) {
                try {
                    String opNumber = driver.findElement(By.cssSelector(String.format(INSTANCE.cssOpColumnLevel4GroupAuthorization, i + 2))).getText();
                    found = true;
                    opNumber = opNumber.substring(5, opNumber.indexOf("/"));
                    op.add(Integer.parseInt(opNumber));
                } catch (Exception e) {
                    scrollTo(20);
                }
            }
        }

        for (Liquidation liquidation : group.getLiquidacionesLiquidadas()) {
            if (op.contains(liquidation.getOp().getNumber())) {
                liquidation.setResultAutorizacion("La liquidacion no se incluye en el grupo por no tener el beneficiario CBU activa");
                group.getLiquidacionesARetener().remove(liquidation);
            }
        }
    }


    protected void makeGroupRetention() {

        try {
            driver.findElement(By.id(INSTANCE.selectAllLevel4Group)).click();
            scrollToEndPage();
            driver.findElement(By.id(INSTANCE.processLevel4AuthorizationGroup)).click();
            wait(500);
            driver.findElement(By.id(INSTANCE.errorButton)).click();
        } catch (NoSuchElementException ignored) {
        }

    }

    public void goToLiquidationsList(Group group) {
        driver.findElement(By.id(INSTANCE.rootButton)).click();
        driver.findElement(By.id(INSTANCE.searchInput)).sendKeys("listado de liquidaciones");
        driver.findElement(By.id(INSTANCE.liquidationList)).click();
        wait(INSTANCE.waitingTimeAfterProcessLiquidation);

        WebElement fileType = driver.findElement(By.id(INSTANCE.fileTypeLiquidationList));
        fileType.click();
        fileType.sendKeys(group.getFile().getType());
        driver.findElement(By.id(INSTANCE.fileYearLiquidationList)).sendKeys(String.valueOf(group.getFile().getYear()));
        driver.findElement(By.id(INSTANCE.fileNumberLiquidationList)).sendKeys(String.valueOf(group.getFile().getNumber()));
        scrollToEndPage();
        driver.findElement(By.id(INSTANCE.filterButtonLiquidationList)).click();
    }
}
