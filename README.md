# Simple Text Adventure Game
A Simple Text Advanture Game, game engin that is acessed through a game server. A good example of these sort 
of games is  <a href="https://textadventures.co.uk/games/play/5zyoqrsugeopel3ffhz_vq">  Zork </a>.The game 
engin logic was built from scratch using Java, Maven, and JUnit with proper testing. While the game
entities and actions are read in from the file system. 

## Features
The basic commands for this game are:
<ul>
    <li> <b>Inventory -</b> (or inv for short) lists all of the artefacts currently being carried by the player</li>
    <li> <b>Get -</b> picks up a specified artefact from the current location and adds it into player’s inventory</li>
    <li> <b>Drop -</b> puts down an artefact from player’s inventory and places it into the current location</li>
    <li> <b>Goto -</b> moves the player from the current location to the specified location (if there is a path to that location)</li>
    <li> <b>Look -</b> prints names and descriptions of entities in the current location and lists paths to other locations</li>
</ul>

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java Development Kit (JDK) 19
- Apache Maven 3.9.0

### Installing

Follow these steps to get the development environment running:

1. Clone this repository using `git clone https://github.com/JamesLove1/Simple-Text-Adventure-Game.git`.
2. Navigate to the project directory using `cd Simple-Text-Adventure-Game`.
3. Run `./mvnw clean install` to build the project and install the necessary dependencies.

### Using the database

```bash
# Run the server
$ ./mvnw exec:java@server

# Run the client to start querying
$ ./mvnw exec:java@client -Dexec.args="James"
```

### Compiling

Run `./mvnw compile` to compile the project.


### Running the tests

Run `./mvnw test` for testing.


## Acknowledgments

* This is an assignment given by the instructor at the University of Bristol: [Simon](https://github.com/drslock) from the course of Object-Oriented Programming with Java 2022.
* This assignment is built on top on the base Maven project.

