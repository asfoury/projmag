package com.sdp13epfl2021.projmag

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 *  This a custom test runner for the Hilt framework
 *  See Google's [Android Doc](https://developer.android.com/training/dependency-injection/hilt-testing#instrumented-tests)
 */
class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
