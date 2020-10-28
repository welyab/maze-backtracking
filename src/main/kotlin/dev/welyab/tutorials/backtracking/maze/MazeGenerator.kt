/*
 * Copyright (C) 2020 Welyab da Silva Paula
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package dev.welyab.tutorials.backtracking.maze

import dev.welyab.tutorials.backtracking.maze.Maze.Companion.indexToColumn
import dev.welyab.tutorials.backtracking.maze.Maze.Companion.indexToRow
import dev.welyab.tutorials.backtracking.maze.Maze.Companion.rowColumnToIndex
import java.util.Stack
import kotlin.collections.ArrayList
import kotlin.random.Random

class MazeGenerator(
    private val width: Int,
    private val height: Int
) {

    init {
        if (width < 2 || height < 2) throw IllegalArgumentException(
            "Invalid [width, height] = [$width, $height]. Inform values >= 2."
        )
    }

    private val rows: Int get() = height * 2 - 1
    private val columns: Int get() = width * 2 - 1
    private val mapSize: Int get() = rows * columns

    private val map = ArrayList<Char>()

    fun generateMaze(): Maze {
        initializeMap()
        generatePaths()
        val mazeMap = map
            .asSequence()
            .map {
                if (it == GENERATED_PATH) Maze.PATH
                else Maze.WALL
            }
            .toList()
            .slice(columns)
            .map { it.toMutableList() }
            .toMutableList()
        val horizontalWall = createList(Maze.WALL, columns)
        mazeMap.add(0, horizontalWall)
        mazeMap.forEach {
            it.add(0, Maze.WALL)
            it.add(Maze.WALL)
        }
        mazeMap.add(horizontalWall)
        return Maze(
            columns + 2,
            rows + 2,
            mazeMap.asSequence().flatMap { it.asSequence() }.toList()
        )
    }

    private fun createList(content: Char, size: Int): MutableList<Char> {
        return ArrayList<Char>().apply {
            (0 until size).forEach { _ -> this += content }
        }
    }

    private fun initializeMap() {
        map.clear()
        (0 until mapSize).forEach { _ -> map += EMPTY }
        for (row in 1 until rows step 2) {
            for (column in 1 until columns step 2) {
                map[rowColumnToIndex(row, column, columns)] = BLOCK
            }
        }
    }

    private fun generatePaths() {
        map[0] = GENERATED_PATH
        while (true) {
            val buildingPathStartIndex = findNextBuildingPathStartIndex()
            if (buildingPathStartIndex < 0) break
            generatePath(buildingPathStartIndex)
        }
    }

    private fun generatePath(startIndex: Int) {
        val path = Stack<Int>()
        path.push(startIndex)
        map[startIndex] = BUILDING_PATH
        while (true) {
            val nextIndex = path.peek().peekNextIndex()
            val collision = nextIndex.getCollisionIndex(path.peek())
            when {
                collision < 0 -> {
                    path.push(nextIndex)
                    map[nextIndex] = BUILDING_PATH
                }
                map[collision] == BUILDING_PATH -> {
                    while (path.size >= 2) {
                        val removedIndex = path.pop()
                        map[removedIndex] = EMPTY
                    }
                }
                else -> {
                    while (path.isNotEmpty()) {
                        val removedIndex = path.pop()
                        map[removedIndex] = GENERATED_PATH
                    }
                    map[nextIndex] = GENERATED_PATH
                    return
                }
            }
        }
    }

    private fun Int.getCollisionIndex(excludedIndex: Int): Int {
        val candidateCollisions = getAdjacent(this)
            .asSequence()
            .filter { it != excludedIndex }
            .toList()
        for (candidateIndex in candidateCollisions) {
            if (map[candidateIndex] == BUILDING_PATH) return candidateIndex
            if (map[candidateIndex] == GENERATED_PATH) return candidateIndex
        }
        return -1
    }

    private fun Int.peekNextIndex(): Int {
        val candidates = getAdjacent(this).asSequence().filter { map[it] == EMPTY }.toList()
        return candidates[nextRdnInt(candidates.size)]
    }

    private fun findNextBuildingPathStartIndex(): Int {
        return map.indices.find {
            map[it] == EMPTY
                    && getAdjacent(it)
                .all { adj -> map[adj] == EMPTY }
        } ?: -1
    }

    private fun getAdjacent(index: Int): List<Int> {
        val adjacent = ArrayList<Int>(4)
        if (index.hasUp() && map[index.up()] != BLOCK) adjacent += index.up()
        if (index.hasDown() && map[index.down()] != BLOCK) adjacent += index.down()
        if (index.hasLeft() && map[index.left()] != BLOCK) adjacent += index.left()
        if (index.hasRight() && map[index.right()] != BLOCK) adjacent += index.right()
        return adjacent
    }

    private fun Int.up(): Int = rowColumnToIndex(
        indexToRow(this, columns) - 1,
        indexToColumn(this, columns),
        columns
    )

    private fun Int.down(): Int = rowColumnToIndex(
        indexToRow(this, columns) + 1,
        indexToColumn(this, columns),
        columns
    )

    private fun Int.left(): Int = rowColumnToIndex(
        indexToRow(this, columns),
        indexToColumn(this, columns) - 1,
        columns
    )

    private fun Int.right(): Int = rowColumnToIndex(
        indexToRow(this, columns),
        indexToColumn(this, columns) + 1,
        columns
    )

    private fun Int.hasUp(): Boolean = indexToRow(this, columns) - 1 >= 0
    private fun Int.hasDown(): Boolean = indexToRow(this, columns) + 1 < rows
    private fun Int.hasLeft(): Boolean = indexToColumn(this, columns) - 1 >= 0
    private fun Int.hasRight(): Boolean = indexToColumn(this, columns) + 1 < columns

    companion object {
        private const val EMPTY = '+'
        private const val BLOCK = '0'
        private const val GENERATED_PATH = '#'
        private const val BUILDING_PATH = '*'

        private val rdn = Random(System.currentTimeMillis())

        private fun nextRdnInt(bound: Int): Int = rdn.nextInt(bound)
    }
}

fun main() {
    val generator = MazeGenerator(10, 10)
    val maze = generator.generateMaze()
    println(maze)
}