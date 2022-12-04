import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader

object ResourceFetcher {
    @Throws(FileNotFoundException::class)
    fun use(resourcePath: String, operation: BufferedReader.() -> Unit) {
        this::class.java.getResourceAsStream(resourcePath)?.let{ input ->
            BufferedReader(InputStreamReader(input)).apply{
                operation()
                close()
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