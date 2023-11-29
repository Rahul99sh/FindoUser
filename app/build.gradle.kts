plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.users.findo"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.users.findo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    ndkVersion = "25.2.9519653"
    externalNativeBuild {
        cmake {
            path("CMakeLists.txt")
        }
    }
}

dependencies {
    // Fix Duplicate class
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-firestore:24.9.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.airbnb.android:lottie:6.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    //shimmer loading effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    //bottom navBar
    implementation ("com.ismaeldivita.chipnavigation:chip-navigation-bar:1.3.4")
}