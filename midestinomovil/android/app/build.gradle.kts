plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.flutter.flutter-gradle-plugin")
}

// Hardcoded to prevent Flutter's auto-migration from replacing with lazy Providers
val appMinSdk = 23
val appTargetSdk = 35
val appCompileSdk = 35

android {
    namespace = "com.midestino.app"
    compileSdk = appCompileSdk

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    defaultConfig {
        applicationId = "com.midestino.app"
        minSdk = appMinSdk
        targetSdk = appTargetSdk
        versionCode = 1
        versionName = "1.0.0"
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

flutter {
    source = "../.."
}
