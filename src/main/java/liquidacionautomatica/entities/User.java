/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import java.io.*;

import liquidacionautomatica.exceptions.CredentialsNotFound;
import lombok.Getter;

/**
 *
 * @author rbayarri
 */
@Getter
public class User {

  private final String pilagaUser;
  private final String pilagaPassword;

  public User() throws IOException, CredentialsNotFound {
    String home;
    home = System.getenv("USERPROFILE");
    if (home == null) {
      home = System.getProperty("user.home");
    }
    FileReader leerArchivoCredenciales = new FileReader(String.format("%s%scredenciales.txt", home, File.separator));
    BufferedReader contenidoCredenciales = new BufferedReader(leerArchivoCredenciales);

    this.pilagaUser = contenidoCredenciales.readLine();
    this.pilagaPassword = contenidoCredenciales.readLine();

    validation();
  }

  public void validation() throws CredentialsNotFound {
    if (this.pilagaPassword == null || this.pilagaUser == null) {
      throw new CredentialsNotFound("No se encontraron todas las credenciales necesarias para acceder al sistema");
    }
  }

}
