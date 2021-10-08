import java.net.*;
import java.io.*;

/**
 *
 * @author axele
 */
public class CHMD {
    public static void main(String[] args){
      try{  
          int pto=1234;
          String dir="127.0.0.1";
         // String dir ="255.255.255.255";
          InetAddress dst = InetAddress.getByName(dir);
          String msj="mensaje enviado mediante datagramas..";
          byte[] b=msj.getBytes();
          DatagramSocket cl = new DatagramSocket();
          cl.setBroadcast(true);
          DatagramPacket p = new DatagramPacket(b,b.length,dst,pto);
          cl.send(p);
          System.out.println("Se ha enviado datagrama a "+p.getAddress()+":"+p.getPort()+" con el mensaje:"+msj);
          cl.close();
      }catch(Exception e){
          e.printStackTrace();
      }//catch
    }//main
    
}
