plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

kotlin {
    explicitApi()
    jvmToolchain(21)
}

android {
    namespace = "com.jsijsling.androidx.fragment"
    compileSdk = 36
    defaultConfig.minSdk = 21
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(libs.androidx.fragment)
    testImplementation(libs.junit)
}
