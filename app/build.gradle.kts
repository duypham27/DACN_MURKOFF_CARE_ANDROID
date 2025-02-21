plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.dacn_murkoff_care_android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dacn_murkoff_care_android"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("androidx.constraintlayout:constraintlayout:2.2.0-rc01")
    //To use constraintlayout in compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-rc01")

    //Auth Phone + Github From Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.0.3"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-messaging:23.1.1")  // Optional if you plan to send notifications via FCM


    //Auth Google Login
    implementation ("com.google.android.gms:play-services-auth:20.4.0")

    //GSON
    implementation ("com.google.code.gson:gson:2.8.6")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Picasso from picasso
    implementation ("com.squareup.picasso:picasso:2.8")

    //CircleImageView from hdodenhof
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Icon badge - is used to set number on the top-right corner of icon
    implementation ("com.google.android.material:material:1.8.0-alpha02")

    //Recycler view decorator
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("it.xabaras.android:recyclerview-swipedecorator:1.4")

    //OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

}