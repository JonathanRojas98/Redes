import java.net.*;
import java.io.*;
/**
 *
 * @author axel
 */
public class SHM {
    public static void main(String[] args){
        try{
        ServerSocket s = new ServerSocket(8000);
        s.setReuseAddress(true);
            System.out.println("Servicio iniciado.. esperando cliente..");
        for(;;){
            Socket cl = s.accept();
            System.out.println("Cliente conectado desde->"+cl.getInetAddress()+":"+cl.getPort());
            System.out.println("Tamanio de buffer lectura:"+cl.getReceiveBufferSize());
            System.out.println("Tamanio de buffer escritura:"+cl.getSendBufferSize());
            String msj ="Hola mundo desde el servidor..";
            byte[] b = msj.getBytes();
            //PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedOutputStream bos = new BufferedOutputStream(cl.getOutputStream());
            bos.write(b);
            bos.flush();
            System.out.println("Temp linger: "+cl.getSoLinger());
          System.out.println("Temp timeout: "+cl.getSoTimeout());
          cl.setSoLinger(true, 8); //segundos
          cl.setSoTimeout(900); //milisegundos
          System.out.println("Temp linger: "+cl.getSoLinger());
          System.out.println("Temp timeout: "+cl.getSoTimeout());
            bos.close();
            cl.close();
        }//for
    }catch(Exception e){
    e.printStackTrace();
    }//catch
    }
}
