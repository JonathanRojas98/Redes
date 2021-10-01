
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
public class Nave {
    int tipo;
    boolean vivo;
    int Ax;
    int Ay;
    int Bx;
    int By;
    
    public Nave(int tipo, int Ax, int Ay, int Bx, int By){
        this.tipo = tipo;
        this.Ax = Ax;
        this.Ay = Ay;
        this.Bx = Bx;
        this.By = By;
        vivo = true;
    }
    
    public Nave(int Ax, int Ay, int Bx, int By){
        this.Ax = Ax;
        this.Ay = Ay;
        this.Bx = Bx;
        this.By = By;
        vivo = true;
    }
    
    public boolean esCorrecto(){
        int longitud = -1;
        if(Ax == Bx)
            longitud = Math.abs(Ay - By);
        else if(Ay == By)
            longitud = Math.abs(Ax - Bx);
        
        /*System.out.println(longitud);
        System.out.println(tipo);*/
        switch(tipo){
            case 1:
                if(longitud == 0){
                    return true;
                }
                break;
            case 2:
                if(longitud == 1){ 
                    return true;
                }
                break;
            case 3:
                if(longitud == 2){ 
                    return true;
                }
                break;
            case 4:
                if(longitud == 3){ 
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }
    
    public boolean cabeEnTablero(int table[][]){
        if(!this.esCorrecto()){
            return false;
        }
        if(Ax < 0 || Ax > 9 || Ay < 0 || Ay > 9 || Bx < 0 || Bx > 9 || By < 0 || By > 9){
            return false;
        }
        
        if(Ax == Bx){
            int menor = Ay < By ? Ay : By;
            int mayor = Ay < By ? By : Ay;
            for(int i = menor; i <= mayor; i++)
                if(table[i][Ax] == 1)
                    return false;
        } else if(Ay == By){
            int menor = Ax < Bx ? Ax : Bx;
            int mayor = Ax < Bx ? Bx : Ax;
            for(int i = menor; i <= mayor; i++)
                if(table[Ay][i] == 1)
                    return false;
        }
        
        return true;
    }
    
    public int[][] ponerEnTablero(int table[][]){
        if(Ax == Bx){
            int menor = Ay < By ? Ay : By;
            int mayor = Ay < By ? By : Ay;
            for(int i = menor; i <= mayor; i++)
                table[i][Ax] = 1;
        } else if(Ay == By){
            int menor = Ax < Bx ? Ax : Bx;
            int mayor = Ax < Bx ? Bx : Ax;
            for(int i = menor; i <= mayor; i++)
                table[Ay][i] = 1;
        }
        return table;
    }
    
    public boolean esVivo(){
        return vivo;
    }
    
    public int getTipo(){
        return tipo;
    }
    
    public void morir(){
        vivo = false;
    }
    
    public int[][] eliminarDelTablero(int table[][]){
        if(Ax == Bx){
            int menor = Ay < By ? Ay : By;
            int mayor = Ay < By ? By : Ay;
            for(int i = menor; i <= mayor; i++)
                table[i][Ax] = -1;
        } else if(Ay == By){
            int menor = Ax < Bx ? Ax : Bx;
            int mayor = Ax < Bx ? Bx : Ax;
            for(int i = menor; i <= mayor; i++)
                table[Ay][i] = -1;
        }
        vivo = false;
        return table;
    }
    
    public boolean teDieron(int x, int y){
        int menorX = Ax <= Bx ? Ax : Bx;
        int mayorX = Ax <= Bx ? Bx : Ax;
        int menorY = Ay <= By ? Ay : By;
        int mayorY = Ay <= By ? By : Ay;
        if((menorX <= x && x <= mayorX) && (menorY <= y && y <= mayorY)){
            System.out.println("te dieron");
            return true;
        }
        return false;
    }
    
    public int getAx(){
        return Ax;
    }
    public int getAy(){
        return Ay;
    }
    public int getBx(){
        return Bx;
    }
    public int getBy(){
        return By;
    }
}
