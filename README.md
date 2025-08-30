# Kniffel Game Application

The **Kniffel Game Application** is a Java-based application. It serves as the entry point for the Kniffel game, a dice game similar to Yahtzee. This project organizes and manages the functionality required to implement and play the Kniffel game.

---
 Link to Releases:
 https://github.com/Senti81/kniffel-app/releases/tag/test
---

## Table of Contents

1. [Project Overview](#project-overview)
2. [How to Run](#how-to-run)
3. [Features](#features)
4. [Technologies Used](#technologies-used)
5. [Project Structure](#project-structure)

---

## Project Overview

This application is written in Java and uses the `KniffelApplication` class (referenced in `App.java`) as the main entry point. The main file initializes the application, laying the groundwork for the game's functionality.

The project's main goal is to enable users to play the Kniffel game within an interactive setting, extending the classic game into a robust Java application.

---

## How to Run

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or above is recommended.
- **Build Tool**: Maven or Gradle (as per the project configuration).
- **IDE**: Any Java-compatible IDE, such as IntelliJ IDEA.

### Steps to Run
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd kniffel-game-app
   ```
3. Build the project:
    - If using Maven:
      ```bash
      mvn clean install
      ```
    - If using Gradle:
      ```bash
      gradle build
      ```
4. Run the application:
   ```bash
   java -cp target/kniffel-game-app.jar de.coin.kniffel.App
   ```

Alternatively, open the project in your favorite IDE and run the `main` method from the `App` class.

---

## Features

- Implements the classic rules of the Kniffel game.
- Entry point (`App.java`) cleanly manages application starting logic.
- Modular structure for scalability and easy maintainability.
- Extendable framework for adding new features like multiplayer mode, scores, and history.

---

## Technologies Used

- **Programming Language**: Java
- **Build Tool**: Maven/Gradle (depending on the project setup)
- **IDE Support**: IntelliJ IDEA for streamlined development and debugging.

---

## Project Structure

The project is organized as follows:

```plaintext
src
├── main
│   ├── java
│   │   └── de.coin.kniffel
│   │       ├── App.java             # Main entry point of the application
│   │       ├── KniffelApplication   # Core application logic (referenced in App.java)
│   │       └── ...                  # Additional classes and game logic
│   └── resources                     # Application resources (if any)
├── test                              # Unit tests for the application
└── pom.xml / build.gradle            # Build configuration for Maven or Gradle
```
