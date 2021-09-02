import java.net.*;
import java.io.*;
import javax.swing.JFileChooser;
/**
 *
 * @author axele
 */
public class CEnvia {
    static DataOutputStream dos;
    static DataInputStream dis;
    static Socket cl;
    
    static void enviar_directorios(File[] directorios){
        for (File directorio : directorios) {
            if (directorio.isDirectory()) {
                String path = directorio.getAbsolutePath() + "\\";
                enviar_directorios(directorio.listFiles(File::isDirectory));
                try{
                    dos.writeUTF(path);
                    dos.flush();
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
                    dos.writeLong(tam);
                    dos.flush();
                    long enviados = 0;
                    int l=0, porcentaje=0;
                    while(enviados < tam){
                        byte[] b = (tam < 1500) ? new byte[(int)tam] : new byte[1500];
                        //byte[] b = new byte[1500];
                        l=dis.read(b);
                        System.out.println("enviados: "+l);
                        dos.write(b,0,l);// dos.write(b);
                        dos.flush();
                        enviados = enviados + l;
                        porcentaje = (int)((enviados*100)/tam);
                        System.out.print("\rEnviado el "+porcentaje+" % del archivo");
                    }//while
                    System.out.println("\nArchivo enviado..");
                } catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println(path);
            }
        }
    }
    
    
    public static void main(String[] args){
        try{
            int pto = 8000;
            String dir = "127.0.0.1";
            cl = new Socket(dir,pto);
            System.out.println("Conexion con servidor establecida..");
            
            
            //ESTO ES PARA ENVIAR ARCHIVOS
           
            JFileChooser jf = new JFileChooser(new File("."));
            jf.setMultiSelectionEnabled(true);
            jf.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int r = jf.showOpenDialog(null);
            if(r==JFileChooser.APPROVE_OPTION){
                dos = new DataOutputStream(cl.getOutputStream());
                File[] archivos = jf.getSelectedFiles();
                enviar_directorios(archivos);
                enviar_archivos(archivos);
                dos.writeUTF("fin");
                dos.close();
                cl.close();
            }
            
            //PARA ENVIAR ARCHIVOS
            
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
