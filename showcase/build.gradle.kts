plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(showcaseLibs.plugins.androidx.compose.compiler)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "com.jsijsling.androidx.initializerfragmentfactory"
    compileSdk = 36
    buildFeatures.compose = true
    buildFeatures.viewBinding = true

    defaultConfig {
        applicationId = "com.jsijsling.androidx.initializerfragmentfactory"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(projects.library)
    implementation(showcaseLibs.androidx.appcompat)
    implementation(platform(showcaseLibs.androidx.compose.bom))
    implementation(showcaseLibs.androidx.compose.foundation)
    implementation(showcaseLibs.androidx.compose.material3.android)
    implementation(showcaseLibs.androidx.fragment.ktx)
    debugImplementation(showcaseLibs.androidx.compose.ui.tooling)
}
