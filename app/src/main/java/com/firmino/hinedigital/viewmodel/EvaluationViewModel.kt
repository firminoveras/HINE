package com.firmino.hinedigital.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.firmino.hinedigital.model.entity.Evaluation
import com.firmino.hinedigital.model.repository.EvaluationRepository
import kotlinx.coroutines.launch

class EvaluationViewModel(private val repository: EvaluationRepository) : ViewModel() {

    private val _allEvaluations: LiveData<List<Evaluation>> = repository.allEvaluations.asLiveData()

    val allEvaluations: LiveData<List<Evaluation>>
        get() =
            _allEvaluations

    fun insert(evaluation: Evaluation) = viewModelScope.launch {
        repository.insert(evaluation)
    }

    fun update(evaluation: Evaluation) = viewModelScope.launch {
        repository.update(evaluation)
    }

    fun delete(id: Int) = viewModelScope.launch {
        repository.delete(id)
    }
}