/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liquidacionautomatica.exceptions;

/**
 *
 * @author renzo
 */
public class NotMatchBeneficiary extends Exception{

  /**
   * Creates a new instance of <code>NotMatchBeneficiary</code> without detail
   * message.
   */
  public NotMatchBeneficiary() {
  }

  /**
   * Constructs an instance of <code>NotMatchBeneficiary</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public NotMatchBeneficiary(String msg) {
    super(msg);
  }
}
