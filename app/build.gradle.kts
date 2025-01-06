plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "2.0.20"
//    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.trajanmarket"
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.example.trajanmarket"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${project.findProperty("BASE_URL")}\""
            )
            buildConfigField(
                "String",
                "API_KEY",
                "\"${project.findProperty("API_KEY")}\""
            )
            buildConfigField(
                "String",
                "PROJECT_ID",
                "\"${project.findProperty("PROJECT_ID")}\""
            )
            buildConfigField(
                "String",
                "DATABASE_ID",
                "\"${project.findProperty("DATABASE_ID")}\""
            )
            // COLLECTIONS
            buildConfigField(
                "String",
                "COLLECTION_USERS",
                "\"${project.findProperty("COLLECTION_USERS")}\""
            )
            buildConfigField(
                "String",
                "COLLECTION_CARTS",
                "\"${project.findProperty("COLLECTION_CARTS")}\""
            )
            buildConfigField(
                "String",
                "COLLECTION_PRODUCTS",
                "\"${project.findProperty("COLLECTION_PRODUCTS")}\""
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${project.findProperty("BASE_URL")}\""
            )
            buildConfigField(
                "String",
                "API_KEY",
                "\"${project.findProperty("API_KEY")}\""
            )
            buildConfigField(
                "String",
                "PROJECT_ID",
                "\"${project.findProperty("PROJECT_ID")}\""
            )
            buildConfigField(
                "String",
                "DATABASE_ID",
                "\"${project.findProperty("DATABASE_ID")}\""
            )
            // COLLECTIONS
            buildConfigField(
                "String",
                "COLLECTION_USERS",
                "\"${project.findProperty("COLLECTION_USERS")}\""
            )
            buildConfigField(
                "String",
                "COLLECTION_CARTS",
                "\"${project.findProperty("COLLECTION_CARTS")}\""
            )
            buildConfigField(
                "String",
                "COLLECTION_PRODUCTS",
                "\"${project.findProperty("COLLECTION_PRODUCTS")}\""
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.material)
    
    // HILT
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    
    // NAVIGATION
    implementation(libs.navigation.compose)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment)
    
    // KTOR
    implementation(libs.ktor.cio)
    implementation(libs.ktor.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    
    // COIL
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    
    // ROOM
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    
    // DATASTORE
    implementation(libs.datastore.preferences)
    implementation(libs.datastore)
    
    implementation(libs.compose.shimmeer)
    
    // APPWRITE
    implementation("io.appwrite:sdk-for-kotlin:5.0.1")
    
    implementation(libs.kotlinx.coroutines.play.services)
    
    implementation(libs.osmdroid.android)
}

hilt {
    enableAggregatingTask = true
}

kapt {
    correctErrorTypes = true
}


