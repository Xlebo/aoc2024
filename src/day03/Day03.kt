package day03

import getOrFetchInputData
import getTestInput

// print counter: 2

fun main() {
    val testInput = getTestInput(3, "example")
    val test1 = part1(testInput)
    check(test1 == 161) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 48) { "Got $test2" }

    val input = getOrFetchInputData(3)
    println(part1(input))
    println(part2(input))
}

fun part2(input: List<String>): Int {
    val regex = "mul\\((\\d+,\\d+)\\)|(don't)\\(\\)|(do)\\(\\)".toRegex()
    var mul = true
    return input.sumOf { row ->
        regex.findAll(row)
            .sumOf { match ->
                var value = 0
                val (command, dont, doo) = match.destructured
                when {
                    dont.isNotEmpty() -> mul = false
                    doo.isNotEmpty() -> mul = true
                    else -> if (mul) {
                        val values = command.split(",")
                        value = values[0].toInt() * values[1].toInt()
                    }
                }
                value
            }
    }
}

fun part1(input: List<String>): Int {
    val regex = "mul\\((\\d+),(\\d+)\\)".toRegex()
    return input.sumOf { row ->
        regex.findAll(row)
            .sumOf { match ->
                val (first, second) = match.destructured
                first.toInt() * second.toInt()
            }
    }
}
