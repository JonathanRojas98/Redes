import java.net.*;
import java.io.*;

/**
 *
 * @author axel
 */
public class CHM {
  public static void main(String[] args){
      try{
          //Socket cl = new Socket("2001::1234:1",8000);
          Socket cl = new Socket("localhost",8000);
          System.out.println("Conexion con servidor establecida.. recibiendo datos");
          System.out.println("Buffer de lectura mide:"+cl.getReceiveBufferSize());
          System.out.println("Buffer de escritura mide:"+cl.getSendBufferSize());
          BufferedInputStream bis = new BufferedInputStream(cl.getInputStream());
          byte[] b= new byte[1024];
          int n= bis.read(b);
          String datos = new String(b,0,n);
          System.out.println("Mensaje recibido:"+datos);
          System.out.println("Temp linger: "+cl.getSoLinger());
          System.out.println("Temp timeout: "+cl.getSoTimeout());
          cl.setSoLinger(true, 5); //segundos
          cl.setSoTimeout(1000); //milisegundos
          System.out.println("Temp linger: "+cl.getSoLinger());
          System.out.println("Temp timeout: "+cl.getSoTimeout());
          bis.close();
          cl.close();
      }catch(Exception e){
          e.printStackTrace();
      }
  }//main    
}
