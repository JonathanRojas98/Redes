
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jonat
 */
public class Chat extends javax.swing.JFrame {
    static String usuario;
    static MulticastSocket m;
    static DefaultTableModel model;
    static HashSet<String> usuarios = new HashSet<String>();
    /*static int pto;
    static BufferedReader br;
    static NetworkInterface ni;
    static MulticastSocket m;
    static String dir;
    static InetAddress gpo;
    static SocketAddress dirm;*/
    /**
     * Creates new form Chat
     */
    public Chat() {
        initComponents();
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        jEditorPane1.setEditorKit(htmlEditorKit);
        jEditorPane1.setText("<html><body id='body' style=\"background-color:#FF6666\"></body></html>");
        jScrollPane1.setEnabled(false);
        jTable1.setEnabled(false);
        jTable1.setBackground(new Color(255,102,102));
        String baseURL = "C:\\Users\\jonat\\OneDrive\\Escritorio\\Redes\\Practica 3 MultiChat\\MultiChat\\src\\stickers\\";
        ImageIcon imgThisImg = new ImageIcon(new ImageIcon(baseURL + "tinder.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        jLabel3.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "hi.gif").getImage().getScaledInstance(110, 110, Image.SCALE_DEFAULT));
        jLabel4.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "1.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        jButton3.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "2.gif").getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        jButton4.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "3.gif").getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        jButton5.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "4.gif").getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        jButton6.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "5.gif").getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        jButton7.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "6.gif").getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        jButton8.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "7.gif").getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        jButton9.setIcon(imgThisImg);
        imgThisImg = new ImageIcon(new ImageIcon(baseURL + "8.gif").getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        jButton10.setIcon(imgThisImg);
        jButton2.setEnabled(false);
        
        model = new DefaultTableModel()
        {
          public Class<?> getColumnClass(int column)
          {
            switch(column)
            {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            default:
                return String.class;
            }
          }
        };
        //ASSIGN THE MODEL TO TABLE
        jTable1.setModel(model);
        model.addColumn("");
        model.addColumn("User");
        model.addRow(new Object[0]);
        model.setValueAt(true,0,0);
        model.setValueAt("Público", 0, 1);
    }
    
    
    class Recibe extends Thread{
        MulticastSocket socket;
        public Recibe(MulticastSocket m){
            this.socket = m;
        }
        public void run(){
           try{
                for(;;){
                    DatagramPacket p = new DatagramPacket(new byte[65535],65535);
                    socket.receive(p);
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
                    Mensaje mensaje = (Mensaje)ois.readObject();
                    System.out.println("Mensaje recibido: " + mensaje.message + " de " + mensaje.from);
                    //CASO DE CUANDO ALGUIEN SE CONECTA Y NO SOY YO
                    String message = mensaje.message;
                    String from = mensaje.from;
                    boolean isPublic = mensaje.isPublic;
                    boolean isSticker = mensaje.isSticker;
                    HashSet<String> forUsers = mensaje.forUsers;
                    if(message.equals("token:123456") && !from.equals(usuario)){
                        boolean yaEsta = false;
                        for(String user : usuarios)
                            if(user.equals(from))
                                yaEsta = true;

                        if(!yaEsta){
                            usuarios.add(from);
                            int rowMax = model.getRowCount();
                            model.addRow(new Object[0]);
                            model.setValueAt(false, rowMax, 0);
                            model.setValueAt(mensaje.from, rowMax, 1);
                            enviaMensaje(new Mensaje(usuario, "actualiza:123456", true, false));
                        }
                    } else if(message.equals("actualiza:123456") && !from.equals(usuario)){
                        boolean yaEsta = false;
                        for(String user : usuarios)
                            if(user.equals(from))
                                yaEsta = true;

                        if(!yaEsta){
                            usuarios.add(from);
                            int rowMax = model.getRowCount();
                            model.addRow(new Object[0]);
                            model.setValueAt(false, rowMax, 0);
                            model.setValueAt(mensaje.from, rowMax, 1);
                        }
                    } else if(!message.equals("actualiza:123456") && !message.equals("token:123456")){
                        //SIN STICKER
                        boolean caso = false;
                        String añadirTexto = "";
                        if(from.equals(usuario) && !isSticker){
                            caso = true;
                            añadirTexto = añadirTexto + "(Yo): " + message + "</div>";
                        } else if(isPublic && !isSticker){
                            caso = true;
                            añadirTexto = añadirTexto + "(" + from + "): " + message;
                        } else if(!isPublic && estoyEnLista(forUsers) && !isSticker){
                            caso = true;
                            añadirTexto = añadirTexto + "Privado de " + "(" + from + "): " + message;
                        }
                        
                        
                        
                        //jEditorPane1.setText(añadirTexto);
                        
                        
                        
                        
                        //CON STICKER
                        String baseURL = "C:/Users/jonat/OneDrive/Escritorio/Redes/Practica 3 MultiChat/MultiChat/src/stickers/";
                        if(from.equals(usuario) && isSticker){
                            caso = true;
                            añadirTexto = "Yo:<img src='file:" + baseURL + message + "' width = 50 height=50></img>";
                        } else if(isPublic && isSticker){
                            caso = true;
                            añadirTexto = "De " + from + ":<img src='file:" + baseURL + message + "' width = 50 height=50></img>";
                        } else if(!isPublic && isSticker && estoyEnLista(forUsers)){
                            caso = true;
                            añadirTexto = "Privado De " + from + ":<img src='file:" + baseURL + message + "' width = 50 height=50></img>";
                        }
                        
                        if(caso){
                            if(!isPublic && estoyEnLista(forUsers))
                                añadirTexto = "<p style=\"color:#76b3ff\">" + añadirTexto + "</p>";
                            else
                                añadirTexto = "<p style=\"color:#ffffff\">" + añadirTexto + "</p>";
                            if(from.equals(usuario))
                                añadirTexto = "<div style=\"text-align: right\">" + añadirTexto + "</div>";
                            
                            HTMLDocument doc = (HTMLDocument) jEditorPane1.getDocument();
                            Element elem = doc.getElement("body");
                            String htmlText = String.format("%s", añadirTexto);
                            try {
                                System.out.println(htmlText);
                                doc.insertBeforeEnd(elem, htmlText);
                            } catch (BadLocationException | IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        //System.out.println(jEditorPane1.getText());
                        
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean estoyEnLista(HashSet<String> forUsers){
        for(String usuario1 : forUsers)
            if(usuario1.equals(usuario))
                return true;
        return false;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 102, 102));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Usuario");

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setForeground(new java.awt.Color(0, 0, 0));
        jTextField1.setToolTipText("");
        jTextField1.setSelectedTextColor(new java.awt.Color(0, 0, 0));

        jButton1.setBackground(new java.awt.Color(255, 102, 102));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Conectar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(51, 204, 255));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jTable1.setBackground(new java.awt.Color(255, 102, 102));
        jTable1.setForeground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(255, 102, 102));
        jTable1.setSelectionBackground(new java.awt.Color(255, 102, 102));
        jTable1.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(jTable1);

        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setForeground(new java.awt.Color(0, 0, 0));
        jTextField2.setToolTipText("");
        jTextField2.setSelectedTextColor(new java.awt.Color(0, 0, 0));

        jButton2.setBackground(new java.awt.Color(255, 102, 102));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Enviar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 102, 102));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CHAT DEL AMOR");

        jButton4.setBackground(new java.awt.Color(255, 102, 102));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 102, 102));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 102, 102));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 102, 102));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 102, 102));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 102, 102));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(255, 102, 102));
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setBackground(new java.awt.Color(255, 102, 102));
        jScrollPane2.setBorder(null);
        jScrollPane2.setForeground(new java.awt.Color(255, 102, 102));

        jEditorPane1.setEditable(false);
        jEditorPane1.setBackground(new java.awt.Color(255, 102, 102));
        jEditorPane1.setBorder(null);
        jEditorPane1.setContentType("text/html");
        jEditorPane1.setForeground(new java.awt.Color(255, 102, 102));
        jEditorPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jEditorPane1.setDisabledTextColor(new java.awt.Color(255, 102, 102));
        jEditorPane1.setSelectedTextColor(new java.awt.Color(255, 102, 102));
        jEditorPane1.setSelectionColor(new java.awt.Color(255, 102, 102));
        jScrollPane2.setViewportView(jEditorPane1);
        jEditorPane1.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(36, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        //BOTÓN STICKER 4
        //CASO DE TODOS
        if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
            enviaMensaje(new Mensaje(usuario, "4.gif", true, true));
        } else{
            HashSet<String> usuariosEnviar = new HashSet<String>();
            for(int i = 1; i < model.getRowCount(); i++){
                if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                    usuariosEnviar.add(model.getValueAt(i, 1).toString());
            }
            enviaMensaje(new Mensaje(usuario, "4.gif", false, true, usuariosEnviar));
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //BOTON CONECTAR
        
        
        try{
            int pto = 1234;
            //BufferedReader br = new BufferedReader(new InputStreamReader(System.in,"ISO-8859-1"));
            NetworkInterface ni = NetworkInterface.getByIndex(1);
            m = new MulticastSocket(pto);
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
            r.start();
            usuario = jTextField1.getText();
            jTextField1.setEnabled(false);
            jButton1.setEnabled(false);
            jButton2.setEnabled(true);
            jScrollPane1.setEnabled(true);
            jTable1.setEnabled(true);
            enviaMensaje(new Mensaje(usuario, "token:123456", true, false));
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "No se ha podido conectar a la red, revisa tu configuración");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //BOTÓN ENVIAR MENSAJE
        String textoEnviar = jTextField2.getText();
        //CASO DE TODOS
        if(textoEnviar.length() > 0){
            if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
                enviaMensaje(new Mensaje(usuario, textoEnviar, true, false));
            } else{
                HashSet<String> usuariosEnviar = new HashSet<String>();
                for(int i = 1; i < model.getRowCount(); i++){
                    //System.out.println("Enviar a: " + model.getValueAt(i, 1).toString());
                    if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                        usuariosEnviar.add(model.getValueAt(i, 1).toString());
                }
                enviaMensaje(new Mensaje(usuario, textoEnviar, false, false, usuariosEnviar));
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //BOTÓN STICKER 1
        //CASO DE TODOS
        if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
            enviaMensaje(new Mensaje(usuario, "1.png", true, true));
        } else{
            HashSet<String> usuariosEnviar = new HashSet<String>();
            for(int i = 1; i < model.getRowCount(); i++){
                if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                    usuariosEnviar.add(model.getValueAt(i, 1).toString());
            }
            enviaMensaje(new Mensaje(usuario, "1.png", false, true, usuariosEnviar));
        }
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //BOTÓN STICKER 2
        //CASO DE TODOS
        if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
            enviaMensaje(new Mensaje(usuario, "2.gif", true, true));
        } else{
            HashSet<String> usuariosEnviar = new HashSet<String>();
            for(int i = 1; i < model.getRowCount(); i++){
                if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                    usuariosEnviar.add(model.getValueAt(i, 1).toString());
            }
            enviaMensaje(new Mensaje(usuario, "2.gif", false, true, usuariosEnviar));
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        //BOTÓN STICKER 3
        //CASO DE TODOS
        if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
            enviaMensaje(new Mensaje(usuario, "3.gif", true, true));
        } else{
            HashSet<String> usuariosEnviar = new HashSet<String>();
            for(int i = 1; i < model.getRowCount(); i++){
                if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                    usuariosEnviar.add(model.getValueAt(i, 1).toString());
            }
            enviaMensaje(new Mensaje(usuario, "3.gif", false, true, usuariosEnviar));
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //BOTÓN STICKER 5
        //CASO DE TODOS
        if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
            enviaMensaje(new Mensaje(usuario, "5.gif", true, true));
        } else{
            HashSet<String> usuariosEnviar = new HashSet<String>();
            for(int i = 1; i < model.getRowCount(); i++){
                if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                    usuariosEnviar.add(model.getValueAt(i, 1).toString());
            }
            enviaMensaje(new Mensaje(usuario, "5.gif", false, true, usuariosEnviar));
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        //BOTÓN STICKER 6
        //CASO DE TODOS
        if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
            enviaMensaje(new Mensaje(usuario, "6.gif", true, true));
        } else{
            HashSet<String> usuariosEnviar = new HashSet<String>();
            for(int i = 1; i < model.getRowCount(); i++){
                if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                    usuariosEnviar.add(model.getValueAt(i, 1).toString());
            }
            enviaMensaje(new Mensaje(usuario, "6.gif", false, true, usuariosEnviar));
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        //BOTÓN STICKER 2
        //CASO DE TODOS
        if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
            enviaMensaje(new Mensaje(usuario, "7.gif", true, true));
        } else{
            HashSet<String> usuariosEnviar = new HashSet<String>();
            for(int i = 1; i < model.getRowCount(); i++){
                if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                    usuariosEnviar.add(model.getValueAt(i, 1).toString());
            }
            enviaMensaje(new Mensaje(usuario, "7.gif", false, true, usuariosEnviar));
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        //BOTÓN STICKER 8
        //CASO DE TODOS
        if(Boolean.valueOf(model.getValueAt(0, 0).toString())){
            enviaMensaje(new Mensaje(usuario, "8.gif", true, true));
        } else{
            HashSet<String> usuariosEnviar = new HashSet<String>();
            for(int i = 1; i < model.getRowCount(); i++){
                if(Boolean.valueOf(model.getValueAt(i, 0).toString()))
                    usuariosEnviar.add(model.getValueAt(i, 1).toString());
            }
            enviaMensaje(new Mensaje(usuario, "8.gif", false, true, usuariosEnviar));
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    static public void enviaMensaje(Mensaje mensaje){
        try{
            //BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            
            String dir = "230.1.1.1";
            int pto = 1234;
            InetAddress gpo = InetAddress.getByName(dir);
            ByteArrayOutputStream baos= new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(mensaje);
            oos.flush();
            byte[] b = baos.toByteArray();
            DatagramPacket p = new DatagramPacket(b, b.length, gpo, pto);
            m.send(p);
        }catch(Exception e){
            e.printStackTrace();
        }//catch
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Chat().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
