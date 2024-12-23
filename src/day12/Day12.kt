package day12

import getOrFetchInputData
import getTestInput

typealias Fence = Pair<Pair<Int, Int>, Pair<Int, Int>>

val DIRECTIONS = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)

// print statements counter: 3

fun main() {
    val testInput = getTestInput(12, "example1")
    val test1 = part1(testInput)
    check(test1 == 140) { "Got $test1" }

    val testInput2 = getTestInput(12, "example2.txt")
    val test2 = part1(testInput2)
    check(test2 == 772) { "Got $test2" }

    val testInput3 = getTestInput(12, "example3")
    val test3 = part1(testInput3)
    check(test3 == 1930) { "Got $test3" }

    val test4 = part2(testInput)
    check(test4 == 80) { "Got $test4" }

    val test5 = part2(testInput2)
    check(test5 == 436) { "Got $test5" }

    val testInput4 = getTestInput(12, "example4")
    val test6 = part2(testInput4)
    check(test6 == 236) { "Got: $test6" }

    val testInput5 = getTestInput(12, "example5")
    val test7 = part2(testInput5)
    check(test7 == 368) { "Got: $test7" }

    val test8 = part2(testInput3)
    check(test8 == 1206) { "Got: $test8" }

    val input = getOrFetchInputData(12)
    println(part1(input))
    println(part2(input))
}

fun range(x: Int, y: Int): IntRange {
    return minOf(x, y)..maxOf(x, y)
}

fun part1(input: List<String>): Int {
    val values = mutableListOf<Pair<Int, Int>>()
    val explored = mutableSetOf<Pair<Int, Int>>()
    val toExplore = input.flatMapIndexed { index, row -> row.indices.map { column -> index to column } }.toMutableList()

    while (toExplore.isNotEmpty()) {
        val currentField = toExplore.removeFirst()
        if (explored.contains(currentField)) {
            continue
        }

        var count = 0
        var perimeter = 0
        val currentValue = input[currentField.first][currentField.second]
        val currentFieldTilesToExplore = mutableListOf(currentField)

        while (currentFieldTilesToExplore.isNotEmpty()) {
            val currentTile = currentFieldTilesToExplore.removeFirst()
            if (explored.contains(currentTile)) {
                continue
            }

            explored.add(currentTile)
            ++count
            DIRECTIONS.forEach { d ->
                val new = d.first + currentTile.first to d.second + currentTile.second
                if (input.getOrNull(new.first)?.getOrNull(new.second) == currentValue) {
                    currentFieldTilesToExplore.add(new)
                } else {
                    ++perimeter
                }
            }
        }

        values.add(count to perimeter)
    }

    return values.sumOf { it.first * it.second }
}

fun Fence.contains(point: Pair<Int, Int>): Boolean {
    return point.first in range(this.first.first, this.second.first) &&
            point.second in range(this.first.second, this.second.second)
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + other.first to this.second + other.second
}

fun Pair<Int, Int>.getPerpendicularFieldDirections(): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    return if (this.first != 0) {
        (0 to 2) to (0 to -2)
    } else {
        (2 to 0) to (-2 to 0)
    }
}

fun inputWithFences(input: List<String>): List<String> {
    return (0..input.size * 2).map { row ->
        (0..input[0].length * 2).map { column ->
            if (row % 2 == 0) {
                if (column % 2 == 1 &&
                    input.getOrNull(row / 2)?.getOrNull(column / 2) !=
                    input.getOrNull(row / 2 - 1)?.getOrNull(column / 2)
                ) {
                    "-"
                } else {
                    " "
                }
            } else {
                if (column % 2 == 1) {
                    input[row / 2][column / 2].toString()
                } else {
                    if (input.getOrNull(row / 2)?.getOrNull(column / 2 - 1) !=
                        input.getOrNull(row / 2)?.getOrNull(column / 2)
                    ) {
                        "|"
                    } else {
                        " "
                    }
                }
            }
        }.joinToString("") { it }
    }
}

fun getFenceEdge(
    map: List<String>,
    currentField: Pair<Int, Int>,
    fieldDirection: Pair<Int, Int>,
    fenceDirection: Pair<Int, Int>
): Pair<Int, Int> {
    val newField = currentField + fieldDirection
    val newFence = newField + fenceDirection
    return if (map.getOrNull(newFence.first)?.getOrNull(newFence.second) in listOf('-', '|') &&
        map.getOrNull(newField.first)?.getOrNull(newField.second) == map[currentField.first][currentField.second]
    ) {
        getFenceEdge(map, newField, fieldDirection, fenceDirection)
    } else {
        currentField + fenceDirection
    }
}

fun part2(input: List<String>): Int {
    val map = inputWithFences(input)

    val values = mutableListOf<Pair<Int, Int>>()
    val explored = mutableSetOf<Pair<Int, Int>>()
    val toExplore = input
        .flatMapIndexed { index, row ->
            row.indices.map { column ->
                2 * index + 1 to 2 * column + 1
            }
        }
        .toMutableList()

    while (toExplore.isNotEmpty()) {
        val currentField = toExplore.removeFirst()
        if (explored.contains(currentField)) {
            continue
        }
        var count = 0
        val fences = mutableListOf<Fence>()

        val currentValue = map[currentField.first][currentField.second]
        val currentFieldTilesToExplore = mutableListOf(currentField)

        while (currentFieldTilesToExplore.isNotEmpty()) {
            val currentTile = currentFieldTilesToExplore.removeFirst()
            if (explored.contains(currentTile)) {
                continue
            }

            explored.add(currentTile)
            ++count

            DIRECTIONS.forEach { d ->
                val new = d.first * 2 + currentTile.first to d.second * 2 + currentTile.second
                val newFence = d.first + currentTile.first to d.second + currentTile.second
                if (map.getOrNull(new.first)?.getOrNull(new.second) == currentValue) {
                    currentFieldTilesToExplore.add(new)
                } else {
                    if (fences.none { it.contains(newFence) }) {
                        val (fieldD1, fieldD2) = d.getPerpendicularFieldDirections()
                        fences.add(
                            getFenceEdge(map, currentTile, fieldD1, d) to getFenceEdge(
                                map,
                                currentTile,
                                fieldD2,
                                d
                            )
                        )
                    }
                }
            }
        }
        values.add(count to fences.size)
    }

    return values.sumOf { it.first * it.second }
}
