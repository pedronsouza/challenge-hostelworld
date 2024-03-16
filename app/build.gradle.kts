@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = Android.packageName
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = Android.packageName
        minSdk = Android.minSdk
        targetSdk = Android.compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = Android.instrumentationRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = KotlinOptions.jvmTarget
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":features:property_list"))
    implementation(project(":features:property_detail"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":shared"))

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.timber)

    implementation(libs.bundles.android.lifecycle)
    implementation(libs.bundles.koin)
    implementation(libs.bundles.compose)
}