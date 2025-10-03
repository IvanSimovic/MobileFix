package com.simovic.mobilefix.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.simovic.mobilefix.buildlogic.config.JavaBuildConfig
import com.simovic.mobilefix.buildlogic.ext.debugImplementation
import com.simovic.mobilefix.buildlogic.ext.excludeLicenseAndMetaFiles
import com.simovic.mobilefix.buildlogic.ext.implementation
import com.simovic.mobilefix.buildlogic.ext.libs
import com.simovic.mobilefix.buildlogic.ext.versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("detekt.LongMethod")
class ApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("com.google.devtools.ksp")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.simovic.mobilefix.convention.kotlin")
                apply("com.simovic.mobilefix.convention.spotless")
                apply("com.simovic.mobilefix.convention.easylauncher")
                apply("com.simovic.mobilefix.convention.aboutlibraries")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk =
                    versions
                        .compile
                        .sdk
                        .get()
                        .toInt()

                defaultConfig {
                    applicationId = "com.simovic.mobilefix"

                    minSdk =
                        versions
                            .min
                            .sdk
                            .get()
                            .toInt()

                    targetSdk =
                        versions
                            .target
                            .sdk
                            .get()
                            .toInt()

                    versionCode = 1
                    versionName = "1.0"
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    multiDexEnabled = true

                    vectorDrawables {
                        useSupportLibrary = true
                    }
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

                packaging {
                    excludeLicenseAndMetaFiles()
                }

                testOptions {
                    unitTests.isReturnDefaultValues = true
                }
            }

            dependencies {
                implementation(libs.kotlin.reflect)
                implementation(libs.core.ktx)
                implementation(libs.timber)
                implementation(libs.coroutines)
                implementation(libs.material)
                implementation(libs.compose.material)

                // Compose dependencies
                implementation(platform(libs.compose.bom))
                implementation(libs.tooling.preview)
                debugImplementation(libs.compose.ui.tooling)
                debugImplementation(libs.compose.ui.test.manifest)
                implementation(libs.navigation.compose)

                // Koin
                implementation(platform(libs.koin.bom))
                implementation(libs.bundles.koin)

                implementation(libs.bundles.retrofit)
                implementation(libs.viewmodel.ktx)
                implementation(libs.core.splashscreen)
            }
        }
    }
}
