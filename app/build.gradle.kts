@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-kapt")
    alias(libs.plugins.navigationSafeArgs)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "com.sample.simpsonsviewer"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.sample.simpsonsviewer"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures {
            buildConfig = true
            viewBinding = true
        }
    }

    flavorDimensions += "variant"

    productFlavors {
        create("simpsons") {
            applicationId = "com.sample.simpsonsviewer"
            buildConfigField("String", "API_URL", "\"https://api.duckduckgo.com/?q=simpsons+characters&format=json\"")
        }
        create("wire") {
            applicationId = "com.sample.wireviewer"
            buildConfigField("String", "API_URL", "\"https://api.duckduckgo.com/?q=the+wire+characters&format=json\"")
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xcontext-receivers"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.material)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.room.core.ktx)
    kapt(libs.room.compiler)

    implementation(libs.moshi.core)
    kapt(libs.moshi.codegen)


    implementation(libs.coil)
    implementation(libs.okhttp.core)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}