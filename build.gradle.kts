// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
//    repositories {
//        mavenCentral()
//    }
    dependencies {
//        implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    }
}




plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
//    alias(libs.plugins.ksp) apply false
}