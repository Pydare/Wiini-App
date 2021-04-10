package com.example.android.wiini.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.android.wiini.R
import com.example.android.wiini.databinding.HomeFragmentBinding
import com.example.android.wiini.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: HomeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.home_fragment, container, false
        )

        binding.playButton.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_playAudioFragment)
        }


        binding.savedAudiosButton.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_savedAudiosFragment)
        }

        return binding.root
    }


}