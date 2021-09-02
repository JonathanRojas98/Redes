import java.net.*;
import java.io.*;
import java.util.regex.Pattern;

/**
 *
 * @author axele
 */
public class SRecibe {
    public static void main(String[] args){
      try{
          int pto = 8000;
          ServerSocket s = new ServerSocket(pto);
          ServerSocket s2 = new ServerSocket(pto+1);
          s.setReuseAddress(true);
          System.out.println("Servidor iniciado esperando por archivos..");
          
          for(;;){
              Socket cl = s.accept();
              System.out.println("Cliente conectado desde "+cl.getInetAddress()+":"+cl.getPort());
              DataInputStream dis = new DataInputStream(cl.getInputStream());
              
              
              
              
              //PARA RECIBIR DATOS!
              while(true){
                String nombre = dis.readUTF();
                if(nombre.compareToIgnoreCase("fin") == 0){
                    System.out.println("Cliente desconectado");
                    dis.close();
                    cl.close();
                    break;
                } else if (nombre.substring(nombre.length() - 1).contains("\\")){
                    String directorio = nombre.replace("\\", "\\\\").split("archivos\\\\")[1];
                    directorio = directorio.replace("\\\\", "/");
                    directorio = directorio.substring(1);
                    new File("./archivosServer/" + directorio).mkdirs();
                    
                    //System.out.println(directorio);
                } else{
                    long tam = dis.readLong();
                    nombre = nombre.replace("\\", "\\\\").split("archivos\\\\")[1];
                    nombre = nombre.replace("\\\\", "/");
                    nombre = nombre.substring(1);
                    System.out.println(nombre);
                    System.out.println("Comienza descarga del archivo "+nombre+" de "+tam+" bytes\n\n");
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream("./archivosServer/" + nombre));
                    long recibidos=0;
                    int l=0, porcentaje=0;
                    while(recibidos < tam){
                        byte[] b = (tam < 1500) ? new byte[(int)tam] : new byte[1500];
                        l = dis.read(b);
                        System.out.println("leidos: "+l);
                        dos.write(b,0,l);
                        dos.flush();
                        recibidos = recibidos + l;
                        porcentaje = (int)((recibidos*100)/tam);
                        System.out.print("\rRecibido el "+ porcentaje +" % del archivo");
                    }//while
                    System.out.println("Archivo recibido..");
                    //
                }
              }
              //PARA ENVIAR DATOS!
              
          }//for
          
      }catch(Exception e){
          e.printStackTrace();
      }  
    }//main
}
