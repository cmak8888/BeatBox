package com.csci448.beatbox

import android.view.View
import android.widget.SeekBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var mainActivity: MainActivity
    @get:Rule
    var mainActivityTestRule = ActivityTestRule(MainActivity::class.java)


    @Before
    fun setUp() {
        mainActivity = MainActivity()

    }


    @Test
    fun playbackSpeedTextDisplayed() {
        onView( withId(R.id.playback_text_view) ).check(matches( isDisplayed() ))
    }

    @Test
    fun playbackSpeedTextDefault() {
        val defaultString = mainActivityTestRule.activity.resources.getString(R.string.playback_speed_label, 100)
        onView( withId(R.id.playback_text_view) ).check( matches( withText(defaultString) ) )
    }

    @Test
    fun playbackSpeedText150() {
        val stringAt150 =
            mainActivityTestRule.activity.resources.getString(R.string.playback_speed_label, 150)
        onView(withId(R.id.playback_speed_seek_bar)).perform(setProgress(150))
        onView(withId(R.id.playback_text_view)).check(matches(withText(stringAt150)))
    }

    @Test
    fun playbackSpeedText25() {
        val stringAt150 = mainActivityTestRule.activity.resources.getString( R.string.playback_speed_label, 25 )
        onView(withId(R.id.playback_speed_seek_bar)).perform(setProgress(25))
        onView( withId(R.id.playback_text_view) ).check( matches( withText(stringAt150) ) )
    }


    private fun setProgress(progress: Int) : ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set progress on SeekBar"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(SeekBar::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val seekBar = view as SeekBar?
                seekBar?.progress = progress
            }
        }
        }
    }