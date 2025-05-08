/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package liquidacionautomatica;

import java.io.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquidacionautomatica.entities.User;
import liquidacionautomatica.exceptions.CredentialsNotFound;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.util.Timeout;

/**
 *
 * @author rbayarri
 */
public class LiquidacionAutomatica {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    ObjectMapper mapper = new ObjectMapper();
    try {
      String json = Request.get("https://mabay.com.ar/uncuyo/config-system.json")
              .connectTimeout(Timeout.of(5000, TimeUnit.MILLISECONDS))
              .responseTimeout(Timeout.of(5000, TimeUnit.MILLISECONDS))
              .execute()
              .returnContent()
              .asString();
      HtmlElement config = mapper.readValue(json, HtmlElement.class);
      config.isLocal = false;
      HtmlElement.load(config);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("No se pudo cargar la configuración. Se cargará la configuración local.");
      FileInputStream fileInputStream = null;
      try {
        fileInputStream = new FileInputStream("config-system.json");
        HtmlElement config = mapper.readValue(fileInputStream, HtmlElement.class);
        config.isLocal = true;
        HtmlElement.load(config);
        System.out.println("Configuración cargada desde archivo local");
      } catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "No se encontró archivo de configuración local. " +
                "Por favor, verifique que el archivo config-system.json esté en la misma carpeta que el ejecutable.");
        return;
      } finally {
        if (fileInputStream != null) {
          try {
            fileInputStream.close();
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      }
    }

    User user = null;
    boolean creado = false;
    while (!creado) {
      try {
        user = new User();
        creado = true;
      } catch (IOException | CredentialsNotFound e) {
        String usuarioPilaga = JOptionPane.showInputDialog("Ingrese su usuario de Pilagá");
        String clavePilaga = JOptionPane.showInputDialog("Ingrese su clave de Pilagá");
        
        if (usuarioPilaga == null || usuarioPilaga.isEmpty() || clavePilaga == null || clavePilaga.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Los datos ingresados son incorrectos");
          return;
        }
        FileWriter result;
        try {
          String home;
          home = System.getenv("USERPROFILE");
          if (home == null) {
            home = System.getProperty("user.home");
          }
          result = new FileWriter(String.format("%s%scredenciales.txt", home, File.separator));
          BufferedWriter resultWriter = new BufferedWriter(result);
          resultWriter.write(usuarioPilaga + "\n" + clavePilaga);
          resultWriter.close();
        } catch (IOException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(null, "Error al crear el archivo credenciales.txt");
        }
      }
    }
    
    String amountUntil2 = "";
    try {
      BufferedReader reader = new BufferedReader(new FileReader("retenciones/importe_retencion_hasta_2%.txt"));
      amountUntil2 = reader.readLine();
      Double.parseDouble(amountUntil2);
      System.out.println("Importe máximo a retener 2%: " + amountUntil2);
    } catch (IOException e) {
      System.out.println("No se ha seteado importe máximo de retención al 2%.");
    } catch (NumberFormatException ex){
      System.out.println("No se ha ingreado un importe válido en el archivo.");
    } catch (Exception exc){
      System.out.println("Error inesperado al leer el archivo.");
    }
    
    LiquidacionJFrame initialForm = new LiquidacionJFrame(user, amountUntil2);
    initialForm.setVisible(true);
  }
}
