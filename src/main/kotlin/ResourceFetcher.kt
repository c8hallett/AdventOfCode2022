import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

object ResourceFetcher {
    @Throws(FileNotFoundException::class)
    inline fun <T> with(resourcePath: String, operation: BufferedReader.() -> T): T {
        return this::class.java.getResourceAsStream(resourcePath)?.let{ input ->
             BufferedReader(InputStreamReader(input)) .use { bufferedReader ->
                bufferedReader.operation()
            }
        } ?: throw FileNotFoundException("Resource '$resourcePath' does not exist.")
    }

    @Throws(FileNotFoundException::class)
    fun forEachLine(resourcePath: String, operation: (String) -> Unit) {
        this::class.java.getResourceAsStream(resourcePath)?.let{ input ->
            BufferedReader(InputStreamReader(input)).apply{
                readLines().forEach(operation)
                close()
            }
        } ?: throw FileNotFoundException("Resource '$resourcePath' does not exist.")
    }
}