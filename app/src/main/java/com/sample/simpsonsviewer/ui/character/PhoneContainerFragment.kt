package com.sample.simpsonsviewer.ui.character

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.sample.simpsonsviewer.R
import com.sample.simpsonsviewer.databinding.FragmentPhoneContainerBinding
import com.sample.simpsonsviewer.ui.character.list.CharacterListFragmentDirections
import com.sample.simpsonsviewer.ui.collectOnStart
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class PhoneContainerFragment : Fragment(R.layout.fragment_phone_container) {

    private val viewModel: CharacterViewModel by viewModels({ requireActivity() })
    private var binding: FragmentPhoneContainerBinding? = null

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

                val navController = requireActivity().findNavController(R.id.container)
                if (!hasCharacter && navController.currentDestination?.id != R.id.characterListFragment) {
                    binding?.toolbar?.navigationIcon = null
                    navController.popBackStack()
                } else if (hasCharacter && navController.currentDestination?.id != R.id.characterDetailFragment) {
                    binding?.toolbar?.setNavigationIcon(R.drawable.ic_back)
                    navController.navigate(CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment())
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPhoneContainerBinding.bind(view).apply {
            toolbar.setNavigationOnClickListener { viewModel.clearSelectedCharacter() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}