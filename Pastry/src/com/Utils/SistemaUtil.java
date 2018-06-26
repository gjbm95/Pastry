package com.Utils;

import com.ControladoresRed.ConexionUtils;
import com.ControladoresRed.Mensaje;
import com.Entidades.Estadistica;
import com.Entidades.Fantasma;
import com.Entidades.Nodo;
import com.Entidades.NodoRF;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Junior on 01/04/2018.
 */
public class SistemaUtil {

    public static String tipo ="";
    public static String almacen = "recursos";
    public static Object respuesta; 
    public static boolean terminal=false;
    public static String servidorTiempo;
    public static boolean informarTiempo = true;
    public static String obtenerHora(){
        Calendar calendario = Calendar.getInstance();
        int hora, minutos, segundos,milisegundos;
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        milisegundos = calendario.get(Calendar.MILLISECOND);
        return hora + ":" + minutos + ":" + segundos + ":" + milisegundos;
    }

    
     private static void reseteo(){
        try {
            String ruta = "posicion.txt";
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write("0");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(SistemaUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
     private synchronized static int obtenerPuesto(){
        
        try {
            String ruta = "posicion.txt";
            File archivo = new File(ruta);
            BufferedReader br;
            br = new BufferedReader(new FileReader(archivo));
            StringBuilder posicion=new StringBuilder();
            String valor ="";
            while(valor!=null){
              posicion.append(valor);
              valor =  br.readLine();
            }
            String resultado = posicion.toString();
            int posicionfinal = Integer.parseInt(resultado)+1;
            System.out.println("Valor "+posicionfinal);
            br.close();
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(Integer.toString(posicionfinal));
            bw.close();
            return posicionfinal;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemaUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SistemaUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0; 
     }
      
    
     private static String [] adaptadoresDisponibles(){
          int conteo =0;
                String direcciones []= new String[10];
                try {
                    Enumeration e = NetworkInterface.getNetworkInterfaces();
                    while(e.hasMoreElements())
                    {
                        NetworkInterface n = (NetworkInterface) e.nextElement();
                        Enumeration ee = n.getInetAddresses();
                        while (ee.hasMoreElements())
                        {
                            InetAddress i = (InetAddress) ee.nextElement();
                            String direccion = i.getHostAddress();
                            String octetos[] = direccion.split("\\.");
                            if(octetos.length==4) {
                                System.out.println((conteo+1)+"- " + i.getHostAddress());
                                direcciones[conteo]=i.getHostAddress();
                                conteo++;
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
       return direcciones; 
     }
    
     private static String seleccionarPuerto(){
        String respuesta ="";
        Integer valor =0;
        while ((valor<2001)||(valor>5000)){
            Random r = new Random();
            valor = r.nextInt(5000);
        }
        respuesta = valor.toString();
        return respuesta;
    }
     
    private static int tiempo(){
        Random r = new Random();
        return r.nextInt(30);
    }
     
    public static void generarReporte(){
       ArchivoThread generar = new ArchivoThread();
       new Thread(generar).start();     
    }
    
    private static class ArchivoThread implements Runnable{
        public void run() {
            try {
                String ruta = "resultados.txt";
                File archivo = new File(ruta);
                BufferedWriter bw;
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write("------------------------------------------------------\n");
                bw.newLine();
                bw.write("Resultados de Prueba de Estres \n");
                bw.newLine();
                bw.write("------------------------------------------------------\n");
                bw.newLine();
                bw.write("Nº Nodos estables: "+Estadistica.getNodos_estables()+" \n");
                bw.newLine();
                bw.write("Nº Nodos caidos: "+Estadistica.getNodos_caidos()+" \n");
                bw.newLine();
                bw.write("Nº tablas generadas: "+Estadistica.getTablas_generadas()+"\n");
                bw.newLine();
                bw.write("------------------------------------------------------\n");
                bw.newLine();
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(SistemaUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    public static void reportarTiempo(String funcion, String marca, NodoRF origen){
        if(informarTiempo){
                Mensaje mensaje;
            try {
                mensaje = new Mensaje(funcion, marca, origen, new NodoRF(servidorTiempo,1500));
            
                ConexionUtils.obtenerInstancia().enviarMensaje(mensaje);
           } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(SistemaUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
