package day08

import getOrFetchInputData
import getTestInput

typealias Position = Pair<Int, Int>
typealias Segment = Pair<Position, Position> // ako sa povie úsečka anyways

fun main() {
    val testInput = getTestInput(8, "example")
    val test1 = part1(testInput)
    check(test1 == 14) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 34) { "Got $test2" }

    val input = getOrFetchInputData(8)
    println(part1(input))
    println(part2(input))
}

fun List<Position>.getPairs(): List<Segment> {
    val pairs = mutableListOf<Segment>()
    for (i in this.indices) {
        for (j in i + 1 until this.size) {
            pairs.add(Segment(this[i], this[j]))
        }
    }
    return pairs
}

operator fun Position.plus(other: Position): Position {
    return this.first + other.first to this.second + other.second
}

operator fun Position.minus(other: Position): Position {
     return this.first - other.first to this.second - other.second
}

fun Position.reversed(): Position = this.first * -1 to this.second * -1

fun Segment.getAntinodes(): List<Position> {
    val vector = this.first - this.second
    return listOf(first + vector, second - vector)
}

fun Position.getPeriodicAntinodes(map: List<String>, vector: Pair<Int, Int>): List<Position> {
    val ret = mutableListOf(this)
    var next = this + vector
    while (next.first in map.indices && next.second in map[0].indices) {
        ret.add(next)
        next += vector
    }
    return ret
}

fun Segment.getPeriodicAntinodes(map: List<String>): List<Position> {
    val vector = this.first - this.second
    return this.first.getPeriodicAntinodes(map, vector) + this.second.getPeriodicAntinodes(map, vector.reversed())
}

fun part1(input: List<String>): Int {
    return input.asSequence().flatMapIndexed { row, values ->
        values.mapIndexedNotNull { column, value ->
            if (value != '.') {
                value to (row to column)
            } else {
                null
            }
        }
    }
        .groupBy({ it.first }, { it.second })
        .flatMap { it.value.getPairs() }
        .flatMap { it.getAntinodes() }
        .distinct()
        .count { it.first in input.indices && it.second in input[0].indices }
}

fun part2(input: List<String>): Int {
    return input.asSequence().flatMapIndexed { row, values ->
        values.mapIndexedNotNull { column, value ->
            if (value != '.') {
                value to (row to column)
            } else {
                null
            }
        }
    }
        .groupBy({ it.first }, { it.second })
        .asSequence()
        .flatMap { it.value.getPairs() }
        .flatMap { it.getPeriodicAntinodes(input) }
        .distinct()
        .sortedBy { it.first }
        .count {
            it.first in input.indices && it.second in input[0].indices
        }
}
