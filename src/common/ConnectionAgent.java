/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package common;

import client.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionAgent extends MessageSource implements Runnable {

    Socket socket;
    Scanner in;
    PrintStream out;
    ObjectOutputStream oos;
    ObjectInputStream ois;
    Thread thread;
    Player player;

    public ConnectionAgent(Socket sock){
        try {
            socket = sock;
            in = new Scanner(socket.getInputStream());
            out = new PrintStream(socket.getOutputStream());
        }catch (IOException ioe){
            System.out.println("ERROR IN CONNECTION AGENT");
        }
        thread = new Thread(this);
    }

    public ConnectionAgent(Socket sock, ObjectOutputStream oos, ObjectInputStream ois, Scanner scan, Player player) {
        this.socket = sock;
        this.oos = oos;
        this.ois = ois;
        this.in = scan;
        this.player = player;
    }

    public void sendMessage(String message) throws IOException {
        //out.println(message);
        oos.writeObject(message);
    }

    public void sendObject(Object o) throws IOException {
        oos.writeObject(o);
    }

    public boolean isConnected(){
        return socket.isConnected();
    }

    public void close() throws IOException {
        socket.close();
    }

    public Player getPlayer() {
        return player;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    @Override
    public void run() {
        /*while(true) {
            try {
                String received = in.next();
                if (received.equals("/surrender")) {
                    System.out.println("Client: " + socket + " is disconnecting...");
                    socket.close();
                    System.out.println("Client disconnected");

                    break;
                }

                System.out.println("Message received: " + received);

            }catch(IOException ioe){
                System.out.println("ERROR IN CONNECTION AGENT: 56");
            }
        }

         */
        try {
            System.out.println("!!! " + player.getName() + " has entered battle");
            //Take input
            if (in.hasNextLine()) {
                String input = in.nextLine();
                sendMessage(input);
            }

            boolean end = true;
            while (end) {
                String str = (String) ois.readObject();
                System.out.println(str);
                /*
                if (str != null && !str.contains(player.getName()) && !str.contains("+---+")
                        && !str.contains("exist") && !str.contains("battle") && !str.contains("turn")) {
                    System.out.println(str);
                } else if (str != null) {
                    System.out.println(str);
                    String input = in.nextLine();
                    sendMessage(input);
                } else {
                    end = false;
                }
                */
                if (str != null && (str.contains(player.getName()) || str.contains("battle") || str.contains("exist") && str.contains("turn"))) {
                    String input = in.nextLine();
                    sendMessage(input);
                } else if (str == null){
                    end = false;
                }
            }
            close();
        } catch (IOException | ClassNotFoundException ioe) {
            System.out.println("Error: IO Exception " + ioe.getMessage());
            System.exit(2);
        }
    }
}