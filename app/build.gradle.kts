plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("ru.practicum.android.diploma.plugins.developproperties")
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "API_ACCESS_TOKEN", value = "\"${developProperties.apiAccessToken}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)

    // UI layer libraries
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)

    // region Unit tests
    testImplementation(libs.unitTests.junit)
    // endregion

    // region UI tests
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
    // endregion

    // region Extra libs
    implementation(libs.androidx.activity)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.benchmark.macro)

    // Подключаем библиотеку Glide
    implementation(libs.glide)
    // Подключаем Gson и Retrofit
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    // Подключаем Koin
    implementation(libs.koin.android)
    implementation(libs.adapterdelegates4.kotlin.dsl.viewbinding)
    implementation(libs.koin.androidx.compose)
    // Подключаем Jetpack Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.compose)
    // Подключаем корутины
    implementation(libs.kotlinx.coroutines.android)
    // Подключаем Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    // Подключаем Jetpack Compose
    val composeBom = platform("androidx.compose:compose-bom:2025.08.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Подключаем Peko
    implementation(libs.peko)
    implementation(libs.androidx.coordinatorlayout)
    // Подключаем Material Design3
    implementation(libs.androidx.material)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.adaptive)
    implementation(libs.androidx.foundation)
    // Подключаем Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    // endregion

}
