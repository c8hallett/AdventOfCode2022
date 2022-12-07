package advent

import ResourceFetcher

fun main(args: Array<String>) {
    Day7.part2()
}

data class Node(
    val parent: Node?,
    val pathSegment: String,
    val children: MutableMap<String, Node> = mutableMapOf(),
    var totalSize: Long = 0L
)

class FileDirectory(filePath: String){
    private companion object{
        const val ROOT_DIR = "/"
        const val UP = ".."
        const val SPACE = "  "
        const val DASH = "- "
        val rootNode = Node(parent = null,"/")
    }

    private var currentNode = rootNode

    init {
        ResourceFetcher.forEachLine(filePath) { commandLine ->
            when{
                // command
                commandLine.take(4) == "$ cd" -> {
                    navigate(commandLine.slice(5..commandLine.lastIndex))
                }
                // file
                commandLine.first().isDigit() -> {
                    val fileSize = commandLine.split(" ").first().toLong()
                    addFile(fileSize)
                }
                // we don't really care about "$ ls" commands or directories without files in them
                else -> {}
            }
        }
        calculateDirectorySizes()
    }

    fun traverseSubdirectories(
        node: Node = rootNode,
        depth: Int = 0,
        beforeTraversal: (Node, Int) -> Unit = { _,_ -> },
        afterTraversal: (Node, Int) -> Unit = { _,_ -> },
    ) {
        beforeTraversal(node, depth)
        node.children.values.forEach { childNode ->
            traverseSubdirectories(childNode, depth + 1, beforeTraversal, afterTraversal)
        }
        afterTraversal(node, depth)
    }

    fun totalDirectorySpace() = rootNode.totalSize

    private fun addFile(fileSize: Long) {
        currentNode.totalSize += fileSize
    }

    private fun navigate(pathSegment: String) {
        currentNode = when(pathSegment) {
            UP -> currentNode.parent ?: currentNode
            ROOT_DIR -> rootNode
            else -> currentNode.children.getOrPut(pathSegment) {
                Node(currentNode, pathSegment)
            }
        }
    }

    private fun calculateDirectorySizes() = traverseSubdirectories (
        afterTraversal = { node, _ ->
            node.parent?.apply {
                totalSize += node.totalSize
            }
        }
    )

    private fun spacer(numTabs: Int) = SPACE.repeat(numTabs) + DASH

    override fun toString(): String {
        return StringBuilder().apply {
            traverseSubdirectories(
                beforeTraversal = { node, depth ->
                    val spacer = spacer(depth)
                    appendLine("$spacer${node.pathSegment} (dir, size=${node.totalSize.withCommas()})")
                }
            )
        }.toString()
    }
}

object Day7 {
    fun part1() {
        val directory = FileDirectory("input/day_7.txt")

        var totalSum = 0L
        directory.traverseSubdirectories { subdirectory, _ ->
            if(subdirectory.totalSize <= 100_000L) totalSum += subdirectory.totalSize
        }

        println("Total sum of directories at most 100K: $totalSum")
    }

    fun part2() {
        val directory = FileDirectory("input/day_7.txt")

        val spaceToRemove = directory.totalDirectorySpace() - 40_000_000L  // maximum directory space
        var minimumRemoval = Long.MAX_VALUE

        directory.traverseSubdirectories { node, _ ->
            if(node.totalSize in (spaceToRemove until minimumRemoval)) minimumRemoval = node.totalSize
        }
        println("Minimum directory size to remove: ${minimumRemoval.withCommas()}")
    }
}

private fun Long.withCommas() = "%,d".format(this)