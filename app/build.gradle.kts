import com.simovic.mobilefix.buildlogic.ext.buildConfigFieldFromGradleProperty

plugins {
    id("com.simovic.mobilefix.convention.application")
}

android {
    namespace = "com.simovic.mobilefix.app"

    defaultConfig {
        applicationId = "com.simovic.mobilefix"

        versionCode = 1
        versionName = "0.0.1" // SemVer (Major.Minor.Patch)

        buildConfigFieldFromGradleProperty(project, "apiBaseUrl")
        buildConfigFieldFromGradleProperty(project, "apiToken")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles("proguard-android.txt", "proguard-rules.pro")
        }
    }
}

dependencies {
    // "projects." Syntax utilizes Gradle TYPESAFE_PROJECT_ACCESSORS feature
    implementation(projects.feature.base)
    implementation(projects.feature.album)
    implementation(projects.feature.settings)
    implementation(projects.feature.favourite)
}
