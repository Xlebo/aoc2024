package day11

import getOrFetchInputDataAsString
import getTestInputAsString

fun main() {
    val testInput = getTestInputAsString(11, "example")
    val test1 = part1(testInput)
    check(test1 == 55312) { "Got $test1" }

//    val test2 = part2(testInput)
//    check(test2 == 81) { "Got $test2" }

    val input = getOrFetchInputDataAsString(11)
    println(part1(input))
    println(part2(input))
}

fun part1(input: String): Int {
    var inputs = input.split("\\s+".toRegex()).filter { it.isNotEmpty() }
    (1..25).forEach { _ ->
        inputs = inputs.flatMap { value ->
            when {
                value == "0" -> listOf("1")
                value.length % 2 == 0 -> {
                    val first = value.substring(0..< (value.length / 2))
                    val second = value.substring((value.length / 2 )..<value.length).replaceFirst("^0+".toRegex(), "").ifEmpty { "0" }
                    listOf(first, second)
                }
                else -> listOf((value.toLong() * 2024).toString())
            }
        }
    }
    return inputs.count()
}

fun part2(input: String): Long {
    val stack = input.split("\\s+".toRegex()).filter { it.isNotEmpty() }.map { it to 75 }.toMutableList()
    val cache = mutableMapOf<Pair<String, Int>, Long>()
    while (stack.isNotEmpty()) {
        val current = stack.removeFirst()
        if (current.second < 1) {
            cache[current] = 1
            continue
        }
        if (cache.containsKey(current)) {
            continue
        }
        val newValues = when {
            current.first == "0" -> listOf("1" to current.second - 1)
            current.first.length % 2 == 0 -> {
                val first = current.first.substring(0..< (current.first.length / 2))
                val second = current.first.substring((current.first.length / 2 )..<current.first.length).replaceFirst("^0+".toRegex(), "").ifEmpty { "0" }
                listOf(first to current.second - 1, second to current.second - 1)
            }
            else -> listOf((current.first.toLong() * 2024).toString() to current.second - 1)
        }
        if (newValues.all { cache.containsKey(it) }) {
            cache[current] = newValues.sumOf { cache[it]!!.toLong() }
        } else {
            stack.addFirst(current)
            newValues.filter { !cache.containsKey(it) }.forEach {
                stack.addFirst(it)
            }
        }
    }
    return input.split("\\s+".toRegex()).filter { it.isNotEmpty() }.sumOf { cache[it to 75]!! }
}
