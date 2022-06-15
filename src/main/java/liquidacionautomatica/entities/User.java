/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import liquidacionautomatica.exceptions.CredentialsNotFound;

/**
 *
 * @author rbayarri
 */
public class User {

  private String pilagaUser;
  private String pilagaPassword;

  public User() throws FileNotFoundException, IOException, CredentialsNotFound {
    String user = System.getenv("USERNAME");
    System.out.println(user);
    FileReader leerArchivoCredenciales = new FileReader("C:\\Users\\" + user + "\\credenciales.txt");
    BufferedReader contenidoCredenciales = new BufferedReader(leerArchivoCredenciales);

    this.pilagaUser = contenidoCredenciales.readLine();
    this.pilagaPassword = contenidoCredenciales.readLine();

    validation();
  }

  public String getPilagaUser() {
    return pilagaUser;
  }

  public void setPilagaUser(String pilagaUser) {
    this.pilagaUser = pilagaUser;
  }

  public String getPilagaPassword() {
    return pilagaPassword;
  }

  public void setPilagaPassword(String pilagaPassword) {
    this.pilagaPassword = pilagaPassword;
  }

  public void validation() throws CredentialsNotFound {
    if (this.pilagaPassword == null || this.pilagaUser == null) {
      throw new CredentialsNotFound("No se encontraron todas las credenciales necesarias para acceder al sistema");
    }
  }

}
