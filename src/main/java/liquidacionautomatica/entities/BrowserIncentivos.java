package liquidacionautomatica.entities;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static liquidacionautomatica.HtmlElement.*;
import static liquidacionautomatica.HtmlElement.ROOT_NEW_DEVENGADO_BECAS;

@Getter
public class BrowserIncentivos extends Browser {

    private final String typeOp = "OP36";
    protected final String form1 = "1559";
    protected final String form2 = "15000119";
    protected final String form3 = "1521";

    @Override
    public void goToNew() {
        driver.findElement(By.id(ROOT_BUTTON)).click();
        driver.findElement(By.id(ROOT_EXPENSES)).click();
        driver.findElement(By.id(ROOT_BECAS)).click();
        driver.findElement(By.id(ROOT_DEVENGADO_BECAS)).click();
        driver.findElement(By.id(ROOT_DEPENDENCY_DEVENGADO_BECAS)).click();
        driver.findElement(By.id(ROOT_NEW_DEVENGADO_BECAS)).click();
        wait(WAITING_TIME_AFTER_SCREEN_SELECTION);
    }

    @Override
    protected void completeSpecificData(Liquidation liquidation) {
        driver.findElement(By.id(CHANGE_TAB_1_INCENTIVOS)).click();
        wait(WAITING_TIME_TO_CHANGE_TAB);
        WebElement descriptionIncentivosInput = driver.findElement(By.id(DESCRIPTION_INCENTIVOS_INPUT));
        String newDescripcion = descriptionIncentivosInput.getText() + " " + liquidation.getDescription();
        descriptionIncentivosInput.clear();
        descriptionIncentivosInput.sendKeys(newDescripcion);
        WebElement becaPlanInput = driver.findElement(By.id(BECA_PLAN_INPUT));
        becaPlanInput.click();
        becaPlanInput.sendKeys("ORD");
        driver.findElement(By.id(BECA_DEPENDENCY_INPUT)).click();

        vars.put("dependencyWindow", waitForWindow(WAITING_TIME_TO_OPEN_DEPENDENCY_WINDOW));
        driver.switchTo().window(vars.get("dependencyWindow").toString());
        WebElement nameFilterInput = driver.findElement(By.id(NAME_FILTER_INPUT));
        nameFilterInput.click();
        nameFilterInput.sendKeys(((Liquidation36) liquidation).getDependency());
//        driver.findElement(By.cssSelector("u")).click();
        driver.findElement(By.cssSelector(CSS_DEPENDENCY_SELECTION)).click();
        driver.switchTo().window(originalWindow);
        vars.remove("dependencyWindow");

        driver.findElement(By.id(CHANGE_TAB_2_INCENTIVOS)).click();
    }

    @Override
    protected void markCvuCheckIfNeeded() {
    }

    @Override
    public void authorizeLevel4(Group group, Double amount2) {
        goToLevel4GroupAuthorization();
        filterGroup(group);
        makeGroupRetention();
        group.addRetenidasResto();
    }
}
