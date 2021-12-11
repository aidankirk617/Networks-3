# CS 465 - Computer Networks
## Assignment 3: Battleship (Multiuser game)

##### Professor: Dr. Scott Barlowe
##### Authors: David Jennings & Aidan Kirk
##### Version: November 15, 2021

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

## Changelog
[Timeline for this project](/TIMELINE.md)

No current issues.

#### For David

Here's the rundown. Was able to get a game going that communicates between client and server, rather than earlier where it could only run on server. Seems to work fine? I was able to finish a game without it breaking. That being said, it has some prompts and i need to rewrite some more to let it use all the commands he described. I think the next logical step is for us to check out the connection agent and message stuff and get it working before we move on to threading. After we get threading going, we can finally do the whole "multiple" client bit he wanted, then we're all gucci. Text me if this didn't make any sense

Oh yeah, also run BattleShipDriver.java first, and then BattleDriver.java. Once you have BattleDriver.java running, type '/start' to begin.

CLA's for BattleShipDriver are 5000 5, and localhost 5000 player1 for BattleDriver. Should be able to type in your edit configurations in intellij.
