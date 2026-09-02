package de.vegnpunk.mentalmatics

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform