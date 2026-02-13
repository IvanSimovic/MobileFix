import com.simovic.meapp.buildlogic.ext.buildConfigFieldFromGradleProperty

plugins {
    id("com.simovic.meapp.convention.application")
}

android {
    namespace = "com.simovic.meapp.app"

    defaultConfig {
        applicationId = "com.simovic.meapp"

        versionCode = 1
        versionName = "0.0.1" // SemVer (Major.Minor.Patch)

        buildConfigFieldFromGradleProperty(project, "apiBaseUrl")
        buildConfigFieldFromGradleProperty(project, "apiToken")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    // "projects." Syntax utilizes Gradle TYPESAFE_PROJECT_ACCESSORS feature
    implementation(projects.feature.base)
    implementation(projects.feature.album)
    implementation(projects.feature.livefeed)
    implementation(projects.feature.birthday)
}
