plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    buildToolsVersion = versions.buildTool
    compileSdk = versions.compile

    defaultConfig {
        minSdk = versions.mini
        targetSdk = versions.target
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lintOptions.isAbortOnError = false
    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }
    sourceSets {
        named("main") {
            jniLibs.srcDirs("/libs")
        }
    }
}

dependencies {
    api(project(":common"))
    implementation("androidx.core:core:1.7.0")
}
