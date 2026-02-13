import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.simovic.meapp.buildlogic"

/*
Configure the buildLogic plugins to target JDK from version catalog
This matches the JDK used to build the project, and is not related to what is running on device.
*/
val javaVersion =
    libs
        .versions
        .java
        .get()

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(javaVersion)
    }

    jvmToolchain(javaVersion.toInt())
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.spotless.gradlePlugin)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.test.logger.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
    implementation(libs.junit5.gradlePlugin)
    implementation(libs.easy.launcher.gradlePlugin)
    implementation(libs.aboutlibraries.gradlePlugin)

    /*
    Expose generated type-safe version catalogs accessors accessible from precompiled script plugins
    e.g. add("implementation", libs.koin)
    https://github.com/gradle/gradle/issues/15383
     */
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("applicationConvention") {
            id = "com.simovic.meapp.convention.application"
            implementationClass = "com.simovic.meapp.buildlogic.ApplicationConventionPlugin"
        }

        register("featureConvention") {
            id = "com.simovic.meapp.convention.feature"
            implementationClass = "com.simovic.meapp.buildlogic.FeatureConventionPlugin"
        }

        register("libraryConvention") {
            id = "com.simovic.meapp.convention.library"
            implementationClass = "com.simovic.meapp.buildlogic.LibraryConventionPlugin"
        }

        register("kotlinConvention") {
            id = "com.simovic.meapp.convention.kotlin"
            implementationClass = "com.simovic.meapp.buildlogic.KotlinConventionPlugin"
        }

        register("testConvention") {
            id = "com.simovic.meapp.convention.test"
            implementationClass = "com.simovic.meapp.buildlogic.TestConventionPlugin"
        }

        register("testLibraryConvention") {
            id = "com.simovic.meapp.convention.test.library"
            implementationClass = "com.simovic.meapp.buildlogic.TestConventionLibraryPlugin"
        }

        register("spotlessConvention") {
            id = "com.simovic.meapp.convention.spotless"
            implementationClass = "com.simovic.meapp.buildlogic.SpotlessConventionPlugin"
        }

        register("detektConvention") {
            id = "com.simovic.meapp.convention.detekt"
            implementationClass = "com.simovic.meapp.buildlogic.DetektConventionPlugin"
        }

        register("easyLauncherConvention") {
            id = "com.simovic.meapp.convention.easylauncher"
            implementationClass = "com.simovic.meapp.buildlogic.EasyLauncherConventionPlugin"
        }

        register("aboutLibrariesConvention") {
            id = "com.simovic.meapp.convention.aboutlibraries"
            implementationClass = "com.simovic.meapp.buildlogic.AboutLibrariesConventionPlugin"
        }
    }
}
