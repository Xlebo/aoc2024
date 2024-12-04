package day04

import getOrFetchInputData
import getTestInput

fun main() {
    val testInput = getTestInput(4, "example")
    val test1 = part1(testInput)
    check(test1 == 18) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 9) { "Got $test2" }

    val input = getOrFetchInputData(4)
    println(part1(input))
    println(part2(input))
}

const val WORD = "XMAS"
val DIRECTIONS = (-1..1).flatMap { first -> (-1..1).map { second -> first to second } }.minus(0 to 0)

fun checkWord(letterIndex: Int, direction: Pair<Int, Int>, map: List<String>, position: Pair<Int, Int>): Int {
    if (letterIndex == WORD.length)
        return 1
    val newPos = position.first + direction.first to position.second + direction.second
    if (map.getOrNull(newPos.first)?.getOrNull(newPos.second) == WORD[letterIndex])
        return checkWord(letterIndex.inc(), direction, map, newPos)
    return 0
}

/*
    0 * 2
    * A *
    1 * 3
 */
val X_DIRECTIONS = listOf(-1, 1).flatMap { first -> listOf(-1, 1).map { second -> first to second } }

fun checkA(map: List<String>, position: Pair<Int, Int>): Boolean {
    val values = X_DIRECTIONS
        .map { it.first + position.first to it.second + position.second }
        .map { map.getOrNull(it.first)?.getOrNull(it.second)?: return false }
    return values.groupingBy { it }.eachCount().let {
        it['M'] == 2 && it['S'] == 2 && values[0] != values[3]
    }
}

fun part1(input: List<String>): Int {
    return input.indices.sumOf { row ->
        input[row].indices.sumOf { letter ->
            if (input[row][letter] == 'X') {
                DIRECTIONS.sumOf { d ->
                    checkWord(1, d, input, row to letter)
                }
            } else {
                0
            }
        }
    }
}

fun part2(input: List<String>): Int {
    return input.indices.sumOf { row ->
        input[row].indices.count { letter ->
            if (input[row][letter] == 'A') {
                checkA(input, row to letter)
            } else {
                false
            }
        }
    }
}