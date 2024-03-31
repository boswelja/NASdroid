package com.nasdroid

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider
import kotlin.test.BeforeTest
import kotlin.test.Test

class DITest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockProvider.register { clazz -> mockkClass(clazz, relaxed = true) }
    }

    @Test
    fun verifyDependencyInjectionGraph() {
        startKoin {
            modules(NasDroidModule)
            checkModules {
                withInstance<Context>()
                withInstance<Application>()
                withInstance<SavedStateHandle>()
            }
        }
    }
}
