package org.belajar.trydev.footballleagueapi.view


import android.support.test.espresso.ViewInteraction
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

import org.belajar.trydev.footballleagueapi.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import org.belajar.trydev.footballleagueapi.R.id.*
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeActivityTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testRecyclerView(){
        Thread.sleep(1500)
        onView(withId(rv_match))
                .check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(rv_match))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        Thread.sleep(1500)
        onView(withId(rv_match))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
        Thread.sleep(2500)
    }

    @Test
    fun testAddToFavorite(){
        Thread.sleep(1500)
        onView(withId(rv_match))
                .check(matches(isDisplayed()))
        Thread.sleep(1500)
        onView(withId(rv_match))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        Thread.sleep(1500)
        onView(withId(rv_match))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
        Thread.sleep(1500)
        onView(withId(add_to_favorite))
                .check(matches(isDisplayed()))
        onView(withId(add_to_favorite))
                .perform(click())
        Thread.sleep(1000)

        pressBack()

        Thread.sleep(1000)
        onView(withId(bottom_navigation))
                .check(matches(isDisplayed()))
        onView(withId(favorites))
                .perform(click())
        Thread.sleep(1500)
    }
}
