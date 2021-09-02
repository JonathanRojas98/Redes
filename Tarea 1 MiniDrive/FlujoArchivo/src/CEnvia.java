import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JFileChooser;
/**
 *
 * @author axele
 */
public class CEnvia {
    static DataOutputStream dos;
    static DataOutputStream dos2;
    static DataInputStream dis;
    static DataInputStream dis2;
    static Socket cl;
    static Socket c2;
    
    static void enviar_directorios(File[] directorios){
        for (File directorio : directorios) {
            if (directorio.isDirectory() && directorio.listFiles()!= null) {
                String path = directorio.getPath() + "\\";
                enviar_directorios(directorio.listFiles(File::isDirectory));
                try{
                    System.out.println("El path es: " + path);
                    //System.out.println("El absoute path es: " + directorio.getAbsolutePath());
                    dos.writeUTF(path);
                    dos.flush();
                    dos2.writeUTF("");
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
                    dis = new DataInputStream(new FileInputStream(path));
                    long tam = archivo.length();
                    System.out.println("Tamano del archivo a enviar es de: " + tam);
                    System.out.println("El nombre del archivo es: " + archivo.getName());
                    dos.writeUTF(path);
                    dos.flush();
                    dos2.writeUTF("");
                    dos2.flush();
                    dos.writeLong(tam);
                    dos.flush();
                    long enviados = 0;
                    int l=0; 
                    int porcentaje=0;
                    while(enviados < tam){
                        byte[] b = (tam < 1500) ? new byte[(int)tam] : new byte[1500];
                        //byte[] b = new byte[1500];
                        l=dis.read(b);
                        System.out.println("enviados: "+l);
                        dos.write(b,0,l);// dos.write(b);
                        dos.flush();
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
        nombre = nombre.split("archivos/")[1];
        if(archivo.isDirectory()){
            nombre = nombre + "/";
        }
        return nombre;
    }
    
    static void FileLocalActions(File fileChoose){
        try{
            for(File archivo : fileChoose.listFiles()){
                System.out.println(getPath(archivo));
            }
            System.out.println("Escribe la acción a hacer y después el nombre del archivo o directorio: abrir, subir, borrar");
            Scanner entradaEscaner = new Scanner(System.in);
            String entradaTeclado = entradaEscaner.nextLine();
            entradaEscaner = new Scanner(System.in);
            String archivoNombre = entradaEscaner.nextLine();
            if(entradaTeclado.contains("abrir")){
                File js = new File(new java.io.File(".").getCanonicalPath() + "/archivos/" + archivoNombre);
                FileLocalActions(js);
            } else if(entradaTeclado.contains("subir")){
                File js = new File(new java.io.File(".").getCanonicalPath() + "/archivos/" + archivoNombre);
                if(js.isDirectory()){
                    enviar_directorios(js.listFiles());
                    enviar_archivos(js.listFiles());
                } else{
                    File[] archivos = new File[1];
                    archivos[0] = js;
                    enviar_archivos(archivos);
                }
            } else if(entradaTeclado.contains("borrar")){
                File archivo = new File(new java.io.File(".").getCanonicalPath() + "/archivos/" + archivoNombre); 
                if (archivo.delete()) { 
                  System.out.println("Archivo borrado: " + archivo.getName());
                } else {
                  System.out.println("Error al borrar el archivo.");
                } 
            }
        } catch(Exception e){
            e.printStackTrace();
        }//catch
    }
    
    static void FileServerActions(){
        try{
            int cantidadFiles = dis2.readInt();
            for(int i = 0; i < cantidadFiles; i++){
                System.out.println(dis2.readUTF());
            }
            System.out.println("Escribe la acción a hacer y después el nombre del archivo o directorio: abrir, bajar, borrar");
            Scanner entradaEscaner = new Scanner(System.in);
            String entradaTeclado = entradaEscaner.nextLine();
            entradaEscaner = new Scanner(System.in);
            String archivoNombre = entradaEscaner.nextLine();
            if(entradaTeclado.contains("abrir")){
                dos2.writeUTF("abrir");
                dos2.writeUTF(archivoNombre);
                FileServerActions();
                //File js = new File(new java.io.File(".").getCanonicalPath() + "/archivos/" + archivoNombre);
                //FileLocalActions(js);
            } else if(entradaTeclado.contains("bajar")){
                dos2.writeUTF("bajar");
                dos2.writeUTF(archivoNombre);
                
                while(true){
                    System.out.println(".");
                    String nombre = dis2.readUTF();
                    System.out.println(nombre);
                    if(nombre.compareToIgnoreCase("fin") == 0){
                        //System.out.println("Cliente desconectado");
                        //dis.close();
                        //cl.close();
                        break;
                    } else if (nombre.length() > 1 && nombre.substring(nombre.length() - 1).contains("\\")){
                        String directorio = nombre.replace("\\", "\\\\").split("archivosServer\\\\")[1];
                        directorio = directorio.replace("\\\\", "/");
                        directorio = directorio.substring(1);
                        new File("./archivos/" + directorio).mkdirs();

                        //System.out.println(directorio);
                    } else{
                        long tam = dis2.readLong();
                        nombre = nombre.replace("\\", "\\\\").split("archivosServer\\\\")[1];
                        nombre = nombre.replace("\\\\", "/");
                        nombre = nombre.substring(1);
                        System.out.println(nombre);
                        System.out.println("Comienza descarga del archivo "+nombre+" de "+tam+" bytes\n\n");
                        DataOutputStream dos = new DataOutputStream(new FileOutputStream("./archivos/" + nombre));
                        long recibidos=0;
                        int l=0, porcentaje=0;
                        while(recibidos < tam){
                            byte[] b = (tam < 1500) ? new byte[(int)tam] : new byte[1500];
                            l = dis2.read(b);
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
                /*File js = new File(new java.io.File(".").getCanonicalPath() + "/archivos/" + archivoNombre);
                if(js.isDirectory()){
                    enviar_directorios(js.listFiles());
                    enviar_archivos(js.listFiles());
                } else{
                    File[] archivos = new File[1];
                    archivos[0] = js;
                    enviar_archivos(archivos);
                }*/
            } else if(entradaTeclado.contains("borrar")){
                dos2.writeUTF("borrar");
                dos2.writeUTF(archivoNombre);
                System.out.println(dis2.readUTF());
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        try{
            int pto = 50000;
            int pto1 = pto + 1;
            String dir = "127.0.0.1";
            cl = new Socket(dir,pto);
            c2 = new Socket(dir,pto1);
            System.out.println("Conexion con servidor establecida..");
            
            for(;;){
                /*JFileChooser jf = new JFileChooser(new File("./archivos"));
                jf.setMultiSelectionEnabled(true);
                jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);*/
                System.out.println("Para ver los archivos de la carpeta local, escriba 'local', para ver los archivos del servidor, escriba 'servidor', sino 'salir' para salir:");
                String entradaTeclado = "";
                Scanner entradaEscaner = new Scanner (System.in);
                entradaTeclado = entradaEscaner.nextLine();
                dos = new DataOutputStream(cl.getOutputStream());
                dos2 = new DataOutputStream(c2.getOutputStream());
                dis2 = new DataInputStream(c2.getInputStream());
                if(entradaTeclado.contains("local")){
                    System.out.println("Elegiste local, mostrando los archivos locales:");
                    FileLocalActions(new File("./archivos"));
                } else if(entradaTeclado.contains("servidor")){
                    System.out.println("Elegiste servidor, mostrando los archivos del servidor:");
                    dos.writeUTF("");
                    dos.flush();
                    dos2.writeUTF("servidor");
                    dos2.flush();
                    FileServerActions();
                } else if(entradaTeclado.contains("salir")){
                    break;
                }
            }
            
            dos.close();
            dos2.close();
            cl.close();
            c2.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
