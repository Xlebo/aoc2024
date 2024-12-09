package day09

import getOrFetchInputData
import getTestInput

// print counter: 1

typealias File = MutableList<Int>

fun File.getFreeSpace(): Int {
    val firstOccurrence = this.indexOfFirst { it == -1 }
    if (firstOccurrence < 0)
        return 0
    return this.size - firstOccurrence
}

fun File.hasFreeSpace(): Boolean {
    return this.getFreeSpace() != 0
}

fun File.copyValues(file: File) {
    val firstIndex = this.indexOfFirst { it == -1 }
    (0..<file.size).forEach {
        this[firstIndex + it] = file[it]
    }
}

fun File.empty() {
    this.indices.forEach {
        this[it] = -1
    }
}

fun main() {
    val testInput = getTestInput(9, "example")
    val test1 = part1(testInput)
    check(test1 == 1928L) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 2858L) { "Got $test2" }

    val input = getOrFetchInputData(9)
    println(part1(input))
    println(part2(input))
}

fun String.decompress(): List<Int> {
    var id = 0
    return this.foldIndexed(listOf()) { index, acc, pos ->
        if (index % 2 == 0) {
            val currentId = id++
            acc + List(pos.digitToInt()) { currentId }
        } else {
            acc + List(pos.digitToInt()) { -1 }
        }
    }
}

fun List<Int>.moveBlocks(): List<Int> {
    var start = 0
    var end = this.size - 1
    val result = mutableListOf<Int>()
    while (start <= end) {
        if (this[start] < 0) {
            if (this[end] < 0) {
                end--
            } else {
                result += this[end]
                start++
                end--
            }
        } else {
            result += this[start]
            start++
        }
    }
    return result.filter { it >= 0 }
}

fun String.sortedFiles(): List<Long> {
    var id = 0
    val files = this.mapIndexed { index, file ->
        if (index % 2 == 0) {
            val currentId = id++
            MutableList(file.digitToInt()) { currentId }
        } else {
            MutableList(file.digitToInt()) { -1 }
        }
    }

    files.indices.reversed().forEach { reversedIndex ->
        val toBeMoved = files[reversedIndex]
        if (!toBeMoved.hasFreeSpace()) {
            (0..reversedIndex).forEach { index ->
                if (files[index].getFreeSpace() >= toBeMoved.size) {
                    files[index].copyValues(toBeMoved)
                    toBeMoved.empty()
                }
            }
        }

    }

    return files.flatMap { row -> row.map { it.toLong() } }
}


fun part1(input: List<String>): Long {
    return input.fold(0L) { acc, row ->
        var index = 0L
        acc + row.decompress().moveBlocks().fold(0L) { sum, id ->
            sum + index++ * id
        }
    }
}

fun part2(input: List<String>): Long {
    return input[0].sortedFiles().foldIndexed(0L) { index, acc, value ->
        if (value < 0) {
            acc
        } else {
            acc + value * index
        }
    }
}
