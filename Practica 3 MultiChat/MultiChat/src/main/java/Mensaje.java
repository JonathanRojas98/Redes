
import java.io.Serializable;
import java.util.HashSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jonat
 */
public class Mensaje implements Serializable {
    public String from;
    public String message;
    public boolean isPublic;
    public HashSet<String> forUsers;
    public boolean isSticker;
    
    public Mensaje(String from, String message, boolean isPublic, boolean isSticker, HashSet<String> forUsers){
        this.from = from;
        this.message = message;
        this.isPublic = isPublic;
        this.forUsers = forUsers;
        this.isSticker = isSticker;
    }
    
    public Mensaje(String from, String message, boolean isPublic, boolean isSticker){
        this.from = from;
        this.message = message;
        this.isPublic = isPublic;
        this.isSticker = isSticker;
    }
}
