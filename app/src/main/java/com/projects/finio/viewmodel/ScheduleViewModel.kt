package com.projects.finio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.finio.data.local.entity.Schedule
import com.projects.finio.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    val allSchedules: StateFlow<List<Schedule>> = repository
        .allSchedules
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    fun addSchedule(
        title: String,
        description: String
    ): Result<Unit> {
        viewModelScope.launch {
            val result = repository.insertSchedule(
                Schedule(
                    title = title,
                    description = description,
                    total = 0.00,
                    expireDate = null
                )
            )

            result.onFailure {
                _errorMessage.value = it.message
            }

            result.onSuccess {
                _errorMessage.value = null
            }
        }
        return Result.success(Unit)
    }
}