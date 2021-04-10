package com.example.android.wiini.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.wiini.R
import com.example.android.wiini.viewmodels.PlayAudioViewModel

class PlayAudioFragment : Fragment() {

    private lateinit var viewModel: PlayAudioViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.play_audio_fragment, container, false)
    }

}