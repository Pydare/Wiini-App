package com.example.android.wiini.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.wiini.R
import com.example.android.wiini.viewmodels.SavedAudiosViewModel

class SavedAudiosFragment : Fragment() {

    private lateinit var viewModel: SavedAudiosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_audios_fragment, container, false)
    }


}