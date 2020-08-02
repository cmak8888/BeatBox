package com.csci448.beatbox

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

class SoundViewModelTest {

    private lateinit var sound: Sound
    private lateinit var subject: SoundViewModel
    private lateinit var beatBox: BeatBox

    @Before
    fun setUp() {
        sound = Sound("AssetPath")
        beatBox = mock(BeatBox::class.java)
        subject = SoundViewModel(beatBox)
        subject.sound = sound
    }
    @Test
    fun exposesSoundNameAsTitle() {
        assertThat(subject.title, `is` (sound.name))
    }

    @Test
    fun callsBeatBoxPlayOnButtonClicked() {
        subject.onButtonClicked()
        verify(beatBox).play(sound)
    }
}