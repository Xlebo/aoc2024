import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun fetchInputData(day: Int = 1): String {
    val cookie = System.getProperty("cookie")
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
        .setHeader(
            "cookie",
            "session=$cookie"
        )
        .uri(URI.create("https://adventofcode.com/2024/day/$day/input"))
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return response.body()
}

fun getOrFetchInputDataAsString(day: Int = 1): String {
    val parent = "day${day.toString().padStart(2, '0')}"
    val f = File("src\\$parent", "${parent}_input.txt")

    if (!f.exists()) {
        f.createNewFile()
        f.writeBytes(fetchInputData(day).toByteArray())
    }

    return f.readText()
}

fun getOrFetchInputData(day: Int = 1): List<String> {
    val parent = "day${day.toString().padStart(2, '0')}"
    val f = File("src\\$parent", "${parent}_input.txt")

    if (!f.exists()) {
        f.createNewFile()
        f.writeBytes(fetchInputData(day).toByteArray())
    }

    return f.readLines()
}

fun getTestInput(day: Int, file: String): List<String> {
    return File("src\\day${day.toString().padStart(2, '0')}", "$file.txt").readLines()
}

fun getTestInputAsString(day: Int, file: String): String {
    return File("src\\day${day.toString().padStart(2, '0')}", "$file.txt").readText()
}
