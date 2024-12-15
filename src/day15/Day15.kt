package day15

import getOrFetchInputDataAsString
import getTestInputAsString
import java.rmi.UnexpectedException

// print statement counter: 2

enum class Direction(
    val sign: Char,
    val d: Pair<Int, Int>
) {
    UP('^', -1 to 0),
    DOWN('v', 1 to 0),
    LEFT('<', 0 to -1),
    RIGHT('>', 0 to 1);

    companion object {
        fun parse(c: Char): Direction = entries.find { it.sign == c }!!
    }
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + other.first to this.second + other.second
}

fun main() {
    val testInput = getTestInputAsString(15, "example")
    val test1 = part1(testInput)
    check(test1 == 2028) { "Got $test1" }

    val testInputLarge = getTestInputAsString(15, "example_large")
    val testLarge = part1(testInputLarge)
    check(testLarge == 10092) { "Got: $testLarge" }
    val test2 = part2(testInputLarge)
    check(test2 == 9021) { "Got $test2" }

    val input = getOrFetchInputDataAsString(15)
    println(part1(input))
    println(part2(input))
}

fun move(map: List<MutableList<Char>>, d: Direction, pos: Pair<Int, Int>): Boolean {
    val nextPos = pos + d.d
    val nextVal = map[nextPos.first][nextPos.second]
    return when (nextVal) {
        '#' -> false
        '.' -> {
            map[nextPos.first][nextPos.second] = map[pos.first][pos.second]
            map[pos.first][pos.second] = '.'
            true
        }

        'O' -> {
            if (move(map, d, nextPos)) {
                map[nextPos.first][nextPos.second] = map[pos.first][pos.second]
                map[pos.first][pos.second] = '.'
                true
            } else {
                false
            }
        }

        else -> {
            println("failed moving from $pos in direction $d on map:")
            println("$map")
            throw UnexpectedException("git gud")
        }
    }
}

fun canImoveMyBoiWithoutActuallyMovingHim(map: List<List<Char>>, d: Direction, pos: Pair<Int, Int>): Boolean {
    val nextPos = pos + d.d
    return when (val nextVal = map[nextPos.first][nextPos.second]) {
        '#' -> false
        '.' -> true
        '[', ']' -> {
            val directionToOther = if (nextVal == ']') Direction.LEFT else Direction.RIGHT
            if (d == Direction.UP || d == Direction.DOWN) {
                canImoveMyBoiWithoutActuallyMovingHim(map, d, nextPos) &&
                        canImoveMyBoiWithoutActuallyMovingHim(map, d, nextPos + directionToOther.d)
            } else {
                true
            }
        }

        else -> {
            println("failed moving from $pos in direction $d on map:")
            map.forEach { println(it) }
            throw UnexpectedException("git gud")
        }
    }
}

fun largeMove(map: List<MutableList<Char>>, d: Direction, pos: Pair<Int, Int>, approvedMove: Boolean = false): Boolean {
    val nextPos = pos + d.d
    return when (val nextVal = map[nextPos.first][nextPos.second]) {
        '#' -> false
        '.' -> {
            map[nextPos.first][nextPos.second] = map[pos.first][pos.second]
            map[pos.first][pos.second] = '.'
            true
        }

        '[', ']' -> {
            val directionToOther = if (nextVal == ']') Direction.LEFT else Direction.RIGHT
            if (d == Direction.UP || d == Direction.DOWN) {
                return if (approvedMove || canImoveMyBoiWithoutActuallyMovingHim(map, d, pos)) {
                    largeMove(map, d, nextPos, true) && largeMove(map, d, nextPos + directionToOther.d, true)
                    map[nextPos.first][nextPos.second] = map[pos.first][pos.second]
                    map[pos.first][pos.second] = '.'
                    true
                } else {
                    false
                }
            } else {
                if (largeMove(map, d, nextPos)) {
                    map[nextPos.first][nextPos.second] = map[pos.first][pos.second]
                    map[pos.first][pos.second] = '.'
                    true
                } else {
                    false
                }
            }
        }

        else -> {
            println("failed moving from $pos in direction $d on map:")
            map.forEach { println(it) }
            throw UnexpectedException("git gud")
        }
    }
}

fun part1(input: String): Int {
    val (i1, i2) = input.split("(\\n){2}".toRegex())
    val map = i1.split("\n").map { it.toMutableList() }
    val instructions = i2.replace("\\s+".toRegex(), "")
    var robot: Pair<Int, Int> = 0 to 0
    map.indices.forEach { row -> map[row].indices.forEach { if (map[row][it] == '@') robot = row to it } }

    instructions.forEach {
        val dir = Direction.parse(it)
        if (move(map, dir, robot)) {
            robot += dir.d
        }
    }

    return map.mapIndexed { rowNum, row ->
        row.mapIndexed { colNum, char ->
            if (char == 'O') {
                rowNum * 100 + colNum
            } else {
                0
            }
        }.sum()
    }.sum()
}

fun part2(input: String): Int {
    val (i1, i2) = input.split("(\\n){2}".toRegex())
    val map = i1.split("\n")
        .map { row ->
            row.map { char ->
                when (char) {
                    '#' -> "##"
                    'O' -> "[]"
                    '.' -> ".."
                    '@' -> "@."
                    else -> throw UnexpectedException("Whatever $char is")
                }
            }
                .joinToString("")
        }
        .map { it.toMutableList() }
    val instructions = i2.replace("\\s+".toRegex(), "")
    var robot: Pair<Int, Int> = 0 to 0
    map.indices.forEach { row -> map[row].indices.forEach { if (map[row][it] == '@') robot = row to it } }

    instructions.forEach {
        val dir = Direction.parse(it)
        if (largeMove(map, dir, robot)) {
            robot += dir.d
        }
    }

    return map.mapIndexed { rowNum, row ->
        row.mapIndexed { colNum, char ->
            if (char == '[') {
                rowNum * 100 + colNum
            } else {
                0
            }
        }.sum()
    }.sum()
}
