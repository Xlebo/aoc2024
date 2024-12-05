package day05

import getOrFetchInputData
import getTestInput

fun main() {
    val testInput = getTestInput(5, "example")
    val test1 = part1(testInput)
    check(test1 == 143) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 123) { "Got $test2" }

    val input = getOrFetchInputData(5)
    println(part1(input))
    println(part2(input))
}

fun splitInputToReversedRulesAndUpdates(input: List<String>): Pair<HashSet<Pair<Int, Int>>, List<List<Int>>> {
    val indexOfEmpty = input.indexOf("")
    val reversedRules = input.subList(0, indexOfEmpty)
        .map {
            val (first, second) = it.split("|")
            second.toInt() to first.toInt()
        }
        .toHashSet()
    val updates = input.subList(indexOfEmpty + 1, input.size)
        .map { row -> row.split(",").map { it.toInt() } }

    return reversedRules to updates
}

fun part1(input: List<String>): Int {
    val (reversedRules, updates) = splitInputToReversedRulesAndUpdates(input)
    val updatesTuples = updates.map { row ->
        row.indices.flatMap { first ->
            (first + 1..<row.size).map { second ->
                row[first] to row[second]
            }
        } to row[row.size / 2]
    }
    return updatesTuples.sumOf { row ->
        if (row.first.all { !reversedRules.contains(it) }) {
            row.second
        } else {
            0
        }
    }
}

fun part2(input: List<String>): Int {
    val (reversedRules, updates) = splitInputToReversedRulesAndUpdates(input)
    return updates.map { it.toMutableList() }.sumOf { row ->
        var updated = false
        (0..row.size - 2).map { first ->
            (row.size - 1 downTo first + 1).map { last ->
                 if (reversedRules.contains(row[first] to row[last])) {
                     updated = true
                     val temp = row[first]
                     row[first] = row[last]
                     row[last] = temp
                 }
            }
        }
        if (updated) {
            row[row.size / 2]
        } else {
            0
        }
    }
}
