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
        driver.get(PILAGA_URL);
        this.originalWindow = driver.getWindowHandle();
    }

    public void loginPilaga(User user) {
        WebElement userInput = driver.findElement(By.id(LOGIN_USER_INPUT));
        userInput.click();
        userInput.sendKeys(user.getPilagaUser());
        WebElement passwordInput = driver.findElement(By.id(LOGIN_PASSWORD_INPUT));
        passwordInput.click();
        passwordInput.sendKeys(user.getPilagaPassword());
        driver.findElement(By.cssSelector(CSS_LOGIN_BUTTON)).click();
        try {
            driver.findElement(By.cssSelector(CSS_LOGIN_OTHER_ENTITY)).click();
        } catch (NoSuchElementException ignored) {
        }
        try {
            driver.findElement(By.id(ERROR_BUTTON)).click();
            JOptionPane.showMessageDialog(null, "El usuario y/o clave de pilagá son incorrectos");
            System.exit(0);
        } catch (NoSuchElementException ignored) {
        }
        wait(WAITING_TIME_AFTER_LOGIN);
        this.js.executeScript("document.body.style.zoom='70%'");
    }

    public void liquidate(Group group) {
        for (Liquidation liquidation : group.getLiquidaciones()) {
            System.out.println("Liquidando " + liquidation.getCompromiso().toString());
            System.out.println("Total amount: " + liquidation.getTotalAmount());
            if (liquidation.getOp() == null) {
                lookForCompromiso(liquidation.getCompromiso());
                wait(WAITING_TIME_AFTER_LOOK_FOR_COMPROMISO);
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
                wait(WAITING_TIME_AFTER_ROW_SELECTION);
                completeData(group);
                try {
                    completeSpecificData(liquidation);
                } catch (NotMatchBeneficiary e) {
                    liquidation.setResultLiquidacion(e.getMessage());
                    scrollToEndPage();
                    driver.findElement(By.id(CANCEL_NEW)).click();
                    otherFilter();
                    continue;
                }
                wait(WAITING_TIME_TO_COMPLETE_PARTIDAS);
                completePartidas(liquidation);
                finishOP(liquidation);
                wait(WAITING_TIME_AFTER_FINISH_OP);
                otherFilter();
            }

            if (liquidation.getOp() != null) {
                liquidation.setResultLiquidacion(liquidation.getSuccessLiquidationMessage());
                group.addLiquidacionLiquidada(liquidation);
                // todo: ver si se puede agregar la op en el excel
            }
        }
    }

    private void lookForCompromiso(Compromiso compromiso) {
        WebElement document = driver.findElement(By.id(String.format(DOCUMENT_INPUT, getForm1())));
        document.click();
        document.sendKeys(compromiso.getType());
        driver.findElement(By.id(String.format(DOCUMENT_NUMBER_INPUT, getForm1())))
                .sendKeys(String.valueOf(compromiso.getNumber()));
        driver.findElement(By.id(String.format(DOCUMENT_YEAR_INPUT, getForm1())))
                .sendKeys(String.valueOf(compromiso.getYear()));
        driver.findElement(By.id(TOBA_FORM)).click();
        driver.findElement(By.id(String.format(FILTER_BUTTON, getForm1()))).click();
    }

    private int findIndex(Compromiso compromiso) {
        List<WebElement> filas = driver.findElements(By.cssSelector(CSS_ELEMENTS_FOUND));
        System.out.println("Filas: " + filas.size());
        if (!filas.isEmpty()) {
            for (int i = 2; i < filas.size() + 2; i++) {
                String nuiFound = driver.findElement(By.cssSelector(String.format(CSS_COMPROMISOS_FOUND_ROW, i))).getText();
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
                        .cssSelector(String.format(CSS_COMPROMISO_ROW, index)))
                .getText());

        if (available < liquidation.getTotalAmount()) {
            liquidation.setResultLiquidacion(liquidation.getNotEnoughMoneyMessage(available));
            return false;
        }
        return true;
    }

    private void selectRow(int index) {
        driver.findElement(By.id(String.format(ROW_SELECTION, index - 2))).click();
    }

    private void completeData(Group group) {
        WebElement opType = driver.findElement(By.id(OP_TYPE_INPUT));
        opType.click();
        opType.sendKeys(group.getTypeOP());
        WebElement fileType = driver.findElement(By.id(FILE_TYPE_INPUT));
        fileType.click();
        fileType.sendKeys(group.getFile().getType());
        WebElement fileNumber = driver.findElement(By.id(FILE_NUMBER_INPUT));
        fileNumber.clear();
        fileNumber.sendKeys(String.valueOf(group.getFile().getNumber()));
        WebElement fileYear = driver.findElement(By.id(FILE_YEAR_INPUT));
        fileYear.clear();
        fileYear.sendKeys(String.valueOf(group.getFile().getYear()));
    }

    private void completePartidas(Liquidation liquidation) {

        List<WebElement> rowsPartidas = driver.findElements(By
                .cssSelector(String.format(CSS_PARTIDAS_ROWS, getForm2())));

        double leftAmount = liquidation.getTotalAmount();

        for (int i = 0; i < rowsPartidas.size(); i++) {

            double rowAvailable = Validations.numberFormat(driver.
                    findElement(By.id(String.format(AVAILABLE_PARTIDA_ROW, i, getForm2())))
                    .getAttribute("value"));

            WebElement amountPartidaRow = driver.findElement(By.id(String.format(AMOUNT_PARTIDA_ROW, i, getForm2())));
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
        driver.findElement(By.id(TOBA_FORM)).click();
        scrollToEndPage();
        driver.findElement(By.id(String.format(PROCESS_BUTTON, getForm3()))).click();

        try {
            String error = driver.findElement(By.cssSelector(CSS_ERROR_PROCESS)).getText().replace("\\n", "").replace("\\", "");
            driver.findElement(By.id(ERROR_BUTTON)).click();
            liquidation.setResultLiquidacion(liquidation.getErrorLiquitationMessage(error));
            scrollToEndPage();
            driver.findElement(By.id(String.format(CANCEL_BUTTON, getForm3()))).click();
            return;
        } catch (NoSuchElementException ignored) {
        }

        wait(WAITING_TIME_AFTER_PROCESS_LIQUIDATION);

        String opGenerated = driver.findElement(By.id(MAIN_DOC)).getText();
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
        driver.findElement(By.id(String.format(FINISH_BUTTON, getForm3()))).click();
    }

    private void otherFilter() {
        driver.findElement(By.id(String.format(COLLAPSE_FILTER_BUTTON, getForm1()))).click();
        driver.findElement(By.id(String.format(DOCUMENT_NUMBER_INPUT, getForm1()))).clear();
        driver.findElement(By.id(String.format(DOCUMENT_YEAR_INPUT, getForm1()))).clear();
    }

    public void createGroup(Group group) {

        goToCreateGroup();
        completeDataFileCreateGroup(group);
        markCvuCheckIfNeeded();

        scrollToEndPage();
        driver.findElement(By.id(LIQUIDATION_FILTER_GROUP_BUTTON)).click();
        wait(WAITING_TIME_AFTER_FILTERING);
        driver.findElement(By.id(COLLAPSE_FILTER_GROUP_BUTTON)).click();
        try {
            driver.findElement(By.id(GROUP_NAME_INPUT)).sendKeys(group.getGroupName());
            driver.findElement(By.id(SELECT_ALL_GROUP_CHECK)).click();
            driver.findElement(By.id(ADD_LIQUIDATION_TO_GROUP)).click();
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, "No existen liquidaciones en nivel 4 para realizar el grupo");
            System.exit(0);
        }
        try {
            driver.findElement(By.id(ERROR_BUTTON)).click();
            JOptionPane.showMessageDialog(null, "El grupo ya se encuentra creado");
            System.exit(0);
        } catch (NoSuchElementException ignored) {
        }
    }

    private void goToCreateGroup() {
        driver.findElement(By.id(ROOT_BUTTON)).click();
        driver.findElement(By.id(SEARCH_INPUT)).sendKeys("grupo");
        driver.findElement(By.id(NEW_GROUP_BUTTON)).click();
        wait(WAITING_TIME_AFTER_PROCESS_LIQUIDATION);
    }

    private void completeDataFileCreateGroup(Group group) {
        WebElement opTypeInput = driver.findElement(By.id(TYPE_OP_GROUP_INPUT));
        opTypeInput.click();
        opTypeInput.sendKeys(group.getTypeOP());
        WebElement fileTypeInput = driver.findElement(By.id(FILE_TYPE_GROUP_INPUT));
        fileTypeInput.click();
        fileTypeInput.sendKeys(group.getFile().getType());
        driver.findElement(By.id(FILE_NUMBER_GROUP_INPUT)).sendKeys(String.valueOf(group.getFile().getNumber()));
        driver.findElement(By.id(FILE_YEAR_GROUP_INPUT)).sendKeys(String.valueOf(group.getFile().getYear()));
        WebElement opLevelInput = driver.findElement(By.id(LEVEL_FROM_GROUP_INPUT));
        opLevelInput.click();
        opLevelInput.sendKeys("Nivel 4");
    }

    public void goToLevel4GroupAuthorization() {
        driver.findElement(By.id(ROOT_BUTTON)).click();
        driver.findElement(By.id(SEARCH_INPUT)).sendKeys("grupo");
        driver.findElement(By.id(LEVEL_4_GROUP_AUTHORIZATION)).click();
        wait(WAITING_TIME_AFTER_PROCESS_LIQUIDATION);
    }

    public void goToLevel7GroupAuthorization() {
        driver.findElement(By.id(ROOT_BUTTON)).click();
        driver.findElement(By.id(SEARCH_INPUT)).sendKeys("grupo");
        driver.findElement(By.id(LEVEL_7_GROUP_AUTHORIZATION)).click();
        wait(WAITING_TIME_AFTER_PROCESS_LIQUIDATION);
    }

    public void filterGroup(Group group) {
        driver.findElement(By.id(GROUP_SELECTION_AUTHORIZATION_INPUT)).click();
        vars.put("groupWindow", waitForWindow(WAITING_TIME_TO_FILTER_GROUP_LIQUIDATIONS_LEVEL_4));
        driver.switchTo().window(vars.get("groupWindow").toString());

        driver.findElement(By.id(GROUP_NAME_INPUT_SELECTION_GROUP_WINDOW)).sendKeys(group.getGroupName());
        driver.findElement(By.id(FILTER_GROUP_WINDOW_BUTTON)).click();
        driver.findElement(By.id(SELECT_GROUP_WINDOW_BUTTON)).click();

        driver.switchTo().window(originalWindow);
        scrollToEndPage();
        driver.findElement(By.id(FILTER_BUTTON_LEVEL_4_GROUP_AUTHORIZATION)).click();
    }

    public boolean sameTotal(Group group, char level) {

        scrollToEndPage();
        double totalPilaga = Validations.numberFormat(driver.findElement(By.cssSelector(CSS_LEVEL_TOTAL)).getText());
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
        driver.findElement(By.id(LIQUIDATION_FILTER_GROUP_BUTTON)).click();
        driver.findElement(By.id(COLLAPSE_FILTER_GROUP_BUTTON)).click();

        List<Integer> op = new ArrayList<>();

        List<WebElement> opRows = driver.findElements(By.cssSelector(CSS_ELEMENTS_FOUND));

        for (int i = 0; i < opRows.size(); i++) {
            boolean found = false;
            while (!found) {
                try {
                    String opNumber = driver.findElement(By.cssSelector(String.format(CSS_OP_COLUMN_LEVEL_4_GROUP_AUTHORIZATION, i + 2))).getText();
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
            driver.findElement(By.id(SELECT_ALL_LEVEL_4_GROUP)).click();
            scrollToEndPage();
            driver.findElement(By.id(PROCESS_LEVEL_4_AUTHORIZATION_GROUP)).click();
            wait(500);
            driver.findElement(By.id(ERROR_BUTTON)).click();
        } catch (NoSuchElementException ignored) {
        }

    }

    public void goToLiquidationsList(Group group) {
        driver.findElement(By.id(ROOT_BUTTON)).click();
        driver.findElement(By.id(SEARCH_INPUT)).sendKeys("listado de liquidaciones");
        driver.findElement(By.id(LIQUIDATION_LIST)).click();
        wait(WAITING_TIME_AFTER_PROCESS_LIQUIDATION);

        WebElement fileType = driver.findElement(By.id(FILE_TYPE_LIQUIDATION_LIST));
        fileType.click();
        fileType.sendKeys(group.getFile().getType());
        driver.findElement(By.id(FILE_YEAR_LIQUIDATION_LIST)).sendKeys(String.valueOf(group.getFile().getYear()));
        driver.findElement(By.id(FILE_NUMBER_LIQUIDATION_LIST)).sendKeys(String.valueOf(group.getFile().getNumber()));
        scrollToEndPage();
        driver.findElement(By.id(FILTER_BUTTON_LIQUIDATION_LIST)).click();
    }
}
