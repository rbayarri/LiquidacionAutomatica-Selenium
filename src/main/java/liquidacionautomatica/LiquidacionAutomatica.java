/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package liquidacionautomatica;

import java.io.BufferedWriter;
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
          result = new FileWriter("credenciales.txt");
          BufferedWriter resultWriter = new BufferedWriter(result);
          resultWriter.write(usuarioPilaga + "\n" + clavePilaga);
          resultWriter.close();
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(null, "Error al crear el archivo credenciales.txt");
        }
      }
    }
    LiquidacionJFrame initialForm = new LiquidacionJFrame(user);
    initialForm.setVisible(true);
  }
}
