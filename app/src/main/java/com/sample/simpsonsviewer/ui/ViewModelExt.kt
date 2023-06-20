package com.sample.simpsonsviewer.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <reified T : ViewModel> Fragment.assistedViewModel(
    crossinline owner: () -> ViewModelStoreOwner = { this },
    crossinline viewModelProducer: () -> T
) = viewModels<T>(ownerProducer = { owner() }) {
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return viewModelProducer() as T
        }

        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return viewModelProducer() as T
        }
    }
}

context(Fragment)
fun <T> Flow<T>.collectOnStart(action: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@collectOnStart.collect(action)
        }
    }
}