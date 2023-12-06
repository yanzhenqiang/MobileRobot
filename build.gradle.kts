import org.jetbrains.kotlin.gradle.dsl.copyFreeCompilerArgsToArgs
initVersions(file("project-versions.json"))
buildscript {
    val kotlin_version = "1.6.21"
    extra.apply {
        set("kotlin_version", kotlin_version)
        set("compose_version", compose_version)
    }
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
        maven("https://maven.aliyun.com/repository/central")
        google { url = uri("https://maven.aliyun.com/repository/google") }
        mavenCentral { url = uri("https://maven.aliyun.com/repository/public") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath(kotlin("gradle-plugin", version = "$kotlin_version"))
        classpath("com.jakewharton:butterknife-gradle-plugin:10.2.3")
        classpath("org.codehaus.groovy:groovy-json:3.0.8")
    }
}
allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
        maven("https://maven.aliyun.com/repository/central")
        google { url = uri("https://maven.aliyun.com/repository/google") }
        mavenCentral { url = uri("https://maven.aliyun.com/repository/public") }
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
