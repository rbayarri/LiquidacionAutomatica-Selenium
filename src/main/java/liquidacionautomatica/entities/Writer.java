/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package liquidacionautomatica.entities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author rbayarri
 */
public abstract class Writer {

  protected String fileName;
  protected FileWriter fileWriter;
  protected BufferedWriter bufferedWriter;

  protected void initializeWriters() throws IOException {
    if (fileWriter == null || bufferedWriter == null) {
      fileWriter = new FileWriter(this.fileName, true);
      bufferedWriter = new BufferedWriter(fileWriter);
    }
  }

  protected void write(String message) {
    try {
      initializeWriters();
      bufferedWriter.write(message);
      bufferedWriter.flush(); // Ensure data is written immediately
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "No se pudo escribir en el archivo de resultados");
    }
  }

  public void closeWriters() {
    try {
      if (bufferedWriter != null) bufferedWriter.close();
      if (fileWriter != null) fileWriter.close();
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null, "Error al cerrar los escritores");
    }
  }
}
