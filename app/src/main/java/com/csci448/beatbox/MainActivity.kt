package com.csci448.beatbox

import android.annotation.SuppressLint
import android.app.LauncherActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.csci448.beatbox.databinding.ActivityMainBinding
import com.csci448.beatbox.databinding.ListItemSoundBinding
import kotlin.math.log

private const val logTag = "448.MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var beatBox: BeatBox
    private var scaleFactor = 3.0f
    private lateinit var scaleListener: ScaleGestureDetector
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate Called")

        beatBox = BeatBox(assets)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.recyclerView.layoutManager = GridLayoutManager(baseContext, scaleFactor.toInt())

        scaleListener = ScaleGestureDetector(baseContext, ScaleListener())

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(baseContext,scaleFactor.toInt())
            adapter = SoundAdapter(beatBox.sounds)
        }

        binding.playbackTextView.text = getString(R.string.playback_speed_label, 100)

        val listener = object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d(logTag, "onProgressChanged")
                binding.playbackTextView.text = getString(R.string.playback_speed_label, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        }

        binding.playbackSpeedSeekBar.setOnSeekBarChangeListener(listener)

        binding.recyclerView.setOnTouchListener{ v: View, event: MotionEvent ->
            Log.d(logTag, "on touch listener")
            scaleListener.onTouchEvent(event)
            if(!scaleListener.isInProgress) {onTouchEvent(event)}
            else {
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(logTag, "onDestroy")
        beatBox.release()
    }
    private inner class SoundHolder(private val binding: ListItemSoundBinding) :
            RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = SoundViewModel(beatBox)
        }

        fun bind(sound: Sound) {
            Log.d(logTag, "Binding in SoundHolder")
            binding.apply {
                viewModel?.sound = sound
                Log.d(logTag, "sound sound sound")
                executePendingBindings()
            }
        }
    }

    private inner class SoundAdapter(private val sounds: List<Sound>):
            RecyclerView.Adapter<SoundHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundHolder {
            Log.d(logTag, "Create Binding in SoundHolder")
            val binding = DataBindingUtil.inflate<ListItemSoundBinding>(layoutInflater,R.layout.list_item_sound, parent, false)
            return SoundHolder(binding)
        }

        override fun onBindViewHolder(holder: SoundHolder, position: Int) {
            val sound = sounds[position]
            Log.d(logTag, "Binding in SoundAdapter")
            holder.bind(sound)
        }

        override fun getItemCount() = sounds.size
    }

    private inner class ScaleListener: ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            Log.d(logTag, "Zooming")
            scaleFactor *= (detector?.scaleFactor ?: 1.0f)
            if (scaleFactor < 1.0f) {
                scaleFactor = 1.0f
            } else if (scaleFactor > 8.0f) {
                scaleFactor = 8.0f
            }
            binding.recyclerView.layoutManager = GridLayoutManager(baseContext, scaleFactor.toInt())
            return super.onScale(detector)
        }
    }
}
