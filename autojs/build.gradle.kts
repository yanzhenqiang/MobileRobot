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

    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lintOptions.isAbortOnError = false
    sourceSets {
        named("main") {
            res.srcDirs("src/main/res","src/main/res-i18n")
        }
    }
}

dependencies {
    implementation("androidx.preference:preference-ktx:1.2.0")
    api("org.greenrobot:eventbus:3.3.1")
    api("net.lingala.zip4j:zip4j:1.3.2")
    api("com.afollestad.material-dialogs:core:0.9.2.3"){
        exclude(group = "com.android.support")
    }
    api("com.google.android.material:material:1.7.0-beta01")
    api("com.makeramen:roundedimageview:2.3.0")
    api("com.squareup.okhttp3:okhttp:4.10.0")
    api("org.jdeferred:jdeferred-android-aar:1.2.6")
    api("com.google.code.gson:gson:2.9.1")
    api(group = "de.mindpipe.android", name = "android-logging-log4j", version = "1.0.3")
    api(group = "log4j", name = "log4j", version = "1.2.17")
    api(project(path = ":common"))
    api(project(path = ":automator"))
    api(fileTree("../app/libs"){include("dx.jar", "rhino-1.7.14-jdk7.jar")})
    api("com.google.mlkit:text-recognition:16.0.0-beta5")
    api("com.google.mlkit:text-recognition-chinese:16.0.0-beta5")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
}

