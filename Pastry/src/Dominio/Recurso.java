/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dominio;

import java.math.BigInteger;

/**
 *
 * @author Junior
 */
public class Recurso {
    private String nombre; 
    private String ruta; 
    private BigInteger hash;
    private String propietario;
    
    public Recurso(){
    
    }

    public Recurso(String nombre, String ruta, BigInteger hash) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.hash = hash;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public BigInteger getHash() {
        return hash;
    }

    public void setHash(BigInteger hash) {
        this.hash = hash;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
    
    
    
    
}
