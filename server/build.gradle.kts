plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.ktorPlugin)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    
    sourceSets {
        jvmMain.dependencies {
            implementation(projects.shared)
            implementation(libs.ktor.server.core)
            implementation(libs.ktor.server.netty)
            implementation(libs.ktor.server.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.server.cors)
            implementation(libs.ktor.server.resources)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.logback)
        }
        
        jvmTest.dependencies {
            implementation(libs.ktor.server.tests)
            implementation(libs.kotlin.test.junit)
        }
    }
}

application {
    mainClass.set("com.prettyteeth.MainKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("PrettyTeeth-server.jar")
    }
}
