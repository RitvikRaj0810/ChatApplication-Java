import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.*;
public class Server extends JFrame{

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

     private JLabel heading=new JLabel("Server Area"); 
     private JTextArea messageArea=new JTextArea();
     private JTextField messageInput=new JTextField();
     private Font font=new Font("Roboto",Font.PLAIN,20);

    Server(){
        try{
        server=new ServerSocket(7777);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting...");
        socket=server.accept();
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());

          createGUI();
          handleEvents();

         startReading();
         //startWriting();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode()==10){
                   // System.out.println("you have pressed enter button");
                   String contentToSend1= messageInput.getText();
                   messageArea.append("Me: "+contentToSend1+"\n");
                   out.println(contentToSend1);
                   out.flush();
                   messageInput.setText("");
                   messageInput.requestFocus();
                }
            }
            
        });
    }

    private void createGUI(){
        this.setTitle("Server Messager[End]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        //heading.setIcon(new ImageIcon("clogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalAlignment(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);
    }

    public void startReading(){
        Runnable r1=()->{
            System.out.println("reader started..");
            try{
            while(true){
                    String msg=br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("client has terminated the chat...");

                        socket.close();
                        break;
                    }
                System.out.println("Client: "+msg);
            }
        }catch(Exception e){
            //e.printStackTrace();
            System.out.println("connection is closed");
        }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2=()->{
            System.out.println("writer started..");
            try{
            while( !socket.isClosed()){
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    

                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
            }
        }catch(Exception e){
                   // e.printStackTrace();
                   System.out.println("connection is closed");
                } 
        };

        new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.println("this is Server..");
        new Server();
    }
}