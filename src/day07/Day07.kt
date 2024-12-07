package day07

import getOrFetchInputData
import getTestInput
import kotlin.math.abs
import kotlin.math.log10


fun main() {
    val testInput = getTestInput(7, "example")
    val test1 = part1(testInput)
    check(test1 == 3749L) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 11387L) { "Got $test2" }

    val input = getOrFetchInputData(7)
    println(part1(input))
    println(part2(input))
}

fun Long.length() = when(this) {
    0L -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

fun equalsPlusOrMultiply(result: Long, base: Long, numbers: List<Long>, index: Int): Boolean {
    if (index >= numbers.size) {
        return base == result
    }
    if (base > result) {
        return false
    }
    val current = numbers[index]
    return equalsPlusOrMultiply(result,base * current, numbers, index.inc()) || equalsPlusOrMultiply(result, base + current, numbers, index.inc())
}

fun part1orConcat(result: Long, base: Long, numbers: List<Long>, index: Int): Boolean {
    if (index >= numbers.size) {
        return base == result
    }
    if (base > result) {
        return false
    }
    val current = numbers[index]
    return part1orConcat(result,base * current, numbers, index.inc()) ||
            part1orConcat(result, base + current, numbers, index.inc()) ||
            part1orConcat(result, base * ("1" + "0".repeat(current.length())).toLong() + current, numbers, index.inc())
}

fun part1(input: List<String>): Long {
   return input.sumOf { row ->
       val (first, second) = row.split(":")
       val result = first.toLong()
       val numbers = second
           .split("\\s+".toRegex())
           .filter { it.isNotEmpty() }
           .map { it.toLong() }
       val base = numbers[0]
       if (equalsPlusOrMultiply(result, base, numbers, 1)) result else 0
    }
}

fun part2(input: List<String>): Long {
    return input.sumOf { row ->
        val (first, second) = row.split(":")
        val result = first.toLong()
        val numbers = second
            .split("\\s+".toRegex())
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
        val base = numbers[0]
        if (part1orConcat(result, base, numbers, 1)) result else 0
    }
}
