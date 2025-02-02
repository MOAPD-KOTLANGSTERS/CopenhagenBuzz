package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}