package day17

fun main() {
    val tests = listOf(
        { test1() },
        { test2() },
        { test3() },
        { test4() },
        { test5() }
    )

    tests.forEach {
        it.invoke()
        println("${it}: OK")
    }
}


fun test1() {
    val c = Computer(0, 0, 9, intArrayOf(2, 6))
    c.processInstruction()
    check(c.b == 1)
    check(c.c == 9)
}

fun test2() {
    val c = Computer(10, 0, 0, intArrayOf(5, 0, 5, 1, 5, 4))
    c.processInstruction()
    c.processInstruction()
    c.processInstruction()
    check(c.output.toString() == "012")
}

fun test3() {
    val c = Computer(2024, 0, 0, intArrayOf(0, 1, 5, 4, 3, 0))
    while (c.processInstruction());
    check(c.a == 0)
    check(c.output.toString() == "42567777310")
}

fun test4() {
    val c = Computer(0, 29, 0, intArrayOf(1, 7))
    c.processInstruction()
    check(c.b == 26)
}

fun test5() {
    val c = Computer(0, 2024, 43690, intArrayOf(4, 0))
    c.processInstruction()
    check(c.b == 44354)
    check(c.c == 43690)
}