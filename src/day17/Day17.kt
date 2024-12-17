package day17

import getOrFetchInputData
import getTestInput


fun main() {
    val testInput = getTestInput(17, "example")
    val test1 = part1(testInput)
    check(test1 == 7036) { "Got $test1" }

    val testInput2 = getTestInput(17, "example2")
    val test2 = part1(testInput2)
    check(test2 == 11048) { "Got: $test2" }

//    val test3 = part2(testInput)
//    check(test3 == 45) { "Got $test3" }

//    val test4 = part2(testInput2)
//    check(test4 == 64) { "Got $test4" }

    val input = getOrFetchInputData(17)
    println(part1(input))
//    println(part2(input))
}

class Computer(
    var a: Int,
    var b: Int,
    var c: Int
) {
    fun interpretValue(v: Int): Int {
        return when {
            v < 4 -> v
            v == 4 -> a
            v == 5 -> b
            v == 6 -> c
            else -> throw UnknownError("IDK this should not happen")
        }
    }
}


fun part1(input: List<String>): Int {
    TODO("Not yet implemented")
}
