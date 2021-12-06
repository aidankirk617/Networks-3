/*
 * Western Carolina University
 * Fall 2021
 * CS-465-01 - Computer Networks
 * Program 3: Battleship (Multiuser Game)
 * Instructor: Dr. Scott Barlowe
 */

package server;

import common.MessageListener;
import common.MessageSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BattleServer implements MessageListener {

    private ServerSocket serverSocket;
    private int current;
    private Game game;

        public static void main (String[]args){
            try {
                //Create the Sockets, Reader and Writer
                ServerSocket servSock = new ServerSocket(5500);
                while (!servSock.isClosed()) {
                    Socket sock = servSock.accept();
                    InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
                    BufferedReader buffReader = new BufferedReader(streamReader);
                    PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
                    //Read in the age and add 42 to it
                    String hold = (buffReader.readLine());

                    //Send the new num
                    writer.println(hold);
                    //Close everything
                    sock.close();
                    streamReader.close();
                    buffReader.close();
                    writer.close();
                }
                servSock.close();
            } catch (IOException ioe) {
                System.out.println("Error!");
                System.exit(1);
            }
        }

        public void listen() {

        }

        public void broadcast (String message){

        }

        /**
         * Used to notify observers that the subject has received a message.
         *
         * @param message The message received by the subject
         * @param source  The source from which this message originated (if needed).
         */
        @Override
        public void messageReceived (String message, MessageSource source){

        }

        /**
         * Used to notify observers that the subject will not receive new messages; observers can
         * deregister themselves.
         *
         * @param source The MessageSource that does not expect more messages.
         */
        @Override
        public void sourceClosed (MessageSource source){

        }
    }

