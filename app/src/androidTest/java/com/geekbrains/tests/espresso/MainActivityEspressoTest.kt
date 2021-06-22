package com.geekbrains.tests.espresso

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.geekbrains.tests.*
import com.geekbrains.tests.NUMBER_OF_RESULTS_REAL
import com.geekbrains.tests.STRING_SEARCH_QUERY
import com.geekbrains.tests.TEST_NUMBER
import com.geekbrains.tests.view.search.MainActivity
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityEspressoTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun activitySearch_IsWorking() {
        onView(withId(R.id.searchEditText)).perform(click())
        onView(withId(R.id.searchEditText))
            .perform(replaceText(STRING_SEARCH_QUERY), closeSoftKeyboard())
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        if (BuildConfig.TYPE == MainActivity.FAKE) {
            onView(withId(R.id.totalCountTextView)).check(matches(withText(TEST_NUMBER)))
        } else {
            onView(isRoot()).perform(delay())
            onView(withId(R.id.totalCountTextView)).check(matches(withText(NUMBER_OF_RESULTS_REAL)))
        }
    }

    private fun delay(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = isRoot()
            override fun getDescription(): String = "wait for ${TIMEOUT_SEARCH_ON_SERVER / 1000} seconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(TIMEOUT_SEARCH_ON_SERVER)
            }
        }
    }

    @After
    fun close() {
        scenario.close()
    }
}
