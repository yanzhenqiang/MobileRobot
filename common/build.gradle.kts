//import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.jakewharton.butterknife")
    id("kotlin-kapt")
}

android {

    buildToolsVersion = versions.buildTool
    compileSdk = versions.compile

    defaultConfig {
        minSdk = versions.mini
        targetSdk = versions.target
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }
    lint.abortOnError = false

    sourceSets {
        getByName("main") {
            res.srcDirs("src/main/res","src/main/res-i18n")
        }
    }
}

dependencies {
    api("androidx.annotation:annotation:1.4.0")
    api(kotlin("reflect", version = "1.7.10"))
    api("com.jrummyapps:colorpicker:2.1.5")

    implementation("androidx.activity:activity-ktx:1.5.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("com.google.android.material:material:1.1.0-alpha01")
    implementation("com.github.ozodrukh:CircularReveal:2.0.1@aar")
}
