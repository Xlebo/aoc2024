package day10

import getOrFetchInputData
import getTestInput

typealias Position = Pair<Int, Int>

operator fun Position.plus(pos: Position): Position {
    return this.first + pos.first to this.second + pos.second
}

val DIRECTIONS = listOf(-1 to 0, 1 to 0, 0 to 1, 0 to -1)

// print statements counter: 1

fun main() {
    val testInput = getTestInput(10, "example")
    val test1 = part1(testInput)
    check(test1 == 36) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 81) { "Got $test2" }

    val input = getOrFetchInputData(10)
    println(part1(input))
    println(part2(input))
}

fun checkTrail(input: List<String>, current: Position): Set<Position> {
    val currentValue = input[current.first][current.second]
    if (currentValue == '9')
        return setOf(current)
    return DIRECTIONS.fold(setOf()) { acc, direction ->
        val newPos = direction + current
        if (input.getOrNull(newPos.first)?.getOrNull(newPos.second) == (currentValue + 1)) {
            acc + checkTrail(input, newPos)
        } else {
            acc
        }
    }
}

fun checkTrailRating(input: List<String>, current: Position): Int {
    val currentValue = input[current.first][current.second]
    if (currentValue == '9')
        return 1
    return DIRECTIONS.sumOf { direction ->
        val newPos = direction + current
        if (input.getOrNull(newPos.first)?.getOrNull(newPos.second) == (currentValue + 1)) {
            checkTrailRating(input, newPos)
        } else {
            0
        }
    }
}

fun part1(input: List<String>): Int {
    return input.indices.sumOf { row ->
        input[row].indices.sumOf { column ->
            if (input[row][column] == '0') {
                checkTrail(input, row to column).size
            } else {
                0
            }
        }
    }
}

fun part2(input: List<String>): Int {
    return input.indices.sumOf { row ->
        input[row].indices.sumOf { column ->
            if (input[row][column] == '0') {
                checkTrailRating(input, row to column)
            } else {
                0
            }
        }
    }
}
