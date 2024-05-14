# Simple Text Adventure Game

## Features

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