# CS 465 - Computer Networks
## Assignment 3: Battleship (Multiuser game)

##### Professor: Dr. Scott Barlowe
##### Authors: David Jennings & Aidan Kirk
##### Version: November 15, 2021

###### This program simulates a game of Battleship that can be played accross a network. The game will eventually support up to 8 players concurently in the same game. The purpose of this project is to learn to implement concurrent network applications, Java threading, and enabling a TCP client to interact with users as well as with a TCP socket.

# Instalation

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
 - /battle    : sent by clients immediately after connecting the the server with <username> where username is the user’s nick name.
 - /start     : begin a game of Battleship. Play cannot begin if 2 or more users are not present.
 - /fire      :
 - /surrender :
 - /display   :


## Known Issues: Troubleshooting

If the game will not compile, try going into each package and compiling all java files.

## Changelog
[Timeline for this project](/TIMELINE.md)

No current issues.

#### Bugs and Fixes

No known bugs at this time.
