buildscript {
    repositories {
        // Make sure that you have the following two repositories
        google()  // Google's Maven repository
        mavenCentral()  // Maven Central repository
        maven { url = uri("https://jitpack.io") }

    }
    dependencies {
        // Add the dependency for the Google services Gradle plugin
        classpath("com.android.tools.build:gradle:8.5.1")
        classpath ("com.google.gms:google-services:4.3.14")

    }
}


// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}