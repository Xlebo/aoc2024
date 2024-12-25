package day17

import getOrFetchInputDataAsString
import getTestInputAsString
import kotlin.math.pow

fun main() {
//    val testInput = getTestInputAsString(17, "example")
//    val test1 = part1(testInput)
//    check(test1 == "4,6,3,5,6,3,5,2,1,0") { "Got $test1" }

    val testInput2 = getTestInputAsString(17, "example2")
//    val testtest = part1(testInput2)
//    println("test1 of test2 result: $testtest")
//    val test2 = part2(testInput2)
//    check(test2 == 117440L) { "Got: $test2 " }

    val input = getOrFetchInputDataAsString(17)
//    println(part1(input))
    println(part2(input))
    val testtest = part1(input)
    println("test1 of test2 result: $testtest")
}

class Computer(
    var a: Long,
    var b: Long,
    var c: Long,
    val input: IntArray,
    var output: StringBuilder = StringBuilder(),
    var reverseOutput: MutableList<Char> = mutableListOf(),
    var pointer: Int = 0
) {
    fun comboOperand(v: Long): Long {
        return when {
            v < 4 -> v
            v == 4L -> a
            v == 5L -> b
            v == 6L -> c
            else -> throw UnknownError("IDK this should not happen")
        }
    }

    fun comboOperandSet(o: Int, v: Long) {
        when (o) {
            4 -> a = v
            5 -> b = v
            6 -> c = v
        }
    }

    fun processInstruction(): Boolean {
        if (input.size <= pointer + 1) {
            return false
        }
        val i = input[pointer]
        val operand = input[pointer.inc()].toLong()
        when (i) {
            0 -> a = (a / (2).toDouble().pow(comboOperand(operand).toInt())).toLong()
            1 -> b = b xor operand
            2 -> b = comboOperand(operand) % 8
            3 -> if (a != 0L) pointer = operand.toInt()
            4 -> b = b xor c
            5 -> output.append(comboOperand(operand) % 8)
            6 -> b = (a / (2).toDouble().pow(comboOperand(operand).toInt())).toLong()
            7 -> c = (a / (2).toDouble().pow(comboOperand(operand).toInt())).toLong()
        }
        if (i != 3 || a == 0L) {
            pointer += 2
        }
        return true
    }

    fun setUpReverse() {
        input.forEach {
            reverseOutput.add(it.digitToChar())
        }
        pointer = input.size - 2
    }

    fun reverse(): Boolean {
        if (pointer == 0 && reverseOutput.isEmpty()) {
            return false
        }
        val i = input[pointer]
        val operand = input[pointer.inc()]
        when (i) {
            0 -> {
                a = (a * (2).toDouble().pow(comboOperand(operand.toLong()).toInt())).toLong()
            }

            1 -> b = b xor operand.toLong()
            2 -> {
                val combo = comboOperand(operand.toLong())
                var bmod = b % 8
                comboOperandSet(operand, combo + bmod)
            }
            3 -> {}
            4 -> { b = b xor c }
            5 -> {
                val value = reverseOutput.removeLast().digitToInt().toLong()
//                val combo = comboOperand(operand.toLong())
//                if (combo % 8 != value) {
//                    while (++combo % 8 != value);
//                }
//                else {
//                    combo *= 8
//                }
                comboOperandSet(operand, value)
            }

            6 -> {}
            7 -> { c = (a * 8 + (b xor 1)) / (2).toDouble().pow((b xor 1).toDouble()).toLong() }
        }

        if (pointer > 0) {
            pointer -= 2
        } else {
            pointer = input.indices.find { input[it] == 3 && it % 2 == 0 }!!
        }

        return true
    }

    override fun toString(): String {
        return "Computer(a=$a, b=$b, c=$c, currentInput=${input.getOrNull(pointer) ?: "X"}, ${input.getOrNull(pointer + 1) ?: "X"}, pointer=$pointer, output=$output, reverse=$reverseOutput)"
    }

}

fun part1(input: String): String {
    val (i1, i2) = input.split("(\\n){2}".toRegex())
    val (a, b, c) = i1.split('\n')
        .map { "(\\d+)".toRegex().find(it)!!.value.toLong() }
    val instructions = i2.split("[, ]".toRegex()).filter { it.length < 2 }
        .map { it.toInt() }.toIntArray()
    val computer = Computer(a, b, c, instructions)
    do {
        println(computer)
    } while (computer.processInstruction())
    return computer.output.toString().toCharArray().joinToString(",")
}

fun part2(input: String): Long {
    val (i1, i2) = input.split("(\\n){2}".toRegex())
    val (_, b, c) = i1.split('\n')
        .map { "(\\d+)".toRegex().find(it)!!.value.toLong() }
    val instructions = i2.split("[, ]".toRegex()).filter { it.length < 2 }
        .map { it.toInt() }.toIntArray()

    val computer = Computer(0, b, c, instructions)
    computer.setUpReverse()
    do {
        println(computer)
    } while (computer.reverse())

    return computer.a
}
