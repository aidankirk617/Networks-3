# CS 465 - Computer Networks
## Assignment 3: Battleship (Multiuser game)

##### Professor: Dr. Scott Barlowe
##### Authors: David Jennings & Aidan Kirk
##### Version: December 11th, 2021

###### This program simulates a game of Battleship that can be played accross a network. The game will eventually support up to 8 players concurently in the same game. The purpose of this project is to learn to implement concurrent network applications, Java threading, and enabling a TCP client to interact with users as well as with a TCP socket.

# Installation

Download the source folder. Compile the program using javac from outside of the package directories.
```bash
 find . -name "*.java" > sources.txt
 javac @sources.txt
 ```

# Usage

Run the Server using java. Then run the client using java.

###### Server Side:
```bash
 java server.BattleShipDriver [port]
 ```

###### Client Side:
```bash
 client.BattleDriver [server address] [port] [username]
 ```
 Commands:
 - battle    : sent by clients immediately after connecting the the server with \<username> where username is the user’s nick name.
   - USE: /battle \<username>
 
 - start     : begin a game of Battleship. Play cannot begin if 2 or more users are not present.
   - USE: /start
   
 - fire      : sent during game play to indicate the user to attack, and the location within the board you wish to attack. 
   - USE: /fire \<[0-9]+> \<[0-9]+> \<username>
   
 - surrender : sent when you intend to disconnect from the server.
   - USE: /surrender
   
 - display   : sent when you want to examine the current state of a board.
   - USE: /display \<username>


## Known Issues: Troubleshooting

If the game will not compile, try going into each package and compiling all java files.

12/11/2021 If a player does /battle username twice before the game starts an extra player will be added to the list, causing bugs throughout

12/11/2021 If a player leaves the game before it is finished, the end of the game will not be recognized as their grid will still exist

12/11/2021 players are able to fire at any time (turns are not tracked)

12/11/2021 when the game ends, players are still able to fire at grids

12/11/2021 if a player tries to enter the server in the middle of a game, many bugs will occur

12/11/2021 the server must be restarted to start a new game
