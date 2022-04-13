/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package liquidacionautomatica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import liquidacionautomatica.entities.Writer;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import liquidacionautomatica.entities.Browser;
import liquidacionautomatica.entities.Group;
import liquidacionautomatica.entities.User;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author rbayarri
 */
public class LiquidacionJFrame extends javax.swing.JFrame {

  /**
   * Creates new form LiquidacionJFrame
   */
  private User user;

  public LiquidacionJFrame() {
  }

  public LiquidacionJFrame(User user) {
    initComponents();
    this.typeLiquidacion.addItem("Contratos");
    this.typeLiquidacion.addItem("Ordenanaza 36");
    this.user = user;
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    typeLiquidacion = new javax.swing.JComboBox<>();
    liquidar = new javax.swing.JButton();
    cancel = new javax.swing.JButton();
    jLabel3 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    amountUntil2 = new javax.swing.JTextField();
    jLabel9 = new javax.swing.JLabel();
    jLabel12 = new javax.swing.JLabel();
    jLabel13 = new javax.swing.JLabel();
    jLabel14 = new javax.swing.JLabel();
    jLabel10 = new javax.swing.JLabel();
    jLabel11 = new javax.swing.JLabel();
    jLabel15 = new javax.swing.JLabel();
    jLabel16 = new javax.swing.JLabel();
    jLabel17 = new javax.swing.JLabel();
    jLabel19 = new javax.swing.JLabel();
    jLabel20 = new javax.swing.JLabel();
    jLabel21 = new javax.swing.JLabel();
    jLabel22 = new javax.swing.JLabel();
    jLabel23 = new javax.swing.JLabel();
    jLabel24 = new javax.swing.JLabel();
    jLabel25 = new javax.swing.JLabel();
    jCheckBox1 = new javax.swing.JCheckBox();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Liquidación Automática");

    jLabel1.setText("Tipo de liquidación");

    liquidar.setText("Liquidar");
    liquidar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        liquidarActionPerformed(evt);
      }
    });

    cancel.setText("Cancelar");
    cancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelActionPerformed(evt);
      }
    });

    jLabel3.setText("<html><p style=\"font-weight: bold;text-decoration:underline\">Pasos previos comunes:</p></html>");
    jLabel3.setToolTipText("");

    jLabel2.setText("1) Verificar el periodo, el año y el número de listado del grupo");

    jLabel4.setText("2) Verificar que el expediente indicado sea correcto");

    jLabel7.setText("Monto máximo de retención al 2%:");

    amountUntil2.setText("38000");
    amountUntil2.setToolTipText("");

    jLabel9.setText("<html><p style=\"font-weight: bold;text-decoration:underline\">Pasos previos Contratos:</p></html>");
    jLabel9.setToolTipText("");

    jLabel12.setText("1) Completar la situación de AFIP de cada contratado (AC, EX, NA o RI)");

    jLabel13.setText("2) Verificar datos del tipo, letra, fecha y monto  del comprobante");

    jLabel14.setText("3) En caso de ser necesario, modificar el importe de retención al 2%");

    jLabel10.setText("3) De existir registros liquidados, completar la columna OP con los datos de Pilagá");

    jLabel11.setText("4) Indicar si el grupo ya fue creado.En este caso, se asume que se liquidaron todos");

    jLabel15.setText("    los registros y los mismos ya se encuentran incluidos en el grupo en Nivel 4");

    jLabel16.setText("4) Control de totales del grupo en nivel 4");
    jLabel16.setEnabled(false);

    jLabel17.setText("<html><p style=\"font-weight: bold;text-decoration:underline\">Orden de ejecución del programa:</p></html>");
    jLabel17.setToolTipText("");

    jLabel19.setText("1) Lectura del archivo");
    jLabel19.setEnabled(false);

    jLabel20.setText("2) Liquidacion de registros");
    jLabel20.setEnabled(false);

    jLabel21.setText("3) Creación del grupo e inclusión de las liquidaciones");
    jLabel21.setEnabled(false);

    jLabel22.setText("7) Pase a nivel 7 por grupo");
    jLabel22.setEnabled(false);

    jLabel23.setText("6) Retención de impuesto (Contratos)");
    jLabel23.setEnabled(false);

    jLabel24.setText("5) Detección de diferencias");
    jLabel24.setEnabled(false);

    jLabel25.setText("8) Control de totales del grupo en nivel 7");
    jLabel25.setEnabled(false);

    jCheckBox1.setText("Grupo creado");

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(40, 40, 40)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(typeLiquidacion, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(amountUntil2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel4)
                  .addComponent(jLabel2)
                  .addComponent(jLabel10)
                  .addComponent(jLabel11)
                  .addComponent(jLabel15)
                  .addComponent(jLabel12)
                  .addComponent(jLabel13)
                  .addComponent(jLabel14)
                  .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabel19)
                  .addComponent(jLabel20)
                  .addComponent(jLabel21)
                  .addComponent(jLabel16)
                  .addComponent(jLabel24)
                  .addComponent(jLabel23)
                  .addComponent(jLabel25)
                  .addComponent(jLabel22)))))
          .addGroup(layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addComponent(jCheckBox1))
          .addGroup(layout.createSequentialGroup()
            .addGap(345, 345, 345)
            .addComponent(liquidar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(84, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(28, 28, 28)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(typeLiquidacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(jCheckBox1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel7)
          .addComponent(amountUntil2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel4)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel10)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel11)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel15)
            .addGap(18, 18, 18)
            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(jLabel12))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel19)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel20)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel21)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel16)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel24)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel23)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel22)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel25)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel13)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel14)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(liquidar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(22, 22, 22))
    );

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
      this.setVisible(false);
      System.exit(0);
    }//GEN-LAST:event_cancelActionPerformed

    private void liquidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_liquidarActionPerformed

      this.jLabel19.setEnabled(true);
      Double amount2;
      String directory = null;
      try {
        amount2 = Double.parseDouble(this.amountUntil2.getText());
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "El formato indicado no es válido");
        return;
      }

      JFileChooser fc = new JFileChooser();
      fc.setCurrentDirectory(new File("M:\\CONTRATOS\\PENDIENTES\\"));
      FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text", "xlsx", "xlsm");
      fc.setFileFilter(filter);
      int respuesta = fc.showOpenDialog(this);
      if (respuesta == JFileChooser.APPROVE_OPTION) {
        File archivoElegido = fc.getSelectedFile();
        directory = archivoElegido.getParent();

        FileInputStream file = null;
        try {
          file = new FileInputStream(archivoElegido);
        } catch (FileNotFoundException ex) {
          Logger.getLogger(LiquidacionJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        XSSFWorkbook workbook = null;
        try {
          workbook = new XSSFWorkbook(file);
        } catch (IOException ex) {
          Logger.getLogger(LiquidacionJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        XSSFSheet sheet = workbook.getSheetAt(0);
        Group group = new Group();
        group.readingHeaders(sheet, this.typeLiquidacion.getSelectedItem().toString());
        if (this.typeLiquidacion.getSelectedItem().toString().equals("Contratos")) {
          group.setTypeOP("OPCT");
          group.readingContratosExcel(sheet);
        } else {
          group.setTypeOP("OP36");
          group.reading36Excel(sheet);
        }
        Writer writer = new Writer(group, directory);
        writer.writeFirstRead(group);

        this.jLabel20.setEnabled(true);
        Browser browser = new Browser();
        try {
          browser.open();
        } catch (Exception e) {
          JOptionPane.showMessageDialog(null, "No se encontraron navegadores instalados");
          System.exit(0);
        }
        browser.goToPilaga();
        browser.loginPilaga(user);
        String form1 = null;
        String form2 = null;
        String form3 = null;
        if (this.typeLiquidacion.getSelectedItem().toString().equals("Contratos")) {
          browser.goToDevengadoCompras();
          form1 = "2282";
          form2 = "84000213";
          form3 = "2035";
        } else {
          browser.goToDevengadoBecas();
          form1 = "1559";
          form2 = "15000119";
          form3 = "1521";
        }
        browser.liquidar(group, form1, form2, form3, this.typeLiquidacion.getSelectedItem().toString());
        writer.writeResultLiquidacion(group);

        this.jLabel21.setEnabled(true);
        if (!this.jCheckBox1.isSelected()) {
          browser.createGroup(group, this.typeLiquidacion.getSelectedItem().toString());
        }

        this.jLabel16.setEnabled(true);
        group.setLiquidacionesARetener(group.getLiquidacionesLiquidadas());
        browser.goToAutorizacionPorGrupoNivel4();
        browser.filtrarGrupo(group);
        if (!browser.sameTotal(group, '4')) {
          this.jLabel24.setEnabled(true);
          browser.differences(group);
          writer.writeLiquidacionesExcluidas(group);
        }
        if (this.typeLiquidacion.getSelectedItem().toString().equals("Contratos")) {
          try {
            this.jLabel23.setEnabled(true);
            browser.retenerManual(group, amount2);

          } catch (InterruptedException ex) {
            // Demora en esperar que cargue la retencion
          }
        } else { // Para ord 36
          group.addRetenidasResto();
        }
        this.jLabel22.setEnabled(true);
        browser.goToAutorizacionPorGrupoNivel4();
        browser.filtrarGrupo(group);
        browser.retenerRestoGrupo(group);
        writer.writeLiquidacionNoRetenidas(group);

        this.jLabel25.setEnabled(true);
        browser.goToAutorizacionPorGrupoNivel7();
        browser.filtrarGrupo(group);
        if (!browser.sameTotal(group, '7')) {
          JOptionPane.showMessageDialog(null, "Revisar diferencias");
        }
        browser.goToLiquidationsList(group);
        writer.writeForExp(group);
        JOptionPane.showMessageDialog(null, "Se procesó el grupo con éxito\nDescargar OP y unirlas en un único archivo\nConsultar el archivo con los resultados de la liquidación");
      }
      System.exit(0);
    }//GEN-LAST:event_liquidarActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;

        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(LiquidacionJFrame.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(LiquidacionJFrame.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(LiquidacionJFrame.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(LiquidacionJFrame.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new LiquidacionJFrame().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField amountUntil2;
  private javax.swing.JButton cancel;
  private javax.swing.JCheckBox jCheckBox1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel10;
  private javax.swing.JLabel jLabel11;
  private javax.swing.JLabel jLabel12;
  private javax.swing.JLabel jLabel13;
  private javax.swing.JLabel jLabel14;
  private javax.swing.JLabel jLabel15;
  private javax.swing.JLabel jLabel16;
  private javax.swing.JLabel jLabel17;
  private javax.swing.JLabel jLabel19;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel20;
  private javax.swing.JLabel jLabel21;
  private javax.swing.JLabel jLabel22;
  private javax.swing.JLabel jLabel23;
  private javax.swing.JLabel jLabel24;
  private javax.swing.JLabel jLabel25;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel9;
  private javax.swing.JButton liquidar;
  private javax.swing.JComboBox<String> typeLiquidacion;
  // End of variables declaration//GEN-END:variables
}