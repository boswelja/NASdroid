package com.nasdroid

import android.app.Application
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProvider
import kotlin.test.BeforeTest
import kotlin.test.Test

class DITest {

    private lateinit var mockSavedStateHandle: SavedStateHandle

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setUp() {
        mockSavedStateHandle = mockk()
        every { mockSavedStateHandle.get<String>(any()) } returns "1"
        every { mockSavedStateHandle.getStateFlow<String?>(any(), any()) } answers {
            MutableStateFlow(args[1] as String?)
        }
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
                withInstance(mockSavedStateHandle)
            }
        }
    }
}
