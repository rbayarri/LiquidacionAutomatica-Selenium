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
import lombok.Getter;

/**
 *
 * @author rbayarri
 */
@Getter
public class User {

  private final String pilagaUser;
  private final String pilagaPassword;

  public User() throws FileNotFoundException, IOException, CredentialsNotFound {
    String home = System.getenv("USERPROFILE");
    FileReader leerArchivoCredenciales = new FileReader(home + "\\credenciales.txt");
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
