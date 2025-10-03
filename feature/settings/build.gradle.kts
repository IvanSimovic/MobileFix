plugins {
    id("com.simovic.mobilefix.convention.feature")
}

android {
    namespace = "com.simovic.mobilefix.feature.settings"
}

dependencies {
    implementation(libs.aboutlibraries.compose)
}
