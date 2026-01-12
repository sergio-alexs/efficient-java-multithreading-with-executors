# Efficient Java Multithreading with Executors

This project provides a comprehensive set of tutorials and exercises for learning efficient multithreading techniques in Java, with a special focus on the Executor Framework.

## How to Build

This is a standard Maven project. To build it, run the following command from the project's root directory:

```shell
mvn clean install
```

## How to Run

The project contains numerous `main` methods, each demonstrating a specific concept. You can run them directly from your IDE or use the `exec-maven-plugin`.

To run a specific class, use the following command, replacing `<main-class>` with the fully qualified name of the class you want to run (e.g., `tut1.executors.running.UsingCachedThreadPool`):

```shell
mvn exec:java -Dexec.mainClass="<main-class>"
```

## Project Structure

The project is organized into tutorials, exercises, and common utility classes.

### Tutorials

Each tutorial directory (`tut1` through `tut9`) covers a specific topic and contains two sub-packages:

*   `threads`: Demonstrates the concept using the traditional `java.lang.Thread` approach.
*   `executors`: Demonstrates the same concept using the modern Executor Framework.

Here is a breakdown of the topics covered:

| Tutorial | Topic                                       |
|----------|---------------------------------------------|
| `tut1`   | **Running Tasks**: Basic thread creation and task execution. |
| `tut2`   | **Naming Threads**: Assigning custom names for easier debugging. |
| `tut3`   | **Returning Values**: How to get results back from asynchronous tasks. |
| `tut4`   | **Daemon Threads**: Using daemon threads that don't prevent the application from exiting. |
| `tut5`   | **Alive Check**: Checking the status of threads and tasks. |
| `tut6`   | **Terminating Tasks**: Techniques for gracefully stopping threads and tasks. |
| `tut7`   | **Exception Handling**: Managing exceptions in multithreaded code. |
| `tut8`   | **Joining Tasks**: Waiting for tasks to complete. |
| `tut9`   | **Scheduling Tasks**: Executing tasks at a specific time or with a fixed delay/rate. |

### Exercises

The `tutexercises` package contains a series of exercises (`Exercise1.java`, `Exercise2.java`, etc.) designed to help you practice the concepts covered in the tutorials.

### Common Utilities

*   `tuts/common`: Contains reusable classes like `Task`, `NamedThreadsFactory`, and `ValueReturningTask` that are used across different tutorials.
*   `tuts/utils`: Contains utility classes, such as `TimeUtils` for handling time units.
