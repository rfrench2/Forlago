plugins {
    id("forlago.android-library-conventions")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.leinardi.forlago.library.android.api"
    resourcePrefix = "android_api_"
    defaultConfig {
        consumerProguardFiles("$projectDir/proguard-android-api-consumer-rules.pro")
    }
}

dependencies {
    api(libs.play.core)
    implementation(libs.androidx.room.runtime)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.serialization)
    kapt(libs.hilt.compiler)
}
