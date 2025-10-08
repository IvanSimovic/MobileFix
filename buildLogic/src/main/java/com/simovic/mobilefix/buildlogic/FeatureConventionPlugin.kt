package com.simovic.mobilefix.buildlogic

import com.android.build.api.dsl.LibraryExtension
import com.simovic.mobilefix.buildlogic.config.JavaBuildConfig
import com.simovic.mobilefix.buildlogic.ext.debugImplementation
import com.simovic.mobilefix.buildlogic.ext.excludeLicenseAndMetaFiles
import com.simovic.mobilefix.buildlogic.ext.implementation
import com.simovic.mobilefix.buildlogic.ext.ksp
import com.simovic.mobilefix.buildlogic.ext.libs
import com.simovic.mobilefix.buildlogic.ext.testImplementation
import com.simovic.mobilefix.buildlogic.ext.testRuntimeOnly
import com.simovic.mobilefix.buildlogic.ext.versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("detekt.LongMethod")
class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("com.simovic.mobilefix.convention.kotlin")
                apply("com.simovic.mobilefix.convention.test")
                apply("com.simovic.mobilefix.convention.spotless")
                apply("com.google.devtools.ksp")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                compileSdk =
                    versions
                        .compile
                        .sdk
                        .get()
                        .toInt()

                defaultConfig {
                    minSdk =
                        versions
                            .min
                            .sdk
                            .get()
                            .toInt()

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildFeatures {
                    viewBinding = true
                    buildConfig = true
                    compose = true
                }

                compileOptions {
                    sourceCompatibility = JavaBuildConfig.JAVA_VERSION
                    targetCompatibility = JavaBuildConfig.JAVA_VERSION
                }

                testOptions {
                    unitTests.isReturnDefaultValues = true
                }

                packaging {
                    excludeLicenseAndMetaFiles()
                }
            }

            dependencies {
                // Add feature:base dependency only for non-base feature modules
                if (project.path != ":feature:base") {
                    implementation(project(":feature:base"))
                }

                implementation(libs.kotlin.reflect)
                implementation(libs.core.ktx)
                implementation(libs.timber)
                implementation(libs.coroutines)
                implementation(libs.material)
                implementation(libs.compose.material)

                // Compose dependencies
                implementation(platform(libs.compose.bom))
                implementation(libs.bundles.compose)
                debugImplementation(libs.compose.ui.tooling)
                debugImplementation(libs.compose.ui.test.manifest)

                // Koin
                implementation(platform(libs.koin.bom))
                implementation(libs.bundles.koin)

                implementation(libs.bundles.retrofit)
                implementation(libs.viewmodel.ktx)

                // Room
                implementation(libs.bundles.room)
                ksp(libs.room.compiler)

                // Test dependencies
                testImplementation(project(":library:testUtils"))
                testImplementation(libs.bundles.test)
                testRuntimeOnly(libs.junit.jupiter.engine)
            }
        }
    }
}
