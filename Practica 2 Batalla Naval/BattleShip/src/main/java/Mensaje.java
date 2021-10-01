
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jonat
 */
public class Mensaje implements Serializable {
    //Tipo 1: disparo
    //Tipo 2: instruccion
    //Tipo 3: mensaje
    int tipo;
    String message;
    String nombre;
    int x;
    int y;
    Mensaje(int tipo, String message){
        this.tipo = tipo;
        this.message = message;
    }
    
    Mensaje(int tipo, String message, String nombre){
        this.tipo = tipo;
        this.message = message;
        this.nombre = nombre;
    }
    
    Mensaje(int tipo, String message, int x, int y){
        this.tipo = tipo;
        this.message = message;
        this.x = x;
        this.y = y;
    }
    Mensaje(int tipo, String message, String nombre, int x, int y){
        this.tipo = tipo;
        this.message = message;
        this.nombre = nombre;
        this.x = x;
        this.y = y;
    }
    public int getTipo(){
        return tipo;
    }
    
    public String getMessage(){
        return message;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
}
