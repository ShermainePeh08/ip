# 🐸 Shonks

Shonks is a desktop task management application built using **Java 17** and **JavaFX**.  
It provides a chat style interface for managing tasks efficiently, with support for visual statistics and task archiving.

Shonks may be grumpy — but it keeps your tasks organised.

Hello from
```
  _____ _                 _
 / ____| |               | |
| (___ | |__   ___  _ __ | | _____
 \___ \| '_ \ / _ \| '_ \| |/ / __|
 ____) | | | | (_) | | | |   <\__ \
|_____/|_| |_|\___/|_| |_|_|\_\___/

```
---

## ✨ Features
- Add **Todo**, **Deadline**, and **Event** tasks
- List all tasks
- Mark and unmark tasks
- Delete tasks
- Find tasks by keyword
- View task statistics using an in chat **pie chart**
- Archive tasks
- Restore archived tasks
- Exit with delayed close

---

## 🖥 Running the Application

### Using Gradle Wrapper

Open a terminal in the project root directory.

On Windows:
```cmd
.\gradlew run
```
On macOS/Linux:
```cmd
./gradlew run
```
---
## Running Tests

Shonks comes with automated tests to ensure core functionality works as expected.

To run all tests from the project root directory, execute:

```
gradlew test
```

This command will:

- Compile the test classes
- Run all unit tests under `src/test`
- Execute text-based integration tests (if present)
- Display test results in the terminal

After the tests finish running, a detailed report can be found at:

```
build/reports/tests/test/index.html
```

Open this file in your browser to view the full test summary and detailed results.


## Architecture Overview

Shonks follows a modular architecture with clear separation of concerns:

- `command` — Defines command types and handlers  
- `parser` — Parses user input into command objects  
- `task` — Task models (Todo, Deadline, Event)  
- `storage` — Handles file persistence and archive management  
- `ui` — JavaFX user interface components  
- `Shonks` — Core application logic  
- `Main` and `Launcher` — Application entry points  

This separation improves maintainability and extensibility.

---

## Technical Stack

- Java 17  
- JavaFX  
- Gradle  
- JUnit 5  

---

## Project Structure

```
project-root/
├── README.md
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── data/
│   │   │   └── shonks/
│   │   │       ├── .github/
│   │   │       ├── command/
│   │   │       ├── parser/
│   │   │       ├── storage/
│   │   │       ├── task/
│   │   │       ├── ui/
│   │   │       ├── DialogBox.java
│   │   │       ├── InputBar.java
│   │   │       ├── Launcher.java
│   │   │       ├── Main.java
│   │   │       ├── MainWindowImages.java
│   │   │       ├── Shonks.java
│   │   │       └── ShonksException.java
│   │   └── resources/
│   ├── test/
│   └── text-ui-test/
├── AI.md
```

## Notes

- Ensure the project is built using JDK 17.  
- Do not modify the `src/main/java` directory structure.  
- Archive data is stored in the user's home directory under a `.shonks` folder.

