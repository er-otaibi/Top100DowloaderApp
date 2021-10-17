package com.example.top10downloaderapp

class FeedEntry {
    var name: String = ""


    override fun toString(): String {
        return """
            name = $name
           """.trimIndent()
    }
}