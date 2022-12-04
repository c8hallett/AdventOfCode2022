
fun String.bold(): String = "\u001B[1m$this\u001B[0m"

@Throws(IllegalArgumentException::class)
fun Regex.checkValidMatch(
    text: String,
    ifValid: (List<String>) -> Unit
) {
    matchEntire(text)?.let {
        ifValid(it.groupValues.drop(1))
    } ?: throw IllegalArgumentException("'$text' does not fully match Regex '$this'")
}