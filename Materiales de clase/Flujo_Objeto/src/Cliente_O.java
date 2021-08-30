
import java.net.*;
import java.io.*;

public class Cliente_O {

    public static void main(String[] args) {
        try {
            Socket cl = new Socket("localhost", 3000);
            System.out.println("Conexion con servidor exitosa.. preparado para recibir objeto..");
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            Objeto ob2 = (Objeto) ois.readObject();
            System.out.println("Objeto recibido desde " + cl.getInetAddress() + ":" + cl.getPort() + " con los datos:");
            System.out.println("x:" + ob2.getX() + " y:" + ob2.getY() + " Z:" + ob2.getZ());
            Objeto ob = new Objeto(4, 5.0f, "seis");
            System.out.println("Enviando objeto con los datos\nX:" + ob.getX() + " Y:" + ob.getY() + " Z: " + ob.getZ());
            oos.writeObject(ob);
            oos.flush();
            System.out.println("Objeto enviado..");
            ois.close();
            oos.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//main

}
