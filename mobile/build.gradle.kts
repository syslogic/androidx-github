// :mobile build.gradle
import androidx.room.gradle.RoomExtension
import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.androidx.room)
}

configure<BasePluginExtension> {
    archivesName = "androidx-github-client"
}

configure<RoomExtension> {
    schemaDirectory("${rootDir}/schema")
}

/** Load API access-token from file `token.properties` */
if (rootProject.file("token.properties").exists()) {
    val fis = FileInputStream(rootProject.file("token.properties"))
    val token = Properties()
    token.load(fis)
    project.ext.set("accessToken", token["accessToken"])
    fis.close()
} else {
    println("*** File `token.properties` is missing; the GitHub API may be rate-limited.")
    println("*** The personal access token needs to be defined with an EditTextPreference.")
    project.ext.set("accessToken", "")
}

configure<ApplicationExtension> {
    namespace = "io.syslogic.github"
    buildToolsVersion = libs.versions.android.build.tools.get()
    compileSdk = Integer.parseInt(libs.versions.android.compile.sdk.get())
    defaultConfig {
        applicationId = "io.syslogic.github"
        minSdk = Integer.parseInt(libs.versions.android.min.sdk.get())
        targetSdk = Integer.parseInt(libs.versions.android.target.sdk.get())
        versionCode = Integer.parseInt(libs.versions.app.version.code.get())
        versionName = libs.versions.app.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["accessToken"] = ""
        testBuildType = "debug"
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets {
        named("androidTest") {
            assets.directories.add("$projectDir/schemas")
        }
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }

    signingConfigs {
        named("debug") {
            val debugKeystore = project.extra.get("debugStoreFile") as File
            if (debugKeystore.exists()) {
                storeFile = debugKeystore
                storePassword = project.extra.get("debugKeystorePass").toString()
                keyAlias = project.extra.get("debugKeyAlias").toString()
                keyPassword = project.extra.get("debugKeyPass").toString()
            } else {
                println("debug key-store missing: ${debugKeystore.absolutePath}")
            }
        }
        create("release") {
            val releaseKeystore = project.extra.get("releaseStoreFile") as File
            if (releaseKeystore.exists()) {
                storeFile = releaseKeystore
                storePassword = project.extra.get("releaseKeystorePass").toString()
                keyAlias = project.extra.get("releaseKeyAlias").toString()
                keyPassword = project.extra.get("releaseKeyPass").toString()
            } else {
                println("release key-store missing: ${releaseKeystore.absolutePath}")
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            val accessToken = project.ext.get("accessToken")
            manifestPlaceholders["accessToken"] = "$accessToken"
            signingConfig = signingConfigs.getByName("debug")
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = false
            isPseudoLocalesEnabled = false
            isShrinkResources = false
            isMinifyEnabled = false
            isJniDebuggable = true
            isDebuggable = true
        }
        release {
            manifestPlaceholders["accessToken"] = ""
            signingConfig = signingConfigs.getByName("release")
            proguardFile("${project.rootDir}/proguard/android.pro")
            proguardFile("${project.rootDir}/proguard/androidx.pro")
            proguardFile("${project.rootDir}/proguard/retrofit.pro")
            proguardFile("${project.rootDir}/proguard/window.pro")
            proguardFile("${project.rootDir}/proguard/mobile.pro")
            proguardFile("${project.rootDir}/proguard/jgit.pro")
            enableAndroidTestCoverage = false
            enableUnitTestCoverage = false
            isPseudoLocalesEnabled = false
            isShrinkResources = true
            isMinifyEnabled = true
            isJniDebuggable = false
            isDebuggable = false
        }
    }

    lint {
        lintConfig = rootProject.file("lint.xml")
        checkAllWarnings = true
        warningsAsErrors = true
        abortOnError = false
        showAll = false
        // disable += "UnknownNullness"
    }
}

dependencies {

    api(project(path = ":androidx-github"))

    // Material Design Components
    implementation(libs.material.design)

    // Annotations
    implementation(libs.androidx.annotation)

    // Flexbox Layout
    implementation(libs.flexbox)

    // https://developer.android.com/jetpack/androidx/
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.cardview)

    // Navigation
    implementation(libs.androidx.navigation.fragment)

    // Fragment
    implementation(libs.androidx.fragment)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)

    annotationProcessor(libs.androidx.room.compiler)
    androidTestAnnotationProcessor(libs.androidx.room.compiler)

    // Retrofit2
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson.converter) {
        exclude(group = "com.google.code.gson", module = "gson")
    }

    // https://mvnrepository.com/artifact/org.eclipse.jgit/org.eclipse.jgit
    implementation(libs.jgit)

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    // api("org.slf4j:slf4j-nop:$slf4j_version")
    api(libs.slf4j)

    // jUnit4
    testImplementation(libs.junit)

    // Required for connected tests.
    // https://mvnrepository.com/artifact/androidx.test/monitor
    debugImplementation(libs.androidx.test.monitor)

    // https://mvnrepository.com/artifact/androidx.test.ext
    androidTestImplementation(libs.androidx.test.junit)

    // https://mvnrepository.com/artifact/androidx.test
    // https://developer.android.com/jetpack/androidx/releases/test
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

    // https://mvnrepository.com/artifact/androidx.test.uiautomator/uiautomator
    androidTestImplementation(libs.androidx.test.uiautomator)

    // Espresso
    androidTestImplementation(libs.bundles.espresso)
    // androidTestImplementation(libs.androidx.test.espresso.core)
    // androidTestImplementation(libs.androidx.test.espresso.web)

    // androidTestImplementation("androidx.test.espresso:espresso-contrib:$androidx_test_espresso_version")
    // androidTestImplementation("androidx.test.espresso:espresso-intents:$androidx_test_espresso_version")
    // androidTestImplementation("androidx.test.espresso:espresso-accessibility:$androidx_test_espresso_version")
    // androidTestImplementation("androidx.test.espresso.idling:idling-concurrent:$espresso_version")

    // The following dependency can be either "implementation" or "androidTestImplementation",
    // depending on whether you want it to appear on your APK's compile classpath or the test APK classpath.
    // androidTestImplementation("androidx.test.espresso:espresso-idling-resource:$androidx_test_espresso_version")

    // androidTestImplementation(libs.androidx.fragment.testing)
    // androidTestImplementation(libs.androidx.navigation.testing)
    // androidTestImplementation(libs.androidx.room.testing)
    // androidTestImplementation(libs.bundles.androidx.testing)
}
