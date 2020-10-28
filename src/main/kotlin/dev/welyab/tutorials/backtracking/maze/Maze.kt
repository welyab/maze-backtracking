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

class Maze(
    val width: Int,
    val height: Int,
    private val initialMap: List<Char>
) {

    private var map: MutableList<Char> = initialMap.toMutableList()

    override fun toString(): String {
        return mapToString(map, width)
    }

    companion object {
        const val WALL = '#'
        const val PATH = ' '

        fun rowColumnToIndex(row: Int, column: Int, width: Int): Int {
            return row * width + column
        }

        fun indexToRow(index: Int, width: Int): Int {
            return index / width
        }

        fun indexToColumn(index: Int, width: Int): Int {
            return index % width
        }

        fun mapToString(map: List<Char>, width: Int): String {
            return buildString {
                map.slice(width).forEach { line ->
                    append(line.joinToString(separator = " ")).append("\n")
                }
            }
        }
    }
}
