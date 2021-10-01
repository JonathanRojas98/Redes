import java.io.Serializable;
/**
 *
 * @author axele
 */
public class Datos implements Serializable {
    int np;
    byte[] datos;
    int total;
    
    public Datos(int np, byte[] datos, int total){
        this.np=np;
        this.datos=datos.clone();
        this.total = total;
    }//constructor
    
    int getnp(){
        return this.np;
    }
    
    byte[] getdatos(){
        return this.datos.clone();
    }
    
    int getTotal(){
        return this.total;
    }
}
