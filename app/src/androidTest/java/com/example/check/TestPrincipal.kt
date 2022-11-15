package com.example.check

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.selects.select
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestPrincipal {
    private lateinit var principal: ActivityScenario<ActividadPrincipal>

    @Before
    fun setup() {
        principal = ActivityScenario.launch(ActividadPrincipal::class.java)
        principal.moveToState(Lifecycle.State.RESUMED)

    }

    @After
    fun postExecution() {
        principal.moveToState(Lifecycle.State.DESTROYED)
    }


    @Test
    fun Given_index_0_When_ActividadInicio_Then_index_1() {

        onView(withId(R.id.index1))
                .perform(click())

        Thread.sleep(4000);

        onView(withId(R.id.index2))
                .perform(click())


        principal.moveToState(Lifecycle.State.DESTROYED)

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.check", appContext.packageName)
    }


}


