package com.firmino.hinedigital.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firmino.hinedigital.model.repository.EvaluationRepository
import com.firmino.hinedigital.viewmodel.EvaluationViewModel

class EvaluationModelViewFactory(private val repository: EvaluationRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EvaluationViewModel::class.java)){
            @Suppress("UNCHECKED_CAST") return EvaluationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}