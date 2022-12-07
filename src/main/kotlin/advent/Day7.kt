package advent

import ResourceFetcher

fun main(args: Array<String>) {
    Day7.part2()
}

data class Node(
    val parent: Node?,
    val pathSegment: String,
    val files: MutableMap<String, Long>,
    val children: MutableMap<String, Node>,
    var totalSize: Long = 0L
)

class FileDirectory(filePath: String){
    private companion object{
        const val ROOT_DIR = "/"
        const val UP = ".."
        const val SPACE = "  "
        const val DASH = "- "
        val rootNode = Node(parent = null,"/", mutableMapOf(), mutableMapOf())
    }

    private var currentNode = rootNode

    init {
        ResourceFetcher.forEachLine(filePath) { commandLine ->
            when{
                // command
                commandLine.take(4) == "$ cd" -> navigate(commandLine.slice(5..commandLine.lastIndex))
                // file
                commandLine.first().isDigit() -> {
                    val fileSize = commandLine.takeWhile { it.isDigit() }.toLong()
                    val fileName = commandLine.takeLastWhile { !it.isWhitespace() }
                    addFile(fileName, fileSize)
                }
                else -> {
                    // we don't really care about "$ ls" commands or directories without files in them
                }
            }
        }
        calculateDirectorySizes()
    }

    // assumes children first
    fun traverseSubdirectories(
        node: Node = rootNode,
        depth: Int = 0,
        beforeTraversal: (Node, Int) -> Unit = { _,_ -> Unit },
        afterTraversal: (Node, Int) -> Unit
    ) {
        beforeTraversal(node,depth)
        node.children.values.forEach { childNode ->
            traverseSubdirectories(childNode, depth + 1, beforeTraversal, afterTraversal)
        }
        afterTraversal(node, depth)
    }

    fun totalDirectorySpace() = rootNode.totalSize

    private fun addFile(fileName: String, fileSize: Long) {
        currentNode.files[fileName] = fileSize
    }

    private fun addDirIfNecessary(pathSegment: String) {
        currentNode.children.putIfAbsent(
            pathSegment,
            Node(currentNode, pathSegment, mutableMapOf(), mutableMapOf())
        )
    }

    private fun navigate(pathSegment: String) {
        currentNode = when(pathSegment) {
            UP -> currentNode.parent ?: currentNode
            ROOT_DIR -> rootNode
            else -> {
                addDirIfNecessary(pathSegment)
                currentNode.children.getValue(pathSegment)
            }
        }
    }

    private fun calculateDirectorySizes() = traverseSubdirectories { node, depth ->
        val totalFileSizes = node.files.values.sum()
        node.totalSize += totalFileSizes
        node.parent?.apply {
            totalSize += node.totalSize
        }
    }

    private fun spacer(numTabs: Int) = SPACE.repeat(numTabs) + DASH

    override fun toString(): String {
        return StringBuilder().apply {
            traverseSubdirectories(
                beforeTraversal = { node, depth ->
                    val spacer = spacer(depth)
                    appendLine("$spacer${node.pathSegment} (dir, size=${node.totalSize.withCommas()})")
                },
                afterTraversal = { node, depth ->
                    val spacer = spacer(depth + 1)
                    node.files.forEach { (fileName, fileSize) ->
                        appendLine("$spacer$fileName (file, size=${fileSize.withCommas()})")
                    }
                }

            )
        }.toString()
    }
}

object Day7 {
    fun part1() {
        val directory = FileDirectory("day_7_input.txt")
        println(directory)

        var totalSum = 0L
        directory.traverseSubdirectories { subdirectory, _ ->
            if(subdirectory.totalSize <= 100_000L) totalSum += subdirectory.totalSize
        }

        println("Total sum of directories at most 100K: $totalSum")
    }

    fun part2() {
        val directory = FileDirectory("day_7_input.txt")
        println(directory)
        val minimumUnusedSpace = 30_000_000L
        val currentUnusedSpace = 70_000_000L - directory.totalDirectorySpace()


        val spaceToRemove = minimumUnusedSpace - currentUnusedSpace
        var minimumRemoval = Long.MAX_VALUE

        directory.traverseSubdirectories { node, _ ->
            if(node.totalSize in (spaceToRemove until minimumRemoval)) minimumRemoval = node.totalSize
        }

        println("Minimum directory size to remove: ${minimumRemoval.withCommas()} (desired = ${spaceToRemove.withCommas()})")
    }
}

private fun Long.withCommas() = "%,d".format(this)