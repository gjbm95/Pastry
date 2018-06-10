/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pastry;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    /**
     * Aplica el algoritmo de compendio SHA-1 sobre la entrada y retorna su representacion numerica
     * @param entrada Caracteres alfanumericos
     * @return Numero de maximo 60 digitos
     */
    public static BigInteger generarHash (String entrada) throws NoSuchAlgorithmException
    {
        MessageDigest algoritmoCompendio = MessageDigest.getInstance("SHA1");
        algoritmoCompendio.update(StandardCharsets.UTF_8.encode(entrada));

        BigInteger hash = new BigInteger (1, algoritmoCompendio.digest() );
        return hash.mod(BigInteger.valueOf(20));
    }
    
     public static int seleccionarPuerto(){
        Integer valor =0;
        while ((valor<2001)||(valor>5000)){
            Random r = new Random();
            valor = r.nextInt(5000);
        }
        return valor;
    }
}
