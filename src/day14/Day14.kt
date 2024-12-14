package day14

import day14.Robot.Companion.X_SIZE
import day14.Robot.Companion.Y_SIZE
import getOrFetchInputData
import getTestInput
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer


data class Robot(
    var position: Pair<Int, Int>,
    val velocity: Pair<Int, Int>
) {
    fun move() {
        position = Math.floorMod(
            (position.first + velocity.first),
            X_SIZE
        ) to Math.floorMod((position.second + velocity.second), Y_SIZE)
    }

    companion object {
        var X_SIZE: Int = 101
        var Y_SIZE: Int = 103
    }
}

class Printer(
    val input: List<String>
) : JPanel() {
    private val robots = input.map {
        val (one, two, three, four) = "(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex().find(it)!!.destructured
        Robot(one.toInt() to two.toInt(), three.toInt() to four.toInt())
    }
    private var counter = 0
    private var grid: List<BooleanArray> = listOf()
    var timer: Timer = Timer(100) {
        if (this.updateGrid())
            this.repaint()
    }

    fun setupGrid() {
        grid = List(X_SIZE) { BooleanArray(Y_SIZE) { false } }
        (0.. 6200).forEach { _ ->
            robots.forEach { robot ->
                robot.move()
            }
        }
        counter = 6200
        robots.forEach {
            grid[it.position.first][it.position.second] = true
        }
    }

    private fun updateGrid(): Boolean {
        // dummy condition for tree detection, hoping there is not much of big square patterns :D
        if (grid.any { row -> row.count { it } > 20 } && grid[0].indices.any { column -> grid.indices.count { grid[it][column] } > 20}) {
            timer.stop()
            return false
        }
        grid = List(X_SIZE) { BooleanArray(Y_SIZE) { false } }
        robots.forEach {
            it.move()
            grid[it.position.first][it.position.second] = true
        }
        return true
    }

    override fun getPreferredSize(): Dimension {
        return Dimension(X_SIZE * 10, Y_SIZE * 10 + 20)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val gColor = g.color
        g.drawString("Step: ${counter++}", 0, 10)
        grid.indices.forEach { column ->
            grid[column].indices.forEach { row ->
                if (grid[column][row]) {
                    g.color = Color.GREEN
                    g.fillRect(column * 10, row * 10 + 20, 10, 10)
                }
            }
        }
        g.color = gColor
    }
}

fun main() {
    val testInput = getTestInput(14, "example")
    val test1 = part1(testInput, 11, 7)
    check(test1 == 12L) { "Got: $test1" }

    val input = getOrFetchInputData(14)
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>, x: Int = 101, y: Int = 103): Long {
    X_SIZE = x
    Y_SIZE = y
    val robots = input.map {
        val (one, two, three, four) = "(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex().find(it)!!.destructured
        Robot(one.toInt() to two.toInt(), three.toInt() to four.toInt())
    }
    (1..100).forEach { _ ->
        robots.forEach { it.move() }
    }
    var (q1, q2, q3, q4, out) = listOf(0L, 0L, 0L, 0L, 0L)
    robots.forEach { robot ->
        when {
            (robot.position.first < X_SIZE / 2 && robot.position.second < Y_SIZE / 2) -> q1++
            (robot.position.first > X_SIZE / 2 && robot.position.second < Y_SIZE / 2) -> q2++
            (robot.position.first < X_SIZE / 2 && robot.position.second > Y_SIZE / 2) -> q3++
            (robot.position.first > X_SIZE / 2 && robot.position.second > Y_SIZE / 2) -> q4++
            else -> out++
        }
    }
    return q1 * q2 * q3 * q4
}

fun part2(input: List<String>) {
    val p = Printer(input)
    p.setupGrid()
    val frame = JFrame()
    frame.contentPane.add(p)
    frame.pack()
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isLocationByPlatform = true
    frame.isVisible = true
    p.timer.start()
}


