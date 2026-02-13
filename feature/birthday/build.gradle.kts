plugins {
    id("com.simovic.meapp.convention.feature")
}

android {
    namespace = "com.simovic.meapp.feature.birthday"
}

dependencies {
    implementation(libs.datepicker)
}
