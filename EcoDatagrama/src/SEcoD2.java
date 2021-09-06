import java.net.*;
import java.io.*;
/**
 *
 * @author axele
 */
public class SEcoD2 {
    public static void main(String[] args){
        try{
            int pto = 1234,max=65535,i=0;
            int j=0;
            DatagramSocket s = new DatagramSocket(pto);
                    System.out.println("Servidor de datagrama iniciado en el puerto "+s.getLocalPort());
                    
                    try {
                         String[] elementos = new String [873];                     
                            while(true){
                                DatagramPacket p = new DatagramPacket(new byte[max],max);
                                s.receive(p);
                                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
                                Datos dd = (Datos)ois.readObject();
                                
                                String msj = new String(dd.getdatos(),0,dd.getdatos().length);
                             //String msj = new String(p.getData(),0,p.getLength());
                                elementos[dd.getnp()] = msj;
                                i++;
                                System.out.println("dd.getnp() = " + dd.getnp() + ".");
                                System.out.println("Prueba - Segmento t: "+j+" de "+dd.getT()+"  Datos recibidos: "+elementos[j]+ " Devolviendo eco..");
                                if(dd.getnp()==j){
                                    System.out.println("Segmento: "+dd.getnp()+" de "+dd.getT()+"  Datos recibidos: "+msj+ " Devolviendo eco.. \n");
                                    s.send(p);
                                    j++;
                                }else{
                                    System.out.println("Segmento t: "+j+" de "+dd.getT()+" Datos recibidos: "+elementos[j]+ " Devolviendo eco..\n");
                                    j++;
                                    s.send(p);
                                }
                                
                                
                            }//while
                           
                        } catch (ArrayIndexOutOfBoundsException e) {
                             System.out.println("Array creado muy pequeño");
                        }
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}//class
