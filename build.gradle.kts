// root build.gradle.kts
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.maven.publish) apply false
}

/* JitPack: use tag as versionName. */
if (System.getenv("JITPACK") != null) {
    project.ext.set("version_name", System.getenv("VERSION"))
}

/** Build Configurations */
project.ext.set("archiveBuildTypes", arrayOf("debug", "release"))

/** Modules */
allprojects {

    // Keystore Settings, loaded from keystore.properties
    val keystoreConfig: File = rootProject.file("keystore.properties")
    if (keystoreConfig.exists()) {
        val keystore = Properties()
        val fis = FileInputStream(keystoreConfig)
        keystore.load(fis)
        project.ext.set("debugStoreFile",      System.getProperty("user.home") + "/.android/debug.keystore")
        project.ext.set("debugKeystorePass",   keystore["debugKeystorePass"])
        project.ext.set("debugKeyAlias",       keystore["debugKeyAlias"])
        project.ext.set("debugKeyPass",        keystore["debugKeyPass"])
        project.ext.set("releaseStoreFile",    System.getProperty("user.home") + "/.android/release.keystore")
        project.ext.set("releaseKeystorePass", keystore["releaseKeystorePass"])
        project.ext.set("releaseKeyAlias",     keystore["releaseKeyAlias"])
        project.ext.set("releaseKeyPass",      keystore["releaseKeyPass"])
        fis.close()
    } else {
        println("file missing: $keystoreConfig")
    }

    /** Runtime JAR files in the classpath should have the same version. */
    configurations.configureEach {
        val list: List<String> = listOf("kotlin-stdlib", "kotlin-stdlib-jdk8", "kotlin-stdlib-common")
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                if (list.contains(requested.name)) {
                    useVersion(libs.versions.kotlin.get())
                }
            }
        }
    }

    /** When projects were evaluated */
    gradle.projectsEvaluated {
        tasks.withType<JavaCompile>().configureEach {
            options.compilerArgs.addAll(
                listOf("-Xlint:unchecked", "-Xlint:deprecation")
            )
        }
    }

    project.ext.set("group_id", "io.syslogic")        // group
    project.ext.set("github_handle", "syslogic")      // owner
    project.ext.set("artifact_id", "androidx-github") // repository
    project.ext.set("plugin_name", "GitHub API Android Library")
    project.ext.set("plugin_desc", "Retrofit2 Client & Databindings")
}

// rootProject > clean
tasks.withType<Delete>().configureEach {
    delete(rootProject.fileTree("build"))
    delete(project.fileTree("build"))
}
