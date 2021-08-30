import java.net.*;
import java.io.*;

public class Servidor_O {

public static void main(String[] args)throws Exception{
    try{
	ServerSocket ss = new ServerSocket(3000);
	System.out.println("Servidor iniciado");
        for(;;){
            Socket cl = ss.accept();
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            Objeto ob2 = new Objeto(1,2.0f,"tres");
            oos.writeObject(ob2);
            oos.flush();
            System.out.println("Cliente conectado.. Enviando objeto con los datos\nX:"+ob2.getX()+" Y:"+ob2.getY()+" Z:"+ob2.getZ() );

            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            Objeto ob = (Objeto)ois.readObject();
            /////////////////////////////////////////// Esto es para validar si lo que llegó 
            /*Object oo = ois.readObject();
            if(oo instanceof Objeto){
                Objeto dato = (Objeto)oo;
            }*/
            ///////////////////////////////////////////
            System.out.println("Objeto recibido desde"+cl.getInetAddress()+":"+cl.getPort()+" con los datos");
            System.out.println("x:"+ob.getX()+" y:"+ob.getY()+" Z:"+ob.getZ());
            ois.close();
            oos.close();
            cl.close();
        }//for   
    }catch(Exception e){
        e.printStackTrace();
    }
	}//main
	
}