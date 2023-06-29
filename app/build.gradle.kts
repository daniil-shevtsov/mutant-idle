plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Versions.androidCompileSdk

    defaultConfig {
        applicationId = "com.daniil.shevtsov.idle"
        minSdk = Versions.androidMinSdk
        targetSdk = Versions.androidTargetSdk

        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    lint {
        abortOnError = false
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.time",
            "-Xopt-in=kotlin.Experimental",
            "-Xopt-in=kotlin.time.ExperimentalTime",
            "-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
            "-Xopt-in=com.google.accompanist.pager.ExperimentalPagerApi",
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    lint {
        abortOnError = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
    namespace = "com.daniil.shevtsov.idle"

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}

dependencies {
    with(Deps.Android) {
        implementation(material)
    }

    with(Deps.Accompanist) {
        implementation(insets)
        implementation(pager)
        implementation(systemUiController)
    }

    with(Deps.Dagger) {
        implementation(dagger)
        kapt(daggerCompiler)
    }

    with(Deps.Logging) {
        implementation(timber)
    }

    with(Deps.NavigationComponent) {
        implementation(uiKtx)
        implementation(fragmentKtx)
    }

    with(Deps.Compose) {
        implementation(ui)
        implementation(uiGraphics)
        debugImplementation(uiTooling)
        implementation(uiToolingPreview)
        implementation(foundationLayout)
        implementation(materialExtended)
        implementation(material)
        implementation(navigation)
    }

    with(Deps.Koin) {
        implementation(core)
        implementation(android)
        implementation(compose)
    }

    with(Deps.KorLibs) {
        implementation(kBigNumAndroid)
    }

    with(Deps.ViewBinding) {
        implementation(delegate)
    }

    with(Deps.UnitTest) {
        testImplementation(assertk)
        testImplementation(coroutinesTest)
        testImplementation(jupiter)
        testImplementation(mockk)
        testImplementation(mockkAgent)
        testImplementation(turbine)
    }
}
