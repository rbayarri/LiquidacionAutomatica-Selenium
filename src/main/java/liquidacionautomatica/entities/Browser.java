/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import liquidacionautomatica.exceptions.NotMatchBeneficiary;
import liquidacionautomatica.validations.Validations;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 *
 * @author rbayarri
 */
public class Browser {

  private WebDriver driver = null;
  private JavascriptExecutor js = null;
  private String originalWindow = null;
  private final Map<String, Object> vars = new HashMap<>();

  public Browser() {
  }

  public void open() {
    try {
      System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");
      ChromeOptions options = new ChromeOptions()
              .setBinary("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");
      this.driver = new ChromeDriver(options);

    } catch (Exception e) {
      try {
        ChromeOptions options = new ChromeOptions()
                .setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");

        this.driver = new ChromeDriver(options);
      } catch (Exception ex) {
        try {
          ChromeOptions options = new ChromeOptions()
                  .setBinary("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe");

          this.driver = new ChromeDriver(options);
        } catch (Exception exc) {

          System.setProperty("webdriver.firefox.driver", ".\\drivers\\geckodriver.exe");
          FirefoxOptions options = new FirefoxOptions()
                  .setBinary("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");
          this.driver = new FirefoxDriver(options);

        }
      }
    }

    this.driver.manage()
            .window().maximize();

    this.js = (JavascriptExecutor) driver;
  }

  public void scrollTo(int y) {
    this.js.executeScript("window.scrollTo(0,window.scrollY+" + y);
  }

  public void scrollToStart() {
    this.js.executeScript("window.scrollTo(0, 0)");
  }

  public void scrollToEndPage() {
    this.js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
  }

  public void goToPilaga() {
    driver.get("http://pilaga.intranet.uncu.edu.ar/siu/pilaga/");
    this.originalWindow = driver.getWindowHandle();
  }

  public void loginPilaga(User user) {
    driver.findElement(By.id("ef_form_2000616_datosusuario")).click();
    driver.findElement(By.id("ef_form_2000616_datosusuario")).sendKeys(user.getPilagaUser());
    driver.findElement(By.id("ef_form_2000616_datosclave")).click();
    driver.findElement(By.id("ef_form_2000616_datosclave")).sendKeys(user.getPilagaPassword());
    driver.findElement(By.cssSelector("#form_2000616_datos_ingresar > span")).click();
    try {
      driver.findElement(By.cssSelector("#cuadro_2000580_seleccion_unidad_gestion0_seleccion img")).click();
    } catch (Exception e) {
    }
    try {
      driver.findElement(By.id("boton_overlay")).click();
      JOptionPane.showMessageDialog(null, "El usuario y/o clave de pilagá son incorrectos");
      System.exit(0);
    } catch (Exception e) {
    }
  }

  public void goToDevengadoCompras() {
    driver.findElement(By.id("boton_menu_raiz")).click();
    driver.findElement(By.id("elemento_menu_3435")).click();
    driver.findElement(By.id("elemento_menu_3440")).click();
    driver.findElement(By.id("elemento_menu_3442")).click();
    driver.findElement(By.id("elemento_menu_3334")).click();
  }

  public void goToDevengadoBecas() {
    driver.findElement(By.id("boton_menu_raiz")).click();
    driver.findElement(By.id("elemento_menu_3435")).click();
    driver.findElement(By.id("elemento_menu_3476")).click();
    driver.findElement(By.id("elemento_menu_3480")).click();
    driver.findElement(By.id("elemento_menu_3482")).click();
    driver.findElement(By.id("elemento_menu_3479")).click();
  }

  public void liquidar(Group group, String form1, String form2, String form3, String type) {
    for (Liquidacion liquidacion : group.getLiquidaciones()) {
      if (liquidacion.getOp() == null) {
        lookForCompromiso(liquidacion.getCompromiso(), form1);
        int index = findIndex(liquidacion.getCompromiso());
        if (index == -1) {
          liquidacion.setResultLiquidacion(((LiquidacionContrato) liquidacion).getBeneficiary() + " - No se encuentra el " + liquidacion.getCompromiso().toString() + " o no tiene saldo");
          SendEmailTLS.sendMessage("Error en la liquidación en " + group.getGroupName(),
                  liquidacion.getResultLiquidacion(), false);
          otherFilter(form1);
          continue;
        }
        if (!isThereEnoughBalance(liquidacion, index)) {
          SendEmailTLS.sendMessage("Error en la liquidación en " + group.getGroupName(),
                  liquidacion.getResultLiquidacion(), false);
          otherFilter(form1);
          continue;
        }
        selectRow(index);
        completeGeneralData(group);
        if (type.equals("Contratos")) {
          try {
            completeDataContratos((LiquidacionContrato) liquidacion);
          } catch (NotMatchBeneficiary e) {
            liquidacion.setResultLiquidacion(e.getMessage());
            SendEmailTLS.sendMessage("Error en la liquidación del " + group.getGroupName(),
                    liquidacion.getResultLiquidacion(), false);
            scrollToEndPage();
            driver.findElement(By.id("ci_2035_cancelar")).click();
            otherFilter(form1);
            continue;
          }
        } else {
          completeDataOrd36((Liquidacion36) liquidacion);
        }
        completePartidas(liquidacion, form2);
        finishOP(liquidacion, form3, type, group.getGroupName());
        otherFilter(form1);
      }

      if (liquidacion.getOp() != null) {
        if (type.equals("Contratos")) {
          LiquidacionContrato lC = (LiquidacionContrato) liquidacion;
          String resultado = String.format("%-15s", lC.getOp().toString());
          resultado += " - " + lC.getBeneficiary();
          lC.setResultLiquidacion(resultado);
        } else {
          String resultado = String.format("%-15s", liquidacion.getCompromiso().toString());
          resultado += " - " + liquidacion.getOp().toString();
          liquidacion.setResultLiquidacion(resultado);
        }
        group.addLiquidacionLiquidada(liquidacion);
      }
    }
  }

  private void lookForCompromiso(Compromiso compromiso, String numberForm) {
    driver.findElement(By.id("ef_form_" + numberForm + "_filtro_refdocumento")).click();
    driver.findElement(By.id("ef_form_" + numberForm + "_filtro_refdocumento")).sendKeys(compromiso.getType());
    driver.findElement(By.id("ef_form_" + numberForm + "_filtro_refnumero"))
            .sendKeys(String.valueOf(compromiso.getNumber()));
    driver.findElement(By.id("ef_form_" + numberForm + "_filtro_refanio_doc"))
            .sendKeys(String.valueOf(compromiso.getYear()));
    driver.findElement(By.id("formulario_toba")).click();
    driver.findElement(By.id("form_" + numberForm + "_filtro_ref_filtrar")).click();
  }

  private int findIndex(Compromiso compromiso) {
    List<WebElement> filas = driver.findElements(By.cssSelector(".ei-cuadro-fila"));

    if (!filas.isEmpty()) {
      for (int i = 2; i < filas.size() + 2; i++) {
        if (driver.findElement(By.cssSelector(".ei-cuadro-fila:nth-child(" + i
                + ") > td:nth-child(2)"))
                .getText().equals(compromiso.toString())) {
          return i;
        }
      }
    }
    return -1;
  }

  private boolean isThereEnoughBalance(Liquidacion liquidacion, int index) {

    Double available = Validations.numberFormat(driver.findElement(By
            .cssSelector(".ei-cuadro-fila:nth-child(" + index + ") > td:nth-child(5)"))
            .getText());

    if (available < liquidacion.getTotalAmount()) {
      liquidacion.setResultLiquidacion(((LiquidacionContrato) liquidacion).getBeneficiary() + " - Saldo insuficiente en el "
              + liquidacion.getCompromiso().toString() + ". Disponible: " + available
              + ". A liquidar: " + liquidacion.getTotalAmount()
              + ". Diferencia: " + (liquidacion.getTotalAmount() - available));
      return false;
    }
    return true;
  }

  private void selectRow(int index) {
    driver.findElement(By.id("cuadro_1091_cuadro_ref" + (index - 2) + "_seleccion")).click();
  }

  private void completeGeneralData(Group group) {
    driver.findElement(By.id("ef_form_84000147_printipo")).click();
    driver.findElement(By.id("ef_form_84000147_printipo")).sendKeys(group.getTypeOP());
    driver.findElement(By.id("ef_form_84000146_conttipo")).click();
    driver.findElement(By.id("ef_form_84000146_conttipo")).sendKeys(group.getFile().getType());
    driver.findElement(By.id("ef_form_84000146_contnumero")).clear();
    driver.findElement(By.id("ef_form_84000146_contnumero"))
            .sendKeys(String.valueOf(group.getFile().getNumber()));
    driver.findElement(By.id("ef_form_84000146_contanio")).clear();
    driver.findElement(By.id("ef_form_84000146_contanio"))
            .sendKeys(String.valueOf(group.getFile().getYear()));
  }

  private void completeDataContratos(LiquidacionContrato liquidacion) throws NotMatchBeneficiary {

    driver.findElement(By.id("ci_84000215_paso_alta_cambiar_tab_1")).click();
    String newDescription = driver
            .findElement(By.id("ef_form_84000211_gestiondescripcion"))
            .getText() + " " + liquidacion.getDescription();
    driver.findElement(By.id("ef_form_84000211_gestiondescripcion")).clear();
    driver.findElement(By.id("ef_form_84000211_gestiondescripcion")).sendKeys(newDescription);

    String beneficiary = driver.findElement(By.id("ef_form_84000211_gestionproveedor_desc")).getAttribute("value");
    beneficiary = beneficiary.replace("-", "");
    if (!beneficiary.contains(liquidacion.getCUIT())) {
      throw new NotMatchBeneficiary("Los beneficiarios no coinciden. CUIT a liquidar: "
              + liquidacion.getCUIT() + ". CUIT del " + liquidacion.getCompromiso().toString()
              + ": " + beneficiary);
    }
    completeInvoices(liquidacion);
  }

  private void completeDataOrd36(Liquidacion36 liquidacion) {
    driver.findElement(By.id("ci_84000314_paso_alta_cambiar_tab_1")).click();

    String newDescripcion = driver
            .findElement(By.id("ef_form_1537_gestiondescripcion"))
            .getText() + " " + liquidacion.getDescription();
    driver.findElement(By.id("ef_form_1537_gestiondescripcion")).clear();
    driver.findElement(By.id("ef_form_1537_gestiondescripcion")).sendKeys(newDescripcion);
    driver.findElement(By.id("ef_form_1537_gestionplan_beca")).click();
    driver.findElement(By.id("ef_form_1537_gestionplan_beca")).sendKeys("ORD");
    driver.findElement(By.id("ef_form_1537_gestiondependencia_vinculo")).click();

    vars.put("dependencyWindow", waitForWindow(1000));
    driver.switchTo().window(vars.get("dependencyWindow").toString());
    driver.findElement(By.id("ef_form_1000115_pers_filtroup_nombre_n")).click();
    driver.findElement(By
            .id("ef_form_1000115_pers_filtroup_nombre_n")).sendKeys(liquidacion.getDependency());
    driver.findElement(By.cssSelector("u")).click();
    driver.findElement(By.cssSelector("#cuadro_1000112_pers_popup0_seleccion img")).click();
    driver.switchTo().window(originalWindow);
    vars.remove("dependencyWindow");

    driver.findElement(By.id("ci_84000314_paso_alta_cambiar_tab_2")).click();

  }

  private void completeInvoices(LiquidacionContrato liquidacion) {
    ArrayList<Invoice> invoices = liquidacion.getInvoices();
    driver.findElement(By.id("ci_84000215_paso_alta_cambiar_tab_2")).click();
    for (int i = 0; i < invoices.size(); i++) {
      driver.findElement(By.id("js_form_2045_formulario_compr_agregar")).click();
      driver.findElement(By.id((156 + i) + "_ef_form_2045_formulario_comprtipo_de_comprobante")).click();
      driver.findElement(By.id((156 + i) + "_ef_form_2045_formulario_comprtipo_de_comprobante"))
              .sendKeys(invoices.get(i).getType());
      driver.findElement(By.id((156 + i) + "_ef_form_2045_formulario_comprnumero")).sendKeys(invoices.get(i).getNumber());
      driver.findElement(By.id((156 + i) + "_ef_form_2045_formulario_comprfecha")).sendKeys(invoices.get(i).getDate());
      driver.findElement(By.id((156 + i) + "_ef_form_2045_formulario_comprimporte")).sendKeys(invoices.get(i).getAmount());
      driver.findElement(By.id((156 + i) + "_ef_form_2045_formulario_comprdetalle")).sendKeys(invoices.get(i).getDescription());

    }
    driver.findElement(By.id("ci_84000215_paso_alta_cambiar_tab_3")).click();
  }

  private void completePartidas(Liquidacion liquidacion, String form) {

    List<WebElement> rowsPartidas = driver.findElements(By
            .cssSelector("#cuerpo_js_form_" + form + "_ppg_alta > table > tbody > tr"));

    double leftAmount = liquidacion.getTotalAmount();

    for (int i = 0; i < rowsPartidas.size(); i++) {

      double rowAvailable = Validations.numberFormat(driver.
              findElement(By.id(i + "_ef_form_" + form + "_ppg_altasaldo"))
              .getAttribute("value"));

      driver.findElement(By.id(i + "_ef_form_" + form + "_ppg_altaimporte")).clear();

      if (rowAvailable >= leftAmount) {

        driver.findElement(By.id(i + "_ef_form_" + form + "_ppg_altaimporte"))
                .sendKeys(String.valueOf(leftAmount));
        leftAmount = 0;
        break;

      } else {

        driver.findElement(By.id(i + "_ef_form_" + form + "_ppg_altaimporte"))
                .sendKeys(String.valueOf(rowAvailable));
        leftAmount -= rowAvailable;
        leftAmount = Math.round(leftAmount * 100.0) / 100.0;
      }
    }
  }

  private void finishOP(Liquidacion liquidacion, String form, String type, String nombreGrupo) {
    driver.findElement(By.id("formulario_toba")).click();
    scrollToEndPage();
    driver.findElement(By.id("ci_" + form + "_procesar")).click();

    try {
      String error = driver.findElement(By.cssSelector(".overlay-mensaje div")).getText().replace("\\n", "").replace("\\", "");
      driver.findElement(By.id("boton_overlay")).click();
      if (type.equals("Contratos")) {
        liquidacion.setResultLiquidacion(((LiquidacionContrato) liquidacion).getBeneficiary() + " - " + error);
      } else {
        liquidacion.setResultLiquidacion(liquidacion.getCompromiso().toString() + " - " + error);
      }
      SendEmailTLS.sendMessage("Error en la liquidación del " + nombreGrupo, liquidacion.getResultLiquidacion(), false);
      scrollToEndPage();
      driver.findElement(By.id("ci_" + form + "_cancelar")).click();
      return;
    } catch (Exception e) {
    }

    String opGenerated = driver.findElement(By.id("ef_form_20000352_documentosdocumento_principal")).getText();
    String typeOP = opGenerated.substring(3, 7);
    String numberOP = opGenerated.substring(8, opGenerated.indexOf("/"));
    String yearOP = opGenerated.substring(opGenerated.indexOf("/") + 1);

    Validations.isNumber(numberOP);
    Validations.isYear(yearOP);
    OP op = new OP();
    op.setType(typeOP);
    op.setNumber(Integer.parseInt(numberOP));
    op.setYear(Integer.parseInt(yearOP));

    liquidacion.setOp(op);

    scrollToEndPage();

    driver.findElement(By.id("ci_" + form + "_finalizar")).click();
  }

  private void otherFilter(String numberForm) {
    driver.findElement(By.id("colapsar_boton_js_form_" + numberForm + "_filtro_ref")).click();
    driver.findElement(By.id("ef_form_" + numberForm + "_filtro_refnumero")).clear();
    driver.findElement(By.id("ef_form_" + numberForm + "_filtro_refanio_doc")).clear();
  }

  public void createGroup(Group group, String type) {

    goToCreateGroup();
    completeDataFileCreateGroup(group);

    if (type.equals("Contratos")) {
      driver.findElement(By.id("ef_form_50000074_filtrocon_cbu")).click();
    }
    scrollToEndPage();
    driver.findElement(By.id("form_50000074_filtro_filtrar")).click();
    driver.findElement(By.id("colapsar_boton_js_form_50000074_filtro")).click();
    try {
      driver.findElement(By.id("ef_form_5000208_formularionombre")).sendKeys(group.getGroupName());
      driver.findElement(By.id("js_cuadro_84000011_cuadro_todos")).click();
      driver.findElement(By.id("cuadro_84000011_cuadro_procesar")).click();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "No existen liquidaciones en nivel 4 para realizar el grupo");
      System.exit(0);
    }
    try {
      driver.findElement(By.id("boton_overlay")).click();
      JOptionPane.showMessageDialog(null, "El grupo ya se encuentra creado");
      System.exit(0);
    } catch (Exception e) {
    }
  }

  public void goToCreateGroup() {
    driver.findElement(By.id("boton_menu_raiz")).click();;
    driver.findElement(By.id("buscar_text")).sendKeys("grupo");
    driver.findElement(By.id("elemento_menu_listado_5000063")).click();
  }

  public void completeDataFileCreateGroup(Group group) {
    driver.findElement(By.id("ef_form_50000074_filtrodocumento")).click();
    driver.findElement(By.id("ef_form_50000074_filtrodocumento")).sendKeys(group.getTypeOP());
    driver.findElement(By.id("ef_form_50000074_filtrocontenedor")).click();
    driver.findElement(By.id("ef_form_50000074_filtrocontenedor")).sendKeys(group.getFile().getType());
    driver.findElement(By.id("ef_form_50000074_filtronumero_cont")).sendKeys(String.valueOf(group.getFile().getNumber()));
    driver.findElement(By.id("ef_form_50000074_filtroanio_cont")).sendKeys(String.valueOf(group.getFile().getYear()));
    driver.findElement(By.id("ef_form_50000074_filtronivel_autoriz_desde")).click();
    driver.findElement(By.id("ef_form_50000074_filtronivel_autoriz_desde")).sendKeys("Nivel 4");
  }

  public void goToAutorizacionPorGrupoNivel4() {
    driver.findElement(By.id("boton_menu_raiz")).click();
    driver.findElement(By.id("buscar_text")).sendKeys("grupo");
    driver.findElement(By.id("elemento_menu_listado_50000022")).click();
  }

  public void goToAutorizacionPorGrupoNivel7() {
    driver.findElement(By.id("boton_menu_raiz")).click();
    driver.findElement(By.id("buscar_text")).sendKeys("grupo");
    driver.findElement(By.id("elemento_menu_listado_50000024")).click();
  }

  public void filtrarGrupo(Group group) {
    driver.findElement(By.id("ef_form_17000058_filtrogrupo_vinculo")).click();
    vars.put("groupWindow", waitForWindow(1000));
    driver.switchTo().window(vars.get("groupWindow").toString());

    driver.findElement(By.id("ef_form_50000060_filtronombre")).sendKeys(group.getGroupName());
    driver.findElement(By.id("form_50000060_filtro_filtrar")).click();
    driver.findElement(By.id("cuadro_50000064_cuadro0_seleccion")).click();

    driver.switchTo().window(originalWindow);
    scrollToEndPage();
    driver.findElement(By.id("form_17000058_filtro_filtrar")).click();
  }

  public boolean sameTotal(Group group, char level) {

    scrollToEndPage();
    double totalPilaga = Validations.numberFormat(driver.findElement(By.cssSelector(".ei-cuadro-total.col-num-p1 > strong")).getText());
    double montoGrupo = 0;
    ArrayList<Liquidacion> liquidaciones;
    if (level == '4') {
      liquidaciones = group.getLiquidacionesARetener();
    } else {
      liquidaciones = group.getLiquidacionesRetenidas();
    }

    for (Liquidacion liquidacion : liquidaciones) {
      montoGrupo += liquidacion.getTotalAmount();
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
    driver.findElement(By.id("form_50000074_filtro_filtrar")).click();
    driver.findElement(By.id("colapsar_boton_js_form_50000074_filtro")).click();

    ArrayList<Integer> OPPilaga = new ArrayList();

    List<WebElement> rowsOP = driver.findElements(By.cssSelector(".ei-cuadro-fila"));

    for (int i = 0; i < rowsOP.size(); i++) {
      boolean found = false;
      while (!found) {
        try {
          String numberOPPilaga = driver.findElement(By.cssSelector(".ei-cuadro-cc-fondo tbody tr:nth-child(" + (i + 2)
                  + ") td:nth-child(4)")).getText();
          found = true;
          numberOPPilaga = numberOPPilaga.substring(5, numberOPPilaga.indexOf("/"));
          OPPilaga.add(Integer.parseInt(numberOPPilaga));
        } catch (Exception e) {
          scrollTo(20);
        }
      }
    }

    for (Liquidacion liquidacion : group.getLiquidacionesLiquidadas()) {
      if (OPPilaga.contains(liquidacion.getOp().getNumber())) {
        liquidacion.setResultAutorizacion("La liquidacion no se incluye en el grupo por no tener el beneficiario CBU activa");
        group.getLiquidacionesARetener().remove(liquidacion);
      }
    }
  }

  public void retenerManual(Group group, Double amount2) throws InterruptedException {

    driver.findElement(By.id("boton_menu_raiz")).click();
    driver.findElement(By.id("buscar_text")).sendKeys("autorizac");
    driver.findElement(By.id("elemento_menu_listado_3000046")).click();

    for (Liquidacion l : group.getLiquidacionesARetener()) {
      LiquidacionContrato li = (LiquidacionContrato) l;
      if (li.getSituationAFIP().equals("NA") || li.getSituationAFIP().equals("RI")) {
        driver.findElement(By.id("ef_form_5000132_filtrodocumento")).click();
        driver.findElement(By.id("ef_form_5000132_filtrodocumento")).sendKeys(group.getTypeOP());
        driver.findElement(By.id("ef_form_5000132_filtronumero")).sendKeys(String.valueOf(li.getOp().getNumber()));
        driver.findElement(By.id("ef_form_5000132_filtroanio_doc")).sendKeys(String.valueOf(li.getOp().getYear()));
        scrollToEndPage();
        driver.findElement(By.id("form_5000132_filtro_filtrar")).click();
        driver.findElement(By.id("cuadro_3000148_cuadro0_seleccion")).click();
        driver.findElement(By.id("form_1564_formulario_modificacion")).click();
        scrollToEndPage();
        driver.findElement(By.id("js_form_2037_formulario_agregar")).click();
        scrollToEndPage();

        if (!lineRetencion("Ing. Brutos", li, amount2)) {
          continue;
        }
        if (li.getSituationAFIP().equalsIgnoreCase("RI")) {
          driver.findElement(By.id("js_form_2037_formulario_agregar")).click();
          scrollToEndPage();
          if (!lineRetencion("Ganancias", li, amount2)) {
            continue;
          }
          driver.findElement(By.id("js_form_2037_formulario_agregar")).click();
          scrollToEndPage();
          if (!lineRetencion("Iva", li, amount2)) {
            continue;
          }
          scrollToEndPage();
        }
        String retencionesString = driver.findElement(By.id("s_ef_form_2037_formularioimporte_retenido")).getText();
        li.setAmountRetenciones(Validations.numberFormat(retencionesString));
        driver.findElement(By.id("ci_3000141_asignar_retenciones_procesar")).click();
        driver.findElement(By.id("boton_overlay")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("ef_form_5000132_filtronumero")).clear();
        driver.findElement(By.id("ef_form_5000132_filtroanio_doc")).clear();
      }
      group.addLiquidacionRetenida(li);
    }
  }

  public boolean lineRetencion(String tax, LiquidacionContrato li, double amount2) throws InterruptedException {
    String condition = null;
    int order = -1;
    double monto = li.getTotalAmount();
    if (tax.equals("Ganancias")) {
      order = 1;
      condition = "Ej. Profesiones liberales u Oficios";
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
      if (monto > amount2) {
        condition = "Contratados del Estado Nacional (4%)";
      } else {
        condition = "CONTRATADOS ESTADO NACIONAL 2%";
      }
    } else {
      li.setResultAutorizacion("No se reconoce el impuesto a retener");
      driver.findElement(By.id("ci_3000141_asignar_retenciones_cancelar")).click();
      driver.findElement(By.id("colapsar_boton_js_form_5000132_filtro")).click();
      driver.findElement(By.id("ef_form_5000132_filtronumero")).clear();
      driver.findElement(By.id("ef_form_5000132_filtroanio_doc")).clear();
      return false;
    }

    if (!driver.findElement(By.id((156 + order) + "_ef_form_2037_formularioconcepto")).getText().contains(tax.replace(" ", ""))) {
      li.setResultAutorizacion("No se encuentra cargado el impuesto a retener");
      driver.findElement(By.id("ci_3000141_asignar_retenciones_cancelar")).click();
      driver.findElement(By.id("colapsar_boton_js_form_5000132_filtro")).click();
      driver.findElement(By.id("ef_form_5000132_filtronumero")).clear();
      driver.findElement(By.id("ef_form_5000132_filtroanio_doc")).clear();
      return false;
    }
    boolean retenido = false;
    while (!retenido) {
      driver.findElement(By.id((156 + order) + "_ef_form_2037_formularioconcepto")).click();
      driver.findElement(By.id((156 + order) + "_ef_form_2037_formularioconcepto")).sendKeys(tax);
      driver.findElement(By.id("js_ci_3000141_asignar_retenciones_cont")).click();
      Thread.sleep(2000);
      driver.findElement(By.id((156 + order) + "_ef_form_2037_formularioregimen")).click();
      driver.findElement(By.id((156 + order) + "_ef_form_2037_formularioregimen")).sendKeys(condition);

      driver.findElement(By.id((156 + order) + "_ef_form_2037_formulariomonto_base")).clear();
      driver.findElement(By.id((156 + order) + "_ef_form_2037_formulariomonto_base")).sendKeys(String.valueOf(monto));
      driver.findElement(By.id("form_2037_formulario" + (156 + order) + "_calcular")).click();

      try {
        driver.findElement(By.id("boton_overlay")).click();
        scrollToEndPage();
        driver.findElement(By.id((156 + order) + "_ef_form_2037_formularioconcepto")).click();
        driver.findElement(By.id((156 + order) + "_ef_form_2037_formularioconcepto")).sendKeys("--");
        driver.findElement(By.id("js_ci_3000141_asignar_retenciones_cont")).click();
        Thread.sleep(2000);
        scrollToEndPage();
      } catch (Exception e) {
        retenido = true;
      }
    }
    scrollToEndPage();
    return true;
  }

  public void retenerRestoGrupo(Group group) {

    try {
      driver.findElement(By.id("js_cuadro_84000012_cuadro_todos")).click();
      scrollToEndPage();
      driver.findElement(By.id("cuadro_84000012_cuadro_procesar")).click();
      driver.findElement(By.id("boton_overlay")).click();
    } catch (Exception e) {
      // Se retuvieron todas una por una
    }

  }

  public void goToLiquidationsList(Group group) {
    driver.findElement(By.id("boton_menu_raiz")).click();
    driver.findElement(By.id("buscar_text")).sendKeys("listado de liquidaciones");
    driver.findElement(By.id("elemento_menu_listado_5000043")).click();

    driver.findElement(By.id("ef_form_5000135_filtrocontenedor")).click();
    driver.findElement(By.id("ef_form_5000135_filtrocontenedor")).sendKeys(group.getFile().getType());
    driver.findElement(By.id("ef_form_5000135_filtroanio_cont")).sendKeys(String.valueOf(group.getFile().getYear()));
    driver.findElement(By.id("ef_form_5000135_filtronumero_cont")).sendKeys(String.valueOf(group.getFile().getNumber()));
    scrollToEndPage();
    driver.findElement(By.id("form_5000135_filtro_filtrar")).click();
  }

  public String waitForWindow(int timeout) {
    try {
      Thread.sleep(timeout);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Set<String> whNow = driver.getWindowHandles();
    for (String windowHandle : whNow) {
      if (!originalWindow.contentEquals(windowHandle)) {
        driver.switchTo().window(windowHandle);
        return windowHandle;
      }
    }
    return null;
  }
}
