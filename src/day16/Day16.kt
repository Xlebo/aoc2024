package day16

import getOrFetchInputData
import getTestInput
import java.rmi.UnexpectedException
import java.util.TreeMap
import java.util.TreeSet

enum class Direction(
    val d: Pair<Int, Int>
) {
    UP(-1 to 0),
    DOWN(1 to 0),
    LEFT(0 to -1),
    RIGHT(0 to 1);

    fun getPerpendicularAndThis(): Set<Direction> {
        if (this == UP || this == DOWN) {
            return setOf(RIGHT, LEFT, this)
        }
        return setOf(UP, DOWN, this)
    }
}

class Tile(
    val pos: Pair<Int, Int>,
    val d: Direction,
    val value: Int
) : Comparable<Tile> {
    override fun compareTo(other: Tile): Int {
        if (value.compareTo(other.value) != 0) {
            return value.compareTo(other.value)
        }
        return if (this == other) 0 else -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tile

        if (pos != other.pos) return false
        if (d != other.d) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pos.hashCode()
        result = 31 * result + d.hashCode()
        return result
    }
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return this.first + other.first to this.second + other.second
}

fun explore(current: Tile, toExplore: TreeSet<Tile>, explored: MutableSet<Tile>) {
    current.d.getPerpendicularAndThis().forEach {
        var newVal = current.value + 1
        if (it != current.d) {
            newVal += 1000
        }
        val newPos = Tile(current.pos + it.d, it, newVal)
        if (!explored.contains(newPos)) {
            val existing = toExplore.find { tile -> tile == newPos }
            if (existing == null) {
                toExplore.add(newPos)
            } else {
                if (existing.value > newVal) {
                    toExplore.remove(existing)
                    toExplore.add(newPos)
                }
            }
        }
    }
}

fun exploreWithPath(
    current: Tile,
    currentPath: Set<Pair<Int, Int>>,
    toExplore: TreeMap<Tile, Set<Pair<Int, Int>>>,
    explored: MutableSet<Tile>
) {
    current.d.getPerpendicularAndThis().forEach {
        var newVal = current.value + 1
        if (it != current.d) {
            newVal += 1000
        }
        val newPos = Tile(current.pos + it.d, it, newVal)
        if (!explored.contains(newPos)) {
            val existing = toExplore.keys.find { tile -> tile == newPos }
            if (existing == null) {
                toExplore[newPos] = currentPath + newPos.pos
            } else {
                if (existing.value > newVal) {
                    toExplore.remove(existing)
                    toExplore[newPos] = currentPath + newPos.pos
                } else if (existing.value == newVal) {
                    val existingPath = toExplore[existing] ?: setOf()
                    toExplore[existing] = existingPath + currentPath + newPos.pos
                }
            }
        }
    }
}

fun main() {
    val testInput = getTestInput(16, "example")
    val test1 = part1(testInput)
    check(test1 == 7036) { "Got $test1" }

    val testInput2 = getTestInput(16, "example2.txt")
    val test2 = part1(testInput2)
    check(test2 == 11048) { "Got: $test2" }

    val test3 = part2(testInput)
    check(test3 == 45) { "Got $test3" }

    val test4 = part2(testInput2)
    check(test4 == 64) { "Got $test4" }

    val input = getOrFetchInputData(16)
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    var start = 0 to 0
    input.indices.forEach { row ->
        input[row].indices.forEach { column ->
            if (input[row][column] == 'S') {
                start = row to column
            }
        }
    }

    val toExplore = TreeSet<Tile>()
    val explored = HashSet<Tile>()
    toExplore.add(Tile(start, Direction.RIGHT, 0))

    while (toExplore.isNotEmpty()) {
        val tile = toExplore.first
        toExplore.remove(tile)
        explored.add(tile)

        when (input[tile.pos.first][tile.pos.second]) {
            '#' -> continue
            'E' -> return tile.value
            'S', '.' -> explore(tile, toExplore, explored)
            else -> throw UnexpectedException("Unexpected char: ${input[tile.pos.first][tile.pos.second]} on map:\n $input")
        }
    }
    throw UnexpectedException("How did this happen? We're smarter than this!")
}


fun part2(input: List<String>): Int {
    var startPos = 0 to 0
    input.indices.forEach { row ->
        input[row].indices.forEach { column ->
            if (input[row][column] == 'S') {
                startPos = row to column
            }
        }
    }

    val toExplore = TreeMap<Tile, Set<Pair<Int, Int>>>()
    val explored = HashSet<Tile>()
    val start = Tile(startPos, Direction.RIGHT, 0)
    toExplore[start] = setOf(start.pos)

    var fastestTrack = -1
    val paths = mutableSetOf<Pair<Int, Int>>()

    while (toExplore.isNotEmpty()) {
        val tile = toExplore.firstEntry()
        toExplore.remove(tile.key)
        explored.add(tile.key)

        when (input[tile.key.pos.first][tile.key.pos.second]) {
            '#' -> continue
            'E' -> {
                if (fastestTrack < 0) {
                    fastestTrack = tile.key.value
                    paths += tile.value
                } else if (tile.key.value == fastestTrack) {
                    paths += tile.value
                }
            }
            'S', '.' -> exploreWithPath(tile.key, tile.value, toExplore, explored)
            else -> throw UnexpectedException("Unexpected char: ${input[tile.key.pos.first][tile.key.pos.second]} on map:\n $input")
        }
    }

    return paths.size
}
