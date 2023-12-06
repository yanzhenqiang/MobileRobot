import java.util.*
import java.io.File

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.jakewharton.butterknife")
    id("kotlin-kapt")
}


val propFile: File = File("xxx/sign.properties")
val properties = Properties()
if (propFile.exists()) {
    propFile.inputStream().reader().use {
        properties.load(it)
    }
}

android {
    buildToolsVersion = versions.buildTool
    compileSdk = versions.compile

    defaultConfig {
        applicationId = "org.autojs.autoxjs"
        minSdk = versions.mini
        targetSdk = versions.target
        versionCode = versions.appVersionCode
        versionName = versions.appVersionName
        buildConfigField("boolean", "isMarket", "false")
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["resourcePackageName"] = applicationId.toString()
                arguments["androidManifestFile"] = "$projectDir/src/main/AndroidManifest.xml"
            }
        }
        ndk {
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a"))
        }
    }
    buildFeatures {
        compose = true
    }
    lint {
        abortOnError = false
        disable.addAll(listOf("MissingTranslation", "ExtraTranslation"))
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-rc01"
        kotlinCompilerVersion = "1.6.20"
    }
    signingConfigs {
        if (propFile.exists()) {
            getByName("release") {
                storeFile = file(properties.getProperty("storeFile"))
                storePassword = properties.getProperty("storePassword")
                keyAlias = properties.getProperty("keyAlias")
                keyPassword = properties.getProperty("keyPassword")
            }
        }
    }
    splits {

        // Configures multiple APKs based on ABI.
        abi {

            // Enables building multiple APKs per ABI.
            isEnable = true

            // By default all ABIs are included, so use reset() and include to specify that we only
            // want APKs for x86 and x86_64.

            // Resets the list of ABIs that Gradle should create APKs for to none.
            reset()

            // Specifies a list of ABIs that Gradle should create APKs for.
            include("armeabi-v7a", "arm64-v8a")

            // Specifies that we do not want to also generate a universal APK that includes all ABIs.
            isUniversalApk = true
        }
    }
    buildTypes {
        named("debug") {
            isShrinkResources = false
            isMinifyEnabled = false
            if (propFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        named("release") {
            isShrinkResources = false
            isMinifyEnabled = false
            if (propFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    flavorDimensions.add("channel")
    productFlavors {
        create("common") {
            versionCode = versions.appVersionCode
            versionName = versions.appVersionName
            buildConfigField("String", "CHANNEL", "\"common\"")
            manifestPlaceholders.putAll(mapOf("appName" to "@string/app_name"))
        }
        create("v6") {
            applicationIdSuffix = ".v6"
            versionCode = versions.devVersionCode
            versionName = versions.devVersionName
            buildConfigField("String", "CHANNEL", "\"v6\"")
            manifestPlaceholders.putAll(mapOf("appName" to "Autox.js v6"))
        }
    }

    sourceSets {
        getByName("main") {
            res.srcDirs("src/main/res", "src/main/res-i18n")
            jniLibs.srcDirs("/libs")
        }
    }

    configurations.all {
        resolutionStrategy.force("com.google.code.findbugs:jsr305:3.0.1")
        exclude(group = "org.jetbrains", module = "annotations-java5")
        exclude(group = "com.github.atlassian.commonmark-java", module = "commonmark")
    }

    packagingOptions {
        //ktor netty implementation("io.ktor:ktor-server-netty:2.0.1")
        resources.pickFirsts.addAll(
            listOf(
                "META-INF/io.netty.versions.properties",
                "META-INF/INDEX.LIST"
            )
        )
    }

}

dependencies {
    implementation(project(":automator"))
    implementation(project(":common"))
    implementation(project(":autojs"))

    val lifecycle_version = "2.5.0-rc01"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    val accompanist_version = "0.24.13-rc"
    implementation("com.google.accompanist:accompanist-permissions:$accompanist_version")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist_version")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("org.chromium.net:cronet-embedded:76.3809.111")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2")
    val AAVersion = "4.5.2"
    annotationProcessor("org.androidannotations:androidannotations:$AAVersion")
    kapt("org.androidannotations:androidannotations:$AAVersion")
    implementation("org.androidannotations:androidannotations-api:$AAVersion")
    implementation("com.jakewharton:butterknife:10.2.1") {
        exclude(group = "com.android.support")
    }
    annotationProcessor("com.jakewharton:butterknife-compiler:10.2.3")
    kapt("com.jakewharton:butterknife-compiler:10.2.3")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.7.0-alpha03")
    implementation("com.afollestad.material-dialogs:core:0.9.2.3") {
        exclude(group = "com.android.support")
    }
    implementation("com.yqritc:recyclerview-flexibledivider:1.4.0")
    implementation("com.wang.avi:library:2.1.3")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.github.bumptech.glide:glide:4.8.0") {
        exclude(group = "com.android.support")
    }
    kapt("com.github.bumptech.glide:compiler:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("net.danlew:android.joda:2.10.14")
    implementation("com.twofortyfouram:android-plugin-client-sdk-for-locale:4.0.3")
    implementation("com.afollestad.material-dialogs:commons:0.9.2.3") {
        exclude(group = "com.android.support")
    }
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.savedstate:savedstate-ktx:1.2.0")
    implementation("androidx.savedstate:savedstate:1.2.0")
    val ktor_version = "2.0.3"
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("com.leinardi.android:speed-dial.compose:1.0.0-alpha03")
    implementation("io.noties.markwon:core:4.6.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0-beta01")
    implementation("io.coil-kt:coil-compose:2.0.0-rc03")
    implementation("ai.picovoice:porcupine-android:3.0.1")
}
