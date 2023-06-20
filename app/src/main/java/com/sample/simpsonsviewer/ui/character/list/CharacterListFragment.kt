package com.sample.simpsonsviewer.ui.character.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sample.simpsonsviewer.R
import com.sample.simpsonsviewer.databinding.FragmentListBinding
import com.sample.simpsonsviewer.model.character.CharacterEntity
import com.sample.simpsonsviewer.ui.UIState
import com.sample.simpsonsviewer.ui.character.CharacterViewModel
import com.sample.simpsonsviewer.ui.collectOnStart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterListFragment : Fragment(R.layout.fragment_list) {

    private val viewModel: CharacterViewModel by viewModels({ requireActivity() })
    private val adapter = CharacterListAdapter(::selectCharacter)
    private var binding: FragmentListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.collectOnStart { state ->
            adapter.submitList(state.characters)
            if (state.uiState == UIState.Loading) {
                binding?.progressIndicator?.show()
            } else {
                binding?.progressIndicator?.hide()
            }
            binding?.errorText?.isVisible = state.uiState == UIState.Error
            binding?.input?.apply {
                setText(state.query)
                setSelection(state.query.length)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentListBinding.bind(view)

        binding?.list?.adapter = adapter
        binding?.input?.doAfterTextChanged { viewModel.updateQuery(it?.toString().orEmpty()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun selectCharacter(characterEntity: CharacterEntity) {
        viewModel.selectCharacter(characterEntity)
    }
}