package com.sample.simpsonsviewer.ui.character

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sample.simpsonsviewer.R
import com.sample.simpsonsviewer.databinding.FragmentTabletContainerBinding
import com.sample.simpsonsviewer.ui.collectOnStart
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class TabletContainerFragment : Fragment(R.layout.fragment_tablet_container) {
    private val viewModel: CharacterViewModel by viewModels({ requireActivity() })
    private var binding: FragmentTabletContainerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.clearSelectedCharacter()
        }

        viewModel.state.map { it.selectedCharacter }
            .distinctUntilChanged()
            .collectOnStart { character ->
                val hasCharacter = character != null
                callback.isEnabled = hasCharacter

                binding?.toolbar?.title = character?.name ?: getString(R.string.app_name)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentTabletContainerBinding.bind(view)
    }
}