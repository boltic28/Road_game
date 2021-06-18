package com.boltic28.game1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import com.boltic28.game1.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var game: PlayWindow
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        setContentView(binding.root)

        with(binding) {
            game = gameView
            accelerate.setOnClickListener { viewModel.accelerate() }
            decelerate.setOnClickListener { viewModel.decelerate() }
            turnToLeft.setOnClickListener { viewModel.turnLeft() }
            turnToRight.setOnClickListener { viewModel.turnRight() }
        }

        CoroutineScope(Dispatchers.Main).launch {
            game.doOnLayout { viewModel.initData(it.width.toFloat(), it.height.toFloat()) }
            while (true) {
                delay(PAUSE_BETWEEN_DRAWING)
                binding.speedometer.text =
                    String.format("%s km/h", game.updateState(viewModel.nextFrame()).toString())
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(CAR_APPEARANCE_TIME)
                viewModel.increaseTraffic()
            }
        }
    }
}