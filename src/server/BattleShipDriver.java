/**
 * BattleClient
 * @author Aidan Kirk, David Jennings
 * @version 11/12/21
 */


package server;

import java.net.URI;

public class BattleShipDriver {

    public static void main(String[] args) {
        try {

            URI uri = new URI("https://www.youtube.com/watch?v=x74bZjDYUTE");

            java.awt.Desktop.getDesktop().browse(uri);
            System.out.println("Web page opened in browser");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}