package day02

import getOrFetchInputData
import getTestInput
import kotlin.math.abs

// print statements counter: 7

fun main() {
    val testInput = getTestInput(2, "example")
    val test1 = part1(testInput)
    check(test1 == 2) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 4) { "Got $test2" }

    val customTest = getTestInput(2, "custom_test")
    val test3 = part2(customTest)
    check(test3 == 5) { "Got $test3" }

    val input = getOrFetchInputData(2)
    println(part1(input))
    println(part2(input))
}

fun List<String>.toDifferencesLists() = this.asSequence()
    .map { row -> row.split("\\s+".toRegex()).map { it.toInt() } }
    .map { it.zipWithNext() }
    .map { row -> row.map { it.first - it.second } }

fun List<Int>.isSafe(): Boolean {
    return this.all { abs(it) in 1..3 } && (this.all { it > 0 } || this.all { it < 0 })
}

fun List<Int>.checkRemovedIndex(i: Int): Boolean {
    val firstPart = this.slice(0..i - 2)
    val lastPart = this.slice(i + 2..<this.size)
    return when (i) {
        0 -> {
            this.slice(1..<this.size).isSafe() || (listOf(this[0] + this[1]) + lastPart).isSafe()
        }
        this.size - 1 -> {
            this.slice(0..this.size - 2).isSafe() || (firstPart + (this[i - 2] + this[i - 1])).isSafe()
        }
        else -> {
            val removed1 = firstPart + this[i - 1] + (this[i] + this[i + 1]) + lastPart
            val removed2 = firstPart + (this[i - 1] + this[i]) + this[i + 1] + lastPart
            removed1.isSafe() || removed2.isSafe()
        }
    }
}

fun List<Int>.isTolerable(): Boolean {
    indices.forEach {
        val current = this[it]
        val next = this.getOrElse(it + 1) { 0 }
        val notSameSign = current * next < 0
        val dangerousChange = abs(current) !in 1..3
        if (dangerousChange) {
            return this.checkRemovedIndex(it)
        }
        if (notSameSign) {
            return this.checkRemovedIndex(it) || this.checkRemovedIndex(it + 1)
        }
    }
    return true
}

fun part1(input: List<String>): Int {
    return input
        .toDifferencesLists()
        .filter { it.isSafe() }
        .count()
}

fun part2(input: List<String>): Int {
    return input
        .toDifferencesLists()
        .filter { it.isTolerable() }
        .count()
}
