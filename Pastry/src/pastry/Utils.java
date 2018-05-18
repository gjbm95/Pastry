/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pastry;

import java.util.Calendar;

/**
 *
 * @author Junior
 */
public class Utils {
    public static String filename=""; 
    
    public static String obtenerHora(){
    Calendar calendario = Calendar.getInstance();
    int hora, minutos, segundos, milisegundos;
    hora =calendario.get(Calendar.HOUR_OF_DAY);
    minutos = calendario.get(Calendar.MINUTE);
    segundos = calendario.get(Calendar.SECOND);
    milisegundos = calendario.get(Calendar.MILLISECOND);
    return hora + ":" + minutos + ":" + segundos + ":" + milisegundos;
    }
}
