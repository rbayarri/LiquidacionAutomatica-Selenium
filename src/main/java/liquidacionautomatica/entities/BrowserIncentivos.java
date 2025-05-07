package liquidacionautomatica.entities;

import liquidacionautomatica.HtmlElement;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Getter
public class BrowserIncentivos extends Browser {

    private final String typeOp = "OP36";
    protected final String form1 = "1559";
    protected final String form2 = "15000119";
    protected final String form3 = "1521";

    @Override
    public void goToNew() {
        driver.findElement(By.id(HtmlElement.INSTANCE.rootButton)).click();
        driver.findElement(By.id(HtmlElement.INSTANCE.rootExpenses)).click();
        driver.findElement(By.id(HtmlElement.INSTANCE.rootBecas)).click();
        driver.findElement(By.id(HtmlElement.INSTANCE.rootDevengadoBecas)).click();
        driver.findElement(By.id(HtmlElement.INSTANCE.rootDependencyDevengadoBecas)).click();
        driver.findElement(By.id(HtmlElement.INSTANCE.rootNewDevengadoBecas)).click();
        wait(HtmlElement.INSTANCE.waitingTimeAfterScreenSelection);
    }

    @Override
    protected void completeSpecificData(Liquidation liquidation) {
        driver.findElement(By.id(HtmlElement.INSTANCE.changeTab1Incentivos)).click();
        wait(HtmlElement.INSTANCE.waitingTimeToChangeTab);
        WebElement descriptionIncentivosInput = driver.findElement(By.id(HtmlElement.INSTANCE.descriptionIncentivosInput));
        String newDescripcion = descriptionIncentivosInput.getText() + " " + liquidation.getDescription();
        descriptionIncentivosInput.clear();
        descriptionIncentivosInput.sendKeys(newDescripcion);
        WebElement becaPlanInput = driver.findElement(By.id(HtmlElement.INSTANCE.becaPlanInput));
        becaPlanInput.click();
        becaPlanInput.sendKeys("ORD");
        driver.findElement(By.id(HtmlElement.INSTANCE.becaDependencyInput)).click();

        vars.put("dependencyWindow", waitForWindow(HtmlElement.INSTANCE.waitingTimeToOpenDependencyWindow));
        driver.switchTo().window(vars.get("dependencyWindow").toString());
        WebElement nameFilterInput = driver.findElement(By.id(HtmlElement.INSTANCE.nameFilterInput));
        nameFilterInput.click();
        nameFilterInput.sendKeys(((Liquidation36) liquidation).getDependency());
//        driver.findElement(By.cssSelector("u")).click();
        driver.findElement(By.cssSelector(HtmlElement.INSTANCE.cssDependencySelection)).click();
        driver.switchTo().window(originalWindow);
        vars.remove("dependencyWindow");

        driver.findElement(By.id(HtmlElement.INSTANCE.changeTab2Incentivos)).click();
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
