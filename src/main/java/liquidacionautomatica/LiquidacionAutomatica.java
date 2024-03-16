/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package liquidacionautomatica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import liquidacionautomatica.entities.User;
import liquidacionautomatica.exceptions.CredentialsNotFound;

/**
 *
 * @author rbayarri
 */
public class LiquidacionAutomatica {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {

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
          String home = System.getenv("USERPROFILE");
          result = new FileWriter(home + "\\credenciales.txt");
          BufferedWriter resultWriter = new BufferedWriter(result);
          resultWriter.write(usuarioPilaga + "\n" + clavePilaga);
          resultWriter.close();
        } catch (IOException ex) {
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
