import java.net.*;
import java.io.*;
/**
 *
 * @author axele
 */
public class SEcoD2 {
    public static void main(String[] args){
        try{
            int pto = 1234,max=65535;
            DatagramSocket s = new DatagramSocket(pto);
                    System.out.println("Servidor de datagrama iniciado en el puerto "+s.getLocalPort());
                    while(true){
                        DatagramPacket p = new DatagramPacket(new byte[max],max);
                        s.receive(p);
                        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
                        Datos dd = (Datos)ois.readObject();
                        if(dd.getTotal() == 1){
                            String msj = new String(dd.getdatos(),0,dd.getdatos().length);
                            //String msj = new String(p.getData(),0,p.getLength());
                            System.out.println("Segmento:"+dd.getnp()+"  Datos recibidos: "+msj+  " Total de paquetes a recibir: " + dd.getTotal() + " Devolviendo eco..");
                            s.send(p);
                        } else{
                            Datos[] ddArray = new Datos[dd.getTotal()];
                            ddArray[dd.getnp()] = dd;
                            s.send(p);
                            System.out.println("Se necesita un total de: " + dd.getTotal());
                            for(int i = 1; i < dd.getTotal(); i++){
                                p = new DatagramPacket(new byte[max],max);
                                s.receive(p);
                                ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
                                
                                Datos recibido = (Datos)ois.readObject();
                                ddArray[recibido.getnp()] = recibido;
                                System.out.println("Número de paquete recibido: " + recibido.getnp());
                                s.send(p);
                            }
                            System.out.println("Ya saliste");
                            for(Datos elementoDatos : ddArray){
                                String msj = new String(elementoDatos.getdatos(),0,elementoDatos.getdatos().length);
                                System.out.println("Segmento:"+elementoDatos.getnp()+"  Datos recibidos: "+msj+  " Total de paquetes a recibir: " + elementoDatos.getTotal() + " Devolviendo eco..");
                            }
                        }
                        //s.send(p);
                    }//while
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}//class
