package com.kotlang.util

//version always has to be x.y.z
class VersionString(private val versionString: String): Comparable<VersionString> {
    override operator fun compareTo(other: VersionString): Int {
        val thisParts = this.versionString.split(".")
        val otherParts = other.versionString.split(".")

        for (idx in thisParts.indices) {
            val x = thisParts[idx].toInt()
            val y = otherParts[idx].toInt()
            if (x == y)
                continue

            return x.compareTo(y)
        }

        return 0
    }
}
