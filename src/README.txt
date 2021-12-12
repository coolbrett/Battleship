Author: Brett Dale, Katherine Blanton
Version: 12/11/2021
Program: BattleShip, Program 4
Description: This program is meant to allow multiple players to play a game of Battleship. This is done through a tcp connection
             where each player connects to a shared server. The game commands are then communicated through ConnectionAgents that 
             exist at the server end and client end that are connected to each other via threads.
             
---------------------------------------------------------------------------------------------------------------------------------
Compile: javac ./client/*.java ./common/*.java ./server/*.java

Usage:   java ./server/BattleShipDriver portNumber [GridSize]
         java ./client/BattleDriver localhost portNumber username
---------------------------------------------------------------------------------------------------------------------------------

Files Needed:  client/BattleClient.java
               client/BattleDriver.java
               client/PrintStreamMessageListener.java
               
               common/ConnectionAgent.java
               common/MessageListener.java
               common/MessageSource.java
               
               server/BattleServer.java
               server/BattleShipDriver.java
               server/Game.java
               server/Grid.java
               server/Symbol.java


Instructions: In order to run this program, first you must have all of the files needed located in their appropriate
              pacakge/folder. There are three folders: client, common, and server, that each have files in each. Please look at 
              the files needed to make sure each file is in their appropriate package/folder. All of these folders must be 
              stored in the same folder as well. Once this is done, you must be outside of the three folders, but in the folder
              containing the 3 folders and you will type the compile instruction located above. Once this compiles, you MUST run
              the server usage first. It has an optional grid size argument that ranges from 5-10. The clients have to connect 
              to the server. Once the server has started you may connect as many clients/players as you would like. To do this 
              you type the client usage. You may NOT have the same usernames for your players. After all of the players have 
              connected, any player can type /start to begin the game. Type /display yourUserName to view your board and type
              /display opponentUsername to see the hits and misses on their board. Type /fire row column opponentUsername to 
              fire at the opponent. Type /surrender to leave the game. Happy ship sinking!
              

---------------------------------------------------------------------------------------------------------------------------------
Bugs/Errors Known: A client surrendering does kill the connection to the server and the server acknowledges that the client 
                   will not continue sending messages. However, the client hangs even after the connecton with the server has
                   been terminated. 
