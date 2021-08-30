import java.net.*;
import java.io.*;
/**
 *
 * @author axele
 */
public class CEcoFlujo {
    public static void main(String[] args){
        try{
            int pto = 1234;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "ISO-8859-1"));//"Windows-1250"
            InetAddress host = null;
            String dir="";
            try{
                System.out.println("Escribe la direccion del servidor:");
                dir = br.readLine();
                host = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                main(args);
            }//catch
            Socket cl = new Socket(host,pto);
            System.out.println("Conexion con el servidor "+dir+":"+pto+" establecida\n");
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            while(true){
                System.out.println("Escribe un mensaje, <ENTER> para enviar, \"salir\" para terminar");
                String mensaje = br.readLine(); //Integer.MAX_VALUE
                pw.println(mensaje);
                pw.flush();//Esto no es necesari, pero obliga el envío del mensaje
                if(mensaje.compareToIgnoreCase("salir")==0){
                    br.close();
                    br1.close();
                    pw.close();
                    System.exit(0);
                } else{
                    String eco = br1.readLine();
                    System.out.println("Eco recibido desde "+cl.getInetAddress()+":"+cl.getPort()+" "+eco);
                }//else
            }//while
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }//main
}
