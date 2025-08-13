package com.prettyteeth.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
