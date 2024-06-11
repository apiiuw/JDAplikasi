plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.jdaplikasi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jdaplikasi"
        minSdk = 28
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.android.car.ui:car-ui-lib:2.6.0")
    //Bom
    implementation("com.google.firebase:firebase-bom:33.1.0")
    //Google Signin
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    //Database Firebase
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    //Storage Firebase
    implementation("com.google.firebase:firebase-storage:21.0.0")
    //Auth Firebase
    implementation("com.google.firebase:firebase-auth:23.0.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}