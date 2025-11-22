plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mobdeve.s17.group11.smartspend"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mobdeve.s17.group11.smartspend"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.github.bumptech.glide:glide:5.0.5")
    implementation("io.getstream:photoview:1.0.3")
    testImplementation(libs.junit)
    annotationProcessor("com.github.bumptech.glide:compiler:5.0.5")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}