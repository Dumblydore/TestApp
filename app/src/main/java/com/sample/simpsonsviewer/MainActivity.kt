package com.sample.simpsonsviewer

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.sample.simpsonsviewer.databinding.ActivityMainBinding
import com.sample.simpsonsviewer.ui.character.TabletContainerFragment
import com.sample.simpsonsviewer.ui.character.PhoneContainerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val fragment = if (resources.getBoolean(R.bool.isTablet)) {
            TabletContainerFragment()
        } else {
            PhoneContainerFragment()
        }

        supportFragmentManager.commit {
            replace(R.id.main, fragment)
        }
    }
}