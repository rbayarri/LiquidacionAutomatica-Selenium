/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package liquidacionautomatica.entities;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author renzo
 */
@Getter
@Setter
public class Compromiso {

  private String type;
  private Integer number;
  private Integer year;

  @Override
  public String toString() {
    return type + " : " + number + " / " + year;
  }

}
