package com.ControladoresRed;


import com.Entidades.Fantasma;
import com.Entidades.Nodo;
import com.Entidades.NodoRF;
import com.Entidades.Recurso;
import com.Utils.SistemaUtil;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Junior on 08/04/2018.
 */
public class ConexionUtils {

    private String direccion;
    private int puerto;
    private Socket reves;

    private Object respuesta;

    private static ConexionUtils conexion;


    private ConexionUtils(){

    }


    public static ConexionUtils obtenerInstancia(){

        if (conexion==null)
           conexion = new ConexionUtils();
        return conexion;
    }



    public synchronized Object enviarMensaje(Mensaje dato){
        
        try {
            ObjectOutputStream salidaObjeto;
            //Se colocan los datos del que funge como servidor (Direccion IP y Puerto).
            Socket reves = null;
                reves = new Socket (dato.getDestino().getDireccion(),dato.getDestino().getPuertopeticion());
            salidaObjeto = new ObjectOutputStream(reves.getOutputStream());
            //El cliente manda:
            System.out.println("Funcion: "+dato.getFuncion()+" Tiempo de envio: "+SistemaUtil.obtenerHora());
            salidaObjeto.writeObject(dato);
            //El cliente recibe:
            ObjectInputStream ois = new ObjectInputStream(reves.getInputStream());
            Object respuesta = ois.readObject();
            //Se cierra la conexion.
            reves.close();
            return respuesta;
        } catch (IOException ex) {
            Logger.getLogger(ConexionUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConexionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }catch (Exception ex) {
            System.out.println("Error durante conexion para envio "+dato.getFuncion());
            Logger.getLogger(ConexionUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }



}
