package day13

import getOrFetchInputDataAsString
import getTestInputAsString

typealias Coordinates = Pair<Long, Long>

fun main() {
    val testInput = getTestInputAsString(13, "example")
    val test1 = part1(testInput)
    check(test1 == 480L) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 875318608908L) { "Got $test2" }

    val input = getOrFetchInputDataAsString(13)
    println(part1(input))
    println(part2(input))
}

operator fun Coordinates.plus(other: Coordinates): Coordinates {
    return this.first + other.first to this.second + other.second
}

fun getSolution(a: Coordinates, b: Coordinates, prize: Coordinates): Pair<Long, Long>? {
    var aCount = 0L
    while (aCount * a.first <= prize.first && aCount * a.second <= prize.second) {
        if ((prize.first - aCount * a.first) % b.first == 0L) {
            val mult = (prize.first - aCount * a.first) / b.first
            if (b.second * mult + aCount * a.second == prize.second) {
                return aCount to mult
            }
        }
        ++aCount
    }
    return null
}

fun mrGaussHelpPLis(a: Coordinates, b: Coordinates, prize: Coordinates): Pair<Long, Long>? {
    val det = (a.first * b.second - b.first * a.second)
    val aCount = (prize.first * b.second - prize.second * b.first) / det
    val bCount = (a.first * prize.second - a.second * prize.first) / det
    if (aCount <= 0 || bCount <= 0 || a.first * aCount + b.first * bCount != prize.first
        || a.second * aCount + b.second * bCount != prize.second) {
        return null
    }
    return aCount to bCount
}

fun part1(input: String): Long {
    val games = input
        .split("(\\n){2}".toRegex())
        .filter { it.isNotEmpty() }
        .map { string ->
            val (a, b, prize) = ".*X[=|+](\\d+), Y[=|+](\\d+)".toRegex()
                .findAll(string)
                .map {
                    val (x, y) = it.destructured
                    x.toLong() to y.toLong()
                }
                .toList()
            getSolution(a, b, prize)
        }

    return games.filterNotNull().sumOf { g -> g.first * 3L + g.second }
}

fun part2(input: String): Long {
    val games = input
        .split("(\\n){2}".toRegex())
        .filter { it.isNotEmpty() }
        .map { string ->
            val (a, b, prize) = ".*X[=|+](\\d+), Y[=|+](\\d+)".toRegex()
                .findAll(string)
                .map {
                    val (x, y) = it.destructured
                    x.toLong() to y.toLong()
                }
                .toList()
            mrGaussHelpPLis(a, b, prize + (10000000000000L to 10000000000000L))
        }

    return games.filterNotNull().sumOf { g -> g.first * 3L + g.second }
}
