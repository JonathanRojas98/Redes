import java.net.*;
import java.io.*;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 * @author Axel
 */
class Envia extends Thread{
    MulticastSocket socket;
    BufferedReader br;
    public Envia(MulticastSocket m, BufferedReader br){
        this.socket=m;
        this.br=br;
        
    }
    public void run(){
     try{
        //BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
        String dir= "230.1.1.1";
        int pto=1234;
        InetAddress gpo = InetAddress.getByName(dir);

        for(;;){
            System.out.println("Escribe un mensaje para ser enviado:");
            String mensaje= br.readLine();
            byte[] b = mensaje.getBytes();
            DatagramPacket p = new DatagramPacket(b,b.length,gpo,pto);
            socket.send(p);
        }//for
     }catch(Exception e){
         e.printStackTrace();
     }//catch
     }//run
}//class

class Recibe extends Thread{
    MulticastSocket socket;
    public Recibe(MulticastSocket m){
        this.socket=m;
    }
    public void run(){
       try{
           
        for(;;){
           DatagramPacket p = new DatagramPacket(new byte[65535],65535);
            System.out.println("Listo para recibir mensajes...");
           socket.receive(p);
           String msj = new String(p.getData(),0,p.getLength());
            System.out.println("Mensaje recibido: "+msj);
       } //for
       }catch(Exception e){
           e.printStackTrace();
       }//catch
    }//run
}//class


public class Principal {
    
        static void despliegaInfoNIC(NetworkInterface netint) throws SocketException {
        System.out.printf("Nombre de despliegue: %s\n", netint.getDisplayName());
        System.out.printf("Nombre: %s\n", netint.getName());
        String multicast = (netint.supportsMulticast())?"Soporta multicast":"No soporta multicast";
        System.out.printf("Multicast: %s\n", multicast);
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            System.out.printf("Direccion: %s\n", inetAddress);
        }
        System.out.printf("\n");
     }

    public static void main(String[] args){
        try{
            int pto = 1234;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in,"ISO-8859-1"));
            NetworkInterface ni = NetworkInterface.getByIndex(1);
            MulticastSocket m= new MulticastSocket(pto);
            m.setReuseAddress(true);
            m.setTimeToLive(255);
            String dir= "230.1.1.1";
            InetAddress gpo = InetAddress.getByName(dir);
            SocketAddress dirm;
            try{
                dirm = new InetSocketAddress(gpo,pto);
            }catch(Exception e){
                e.printStackTrace();
                return;
            }
            m.joinGroup(dirm, ni);


            Recibe r = new Recibe(m);
            Envia e = new Envia(m, br);
            e.setPriority(10);
            r.start();
            e.start();
            r.join();
            e.join();
        } catch(Exception e){}
    }//main  
}
