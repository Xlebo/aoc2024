package day01

import getOrFetchInputData
import getTestInput
import kotlin.math.abs

fun main() {
    fun List<String>.asListsOfNumbers() = this.asSequence().flatMap { row ->
        row.split(' ')
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
    }
        .withIndex()
        .groupBy { it.index % 2 == 0 }
        .map { column -> column.value.map { it.value} }

    fun part1(input: List<String>): Int {
        return input.asListsOfNumbers()
            .asSequence()
            .map { it.sorted() }
            .zipWithNext()
            .map { it.first.withIndex().map { index -> abs(it.first[index.index] - it.second[index.index]) } }
            .first()
            .sum()
    }

    fun part2(input: List<String>): Long {
        val (count, values) = input.asListsOfNumbers()
        val valuesMap = values.groupingBy { it }.eachCount()
        return count.sumOf {
            it * (valuesMap[it]?.toLong() ?: 0)
        }
    }

    val testInput = getTestInput(1, "example")
    val test1 = part1(testInput)
    check(test1 == 11) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 31L) { "Got $test2" }

    val input = getOrFetchInputData(1)
    println(part1(input))
    println(part2(input))
}
