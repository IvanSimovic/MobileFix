import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.simovic.mobilefix.buildlogic"

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
            id = "com.simovic.mobilefix.convention.application"
            implementationClass = "com.simovic.mobilefix.buildlogic.ApplicationConventionPlugin"
        }

        register("featureConvention") {
            id = "com.simovic.mobilefix.convention.feature"
            implementationClass = "com.simovic.mobilefix.buildlogic.FeatureConventionPlugin"
        }

        register("libraryConvention") {
            id = "com.simovic.mobilefix.convention.library"
            implementationClass = "com.simovic.mobilefix.buildlogic.LibraryConventionPlugin"
        }

        register("kotlinConvention") {
            id = "com.simovic.mobilefix.convention.kotlin"
            implementationClass = "com.simovic.mobilefix.buildlogic.KotlinConventionPlugin"
        }

        register("testConvention") {
            id = "com.simovic.mobilefix.convention.test"
            implementationClass = "com.simovic.mobilefix.buildlogic.TestConventionPlugin"
        }

        register("testLibraryConvention") {
            id = "com.simovic.mobilefix.convention.test.library"
            implementationClass = "com.simovic.mobilefix.buildlogic.TestConventionLibraryPlugin"
        }

        register("spotlessConvention") {
            id = "com.simovic.mobilefix.convention.spotless"
            implementationClass = "com.simovic.mobilefix.buildlogic.SpotlessConventionPlugin"
        }

        register("detektConvention") {
            id = "com.simovic.mobilefix.convention.detekt"
            implementationClass = "com.simovic.mobilefix.buildlogic.DetektConventionPlugin"
        }

        register("easyLauncherConvention") {
            id = "com.simovic.mobilefix.convention.easylauncher"
            implementationClass = "com.simovic.mobilefix.buildlogic.EasyLauncherConventionPlugin"
        }

        register("aboutLibrariesConvention") {
            id = "com.simovic.mobilefix.convention.aboutlibraries"
            implementationClass = "com.simovic.mobilefix.buildlogic.AboutLibrariesConventionPlugin"
        }
    }
}
