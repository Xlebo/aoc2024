package day06

import getOrFetchInputData
import getTestInput

typealias Position = Pair<Int, Int>
typealias Guard = Pair<Position, Direction>

// print statements counter: 8
// times I tried to fuck around the system and failed: 3
// #nebudutodelat screams: 5

enum class Direction(
    val direction: Pair<Int, Int>
) {
    UP(-1 to 0),
    RIGHT(0 to 1),
    DOWN(1 to 0),
    LEFT(0 to -1);

    fun turnRight(): Direction {
        return entries[(ordinal + 1) % entries.size]
    }
}

fun Position.plus(direction: Direction): Position {
    return this.first + direction.direction.first to this.second + direction.direction.second
}

fun Position.isOnMap(input: List<String>): Boolean {
    return this.first in input.indices && this.first in input[0].indices
}

fun main() {
    val testInput = getTestInput(6, "example")
    val test1 = part1(testInput)
    check(test1 == 41) { "Got $test1" }

    val test2 = part2(testInput)
    check(test2 == 6) { "Got $test2" }

    val input = getOrFetchInputData(6)
    println(part1(input))
    println(part2(input))
}

fun getGuard(input: List<String>): Guard {
    input.indices.forEach { row ->
        input[row].indices.forEach { column ->
            if (input[row][column] == '^')
                return row to column to Direction.UP
        }
    }
    throw IllegalArgumentException("Guard took a day off")
}

fun canMyFellaHopToLoop(
    guard: Guard,
    map: List<List<Char>>,
    obstacle: Position,
    startingPositions: List<Pair<Position, Direction>> = listOf()
): Boolean {
    if (map[obstacle.first][obstacle.second] in listOf('X', '+')) {
        return false
    }
    val currentDir = guard.second.turnRight()
    var currentPos = guard.first
    var nextToGuard = guard.first.first + currentDir.direction.first to guard.first.second + currentDir.direction.second
    while (nextToGuard.first in map.indices && nextToGuard.second in map[0].indices) {
        if ((currentPos to currentDir) in startingPositions) {
            return true
        }
        if (map[nextToGuard.first][nextToGuard.second] == '#' || nextToGuard == obstacle) {
            return canMyFellaHopToLoop(
                currentPos to currentDir,
                map,
                obstacle,
                startingPositions + (guard.first to currentDir)
            )
        }
        currentPos = nextToGuard
        nextToGuard = nextToGuard.first + currentDir.direction.first to nextToGuard.second + currentDir.direction.second
    }
    return false
}

fun mapOfGuardRoute(input: List<String>): Pair<List<MutableList<Char>>, MutableSet<Position>> {
    val map = input.map { it.toMutableList() }
    var guard = getGuard(input)
    val obstacles = mutableSetOf<Position>()
    do {
        val newPos = guard.first.plus(guard.second)
        guard = if (map.getOrNull(newPos.first)?.getOrNull(newPos.second) == '#') {
            map[guard.first.first][guard.first.second] = '+'
            guard.first to guard.second.turnRight()
        } else {
            if (map[guard.first.first][guard.first.second] != '+') {
                map[guard.first.first][guard.first.second] = 'X'
            }
            if (newPos.isOnMap(input) && canMyFellaHopToLoop(guard, map, newPos)) {
                obstacles.add(newPos)
            }
            newPos to guard.second
        }
    } while (guard.first.isOnMap(input))
    return map to obstacles
}

fun part1(input: List<String>): Int {
    return mapOfGuardRoute(input).first.sumOf { row ->
        row.count { it == 'X' || it == '+' }
    }
}

fun part2(input: List<String>): Int {
    val (_, obstacles) = mapOfGuardRoute(input)

    return obstacles.distinct().size
}
