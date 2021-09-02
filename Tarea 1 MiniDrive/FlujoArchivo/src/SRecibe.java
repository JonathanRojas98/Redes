import java.net.*;
import java.io.*;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;

/**
 *
 * @author axele
 */
public class SRecibe{
    static Socket cl;
    static Socket c2;
    static DataInputStream dis;
    static DataInputStream dis2;
    static DataOutputStream dos;
    static DataOutputStream dos2;
    static ServerSocket s;
    static ServerSocket s2;
    
    static void enviar_directorios(File[] directorios){
        for (File directorio : directorios) {
            if (directorio.isDirectory() && directorio.listFiles()!= null) {
                String path = directorio.getPath() + "\\";
                enviar_directorios(directorio.listFiles(File::isDirectory));
                try{
                    System.out.println("El path es: " + path);
                    //System.out.println("El absoute path es: " + directorio.getAbsolutePath());
                    dos2.writeUTF(path);
                    
                    dos2.flush();
                } catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println(path);
            }
        }
    }
    
    static void enviar_archivos(File[] archivos){
        for (File archivo : archivos) {
            String path = archivo.getAbsolutePath();
            //path = path.replace(".\\", "");
            if (archivo.isDirectory()) {
                enviar_archivos(archivo.listFiles());
            } else {
                try{
                    dis2 = new DataInputStream(new FileInputStream(path));
                    long tam = archivo.length();
                    System.out.println("Tamano del archivo a enviar es de: " + tam);
                    System.out.println("El nombre del archivo es: " + archivo.getName());
                    dos2.writeUTF(path);
                    dos2.flush();
                    dos2.writeLong(tam);
                    dos2.flush();
                    long enviados = 0;
                    int l=0; 
                    int porcentaje=0;
                    while(enviados < tam){
                        byte[] b = (tam < 1500) ? new byte[(int)tam] : new byte[1500];
                        //byte[] b = new byte[1500];
                        l=dis2.read(b);
                        System.out.println("enviados: "+l);
                        dos2.write(b,0,l);// dos.write(b);
                        dos2.flush();
                        enviados = enviados + l;
                        porcentaje = (int)((enviados*100)/tam);
                        System.out.print("\rEnviado el " + porcentaje + " % del archivo");
                    }//while
                    System.out.println("\nArchivo enviado..");
                } catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println(path);
            }
        }
    }
    
    static String getPath(File archivo) throws Exception {
        String nombre = archivo.getPath().replace("\\","/");
        nombre = nombre.split("archivosServer/")[1];
        if(archivo.isDirectory()){
            nombre = nombre + "/";
        }
        return nombre;
    }
    
    static void FileLocalActions(File fileChoose){
        try{
            int i = 0;
            for(File archivo : fileChoose.listFiles()){
                i++;
                System.out.println(getPath(archivo));
            }
            dos2.writeInt(i);
            for(File archivo : fileChoose.listFiles()){
                dos2.writeUTF(getPath(archivo));
            }
            String entradaTeclado = dis2.readUTF();
            String archivoNombre = dis2.readUTF();
            if(entradaTeclado.contains("abrir")){
                File js = new File(new java.io.File(".").getCanonicalPath() + "/archivosServer/" + archivoNombre);
                FileLocalActions(js);
            } else if(entradaTeclado.contains("bajar")){
                File js = new File(new java.io.File(".").getCanonicalPath() + "/archivosServer/" + archivoNombre);
                if(js.isDirectory()){
                    enviar_directorios(js.listFiles());
                    enviar_archivos(js.listFiles());
                    
                } else{
                    File[] archivos = new File[1];
                    archivos[0] = js;
                    enviar_archivos(archivos);
                    
                }
            } else if(entradaTeclado.contains("borrar")){
                File archivo = new File(new java.io.File(".").getCanonicalPath() + "/archivosServer/" + archivoNombre); 
                if (archivo.delete()) { 
                  dos2.writeUTF("Archivo borrado: " + archivo.getName());
                } else {
                  dos2.writeUTF("Error al borrar el archivo.");
                } 
            }
        } catch(Exception e){
            e.printStackTrace();
        }//catch
    }
        
        
        
    public static void main(String[] args){
      try{
          int pto = 50000;
          s = new ServerSocket(pto);
          s2 = new ServerSocket(pto+1);
          s.setReuseAddress(true);
          s2.setReuseAddress(true);
          System.out.println("Servidor iniciado esperando por archivos..");
          
          for(;;){
              cl = s.accept();
              c2 = s2.accept();
              System.out.println("Cliente conectado desde "+cl.getInetAddress()+":"+cl.getPort() + " y " + c2.getInetAddress()+":"+c2.getPort());
              dis = new DataInputStream(cl.getInputStream());
              dos = new DataOutputStream(cl.getOutputStream());
              dis2 = new DataInputStream(c2.getInputStream());
              dos2 = new DataOutputStream(c2.getOutputStream());
              
              //PARA RECIBIR DATOS!
              while(true){
                String nombre = dis.readUTF();
                String peticion = dis2.readUTF();
                
                System.out.println("Aqui");
                System.out.println(nombre);
                System.out.println(peticion);
                System.out.println("Aqui 2");
                
                if(peticion.compareToIgnoreCase("servidor") == 0){
                    FileLocalActions(new File("./archivosServer"));
                    System.out.println("Estas en el servidor");
                    Thread.sleep(100);
                    dos2.writeUTF("fin");
                    dos2.flush();
                }
                
                
                
                if(nombre.compareToIgnoreCase("fin") == 0){
                    System.out.println("Cliente desconectado");
                    dis.close();
                    cl.close();
                    break;
                } else if (nombre.length() > 1 && nombre.substring(nombre.length() - 1).contains("\\")){
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
