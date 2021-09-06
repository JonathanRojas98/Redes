import java.io.Serializable;
/**
 *
 * @author axele
 */
public class Datos implements Serializable {
    int np;
    int T;
    byte[] datos;
    
    public Datos(int np, byte[] datos, int T){
        this.np=np;
        this.datos=datos.clone();
        this.T=T;
    }//constructor
    
    int getnp(){
        return this.np;
    }
    
    int getT(){
        return this.T;
    }

    byte[] getdatos(){
        return this.datos.clone();
    }
}
