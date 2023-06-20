package com.sample.simpsonsviewer.ui.character.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.sample.simpsonsviewer.R
import com.sample.simpsonsviewer.databinding.FragmentCharacterDetailBinding
import com.sample.simpsonsviewer.ui.character.CharacterViewModel
import com.sample.simpsonsviewer.ui.collectOnStart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {

    private val viewModel: CharacterViewModel by viewModels({ requireActivity() })
    private lateinit var binding: FragmentCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.mapNotNull { it.selectedCharacter?.icon }.collectOnStart {
            binding.image.load(it) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
            }
        }
        viewModel.state.map { it.selectedCharacter }.collectOnStart { character ->
            binding.description.text = when {
                character == null -> getString(R.string.no_character)
                character.description.isEmpty() -> getString(R.string.empty_description)
                else -> character.description
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCharacterDetailBinding.bind(view)
    }
}