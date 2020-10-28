# Maze Backtracking

A maze generator and solver using backtracking algorithms.

This project is part of a simple tutorial about using of backtracking algorithms.

## Setup

The fast way to get hands on code is open the project in some IDE with support for Kotlin under a Gradle project:

* IntelliJ (recommended)
* Eclipse (with specific kotlin plugins)

## Generating a Maze

The maze paths fits inside a matrix with `width` and `height` dimensions. The `MazeGenerator` class generates mazes by using [Wilson's algorithm](https://en.wikipedia.org/wiki/Maze_generation_algorithm#Wilson's_algorithm), that try to randomly create paths. 

Related to `width` and `height` properties of generator, the final maze will have a matrix with `(width * 2) + 1` columns and `(height * 2)` + 1 rows, subject to initial `width` and `height` `>= 2`.

```kotlin
fun main() {
    val generator = MazeGenerator(10, 10)
    val maze = generator.generateMaze()
    println(maze)
}
```

Outputs

```text
# # # # # # # # # # # # # # # # # # # # #
#                       #       #       #
#   # # #   # # # # #   #   #   # # #   #
#   #           #   #       #           #
# # # # #   # # #   #   #   # # # # # # #
#                   #   #           #   #
# # #   # # # # # # #   # # #   # # #   #
#       #               #               #
# # # # #   # # #   #   #   # # # # #   #
#           #       #   #       #       #
# # #   # # # # # # # # # # # # #   # # #
#                   #           #       #
#   # # # # # # #   # # # # #   # # # # #
#   #               #           #       #
# # #   # # #   #   #   # # #   #   # # #
#       #       #           #           #
# # #   # # #   # # #   # # # # #   #   #
#           #   #   #   #   #   #   #   #
#   # # #   #   #   #   #   #   #   # # #
#   #       #   #       #               #
# # # # # # # # # # # # # # # # # # # # #
```
