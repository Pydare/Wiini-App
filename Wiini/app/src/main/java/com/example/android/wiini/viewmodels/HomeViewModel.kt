package com.example.android.wiini.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.wiini.database.AudioDao
import com.example.android.wiini.database.getDatabase
import com.example.android.wiini.repository.AudiosRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.sql.CommonDataSource


/**
 * HomeViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * This is the job for all coroutines started by this viewMiodel.
     *
     * Cancelling this job will cancel all coroutines started by thus viewmodel
     *
     * It is destroyed when the view model is destroyed
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by HomeViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by calling viewModelJob.
     * cancel(
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)

    private val audiosRepository = AudiosRepository(database)

    init {
        viewModelScope.launch {
            audiosRepository.refreshAudios()
        }
    }

    val playlist = audiosRepository.audios
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    class HomeViewModelFactory(
            private val dataSource: AudioDao,
            private val application: Application
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}