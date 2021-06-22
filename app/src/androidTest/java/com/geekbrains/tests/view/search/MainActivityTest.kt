package com.geekbrains.tests.view.search

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.R
import com.geekbrains.tests.TEST_NUMBER_OF_RESULTS_PLUS_1
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun mainActivityTest() {
        val toDetailsActivityButton = onView(withId(R.id.toDetailsActivityButton))
        toDetailsActivityButton.perform(click())

        val incrementButton = onView(withId(R.id.incrementButton))
        incrementButton.perform(click())

        val totalCountTextView = onView(withId(R.id.totalCountTextView))

        totalCountTextView.check(matches(withText(TEST_NUMBER_OF_RESULTS_PLUS_1)))
        incrementButton.check(matches(isDisplayed()))
    }

    @After
    fun close() {
        scenario.close()
    }
}
