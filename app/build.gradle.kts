plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.rickandmortyapi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.rickandmortyapi"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        jvmTarget = "1.8"
    }
}

dependencies {

    dependencies {
        implementation ("com.jakewharton.timber:timber:4.7.1")
        implementation ("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
        implementation ("androidx.recyclerview:recyclerview:1.2.1")
        implementation ("androidx.appcompat:appcompat:1.3.1")
        implementation ("com.squareup.retrofit2:retrofit:2.9.0")
        implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
        implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
        implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

        androidTestImplementation ("androidx.test:runner:1.5.2")
        androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation ("androidx.test.ext:junit:1.1.5")
        androidTestImplementation ("androidx.test:rules:1.3.0")
        androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

        testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
        testImplementation ("junit:junit:4.13.2")
        testImplementation ("org.mockito:mockito-core:4.5.1")
        testImplementation ("androidx.arch.core:core-testing:2.1.0")

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
    }
}