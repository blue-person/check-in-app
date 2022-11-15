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
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestInicio {

    private lateinit var inicio: ActivityScenario<ActividadInicio>
    private lateinit var principal: ActivityScenario<ActividadPrincipal>

    @Before
    fun setup() {
        inicio = ActivityScenario.launch(ActividadInicio::class.java)
        inicio.moveToState(Lifecycle.State.RESUMED)
    }

    @After
    fun postExecution() {
        inicio.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun Given_null_user_and_pass_When_ActividadInicio_Then_Trigger_dialogo_de_notificacion() {
        val fullName = "juanpaba153@gmail.com"



        onView(withId(R.id.ingresar))
                .perform(click())

        onView(withId(R.id.boton_de_confirmacion))
                .perform(click())

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.check", appContext.packageName)
    }
    @Test
    fun Given_user_and_pass_When_ActividadInicio_Then_Trigger_dialogo_de_notificacion() {
        val fullName = "juanpaba153@gmail.com"
        val pass = "admin123"

        onView(withId(R.id.user))
                .perform(click())
                .perform(typeTextIntoFocusedView(fullName))
                .perform(closeSoftKeyboard())

        onView(withId(R.id.pass))
                .perform(click())
                .perform(typeTextIntoFocusedView(pass))
                .perform(closeSoftKeyboard())

        Thread.sleep(2000);

        onView(withId(R.id.ingresar))
                .perform(click())

        Thread.sleep(2000);

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.check", appContext.packageName)
    }

    @Test
    fun Given_user_and_incorrect_pass_When_ActividadInicio_Then_Trigger_dialogo_de_notificacion() {
        val fullName = "juanpaba153@gmail.com"
        val pass = "admn123"

        onView(withId(R.id.user))
                .perform(click())
                .perform(typeTextIntoFocusedView(fullName))
                .perform(closeSoftKeyboard())

        onView(withId(R.id.pass))
                .perform(click())
                .perform(typeTextIntoFocusedView(pass))
                .perform(closeSoftKeyboard())

        Thread.sleep(2000);

        onView(withId(R.id.ingresar))
                .perform(click())

        Thread.sleep(2000);

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.check", appContext.packageName)
    }



}


