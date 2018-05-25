/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pastry.P2PFileTransferApplicationImpl;
import pastry.Utils;

/**
 *
 * @author Junior
 */
public class Sistema {
    
   private String direccion; 
   private int puerto;
   private ArrayList<Recurso> recursos;
   private P2PFileTransferApplicationImpl app_file;
   private static Sistema sistema; 
   
   
    private Sistema(){
        
    }
   
    public static Sistema obtenerInstancia(){
        if (sistema==null){
          sistema = new Sistema();
        }
        return sistema;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public ArrayList<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(ArrayList<Recurso> recursos) {
        this.recursos = recursos;
    }

    public P2PFileTransferApplicationImpl getApp_file() {
        return app_file;
    }

    public void setApp_file(P2PFileTransferApplicationImpl app_file) {
        this.app_file = app_file;
    }

    public static Sistema getSistema() {
        return sistema;
    }

    public static void setSistema(Sistema sistema) {
        Sistema.sistema = sistema;
    }
    
    
    
    public void cargarRecursos(){
        try {
            File carpeta = new File("storage");
            System.out.println("Cargando recursos...");
            ArrayList<Recurso> recursos = new ArrayList<>();
            Recurso recurso;
            int id = 0;
            for (File archivo : carpeta.listFiles()) {
                recurso = new Recurso();
                recurso.setNombre(archivo.getName());
                recurso.setHash(Utils.generarHash(recurso.getNombre()));
                recurso.setRuta(archivo.getPath());
                recurso.setPropietario(this.sistema.getDireccion());
                recursos.add(recurso);
            }
            this.sistema.setRecursos(recursos);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    
    public boolean buscarRecurso(String filename){
    
        for (Recurso recurso: recursos){
            try {
                if (recurso.getHash().equals(Utils.generarHash(filename))){
                    return true;
                } 
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
       return false;   
    }
  
    
   
    
}
