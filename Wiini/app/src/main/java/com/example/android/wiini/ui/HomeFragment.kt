package com.example.android.wiini.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.wiini.R
import com.example.android.wiini.database.AudiosDatabase
import com.example.android.wiini.database.getDatabase
import com.example.android.wiini.databinding.HomeFragmentBinding
import com.example.android.wiini.viewmodels.HomeViewModel

class
HomeFragment : Fragment() {

    override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
            ): View? {

        val binding: HomeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.home_fragment, container, false
        )
        // set the lifecycleOwner so DataBinding can observe LiveData
        binding.setLifecycleOwner(viewLifecycleOwner)

        // Creating an application for resources
        val application = requireNotNull(this.activity).application
        // getting a reference to the database dao
        val dataSource = getDatabase(application).audioDao
        // creating the view model factory
        val viewModelFactory = HomeViewModel.HomeViewModelFactory(dataSource, application)
        //  finally creating the view model
        val homeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        // setting the homeViewModel variable in the xml file for data binding
        binding.homeViewModel = homeViewModel


        // TODO: change the onClick logic to be used with data binding (unfortunately can't be done for navigation)

        binding.playButton.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPlayAudioFragment())
        }


        binding.savedAudiosButton.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSavedAudiosFragment())
        }

        // set the title of the fragment

        // informs android that this fragment has an overflow menu option
        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * abstract function called to inflate menu option that belongs to the fragment
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }

    /**
     * abstract function called when a menu item is selected
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
                item!!, view!!.findNavController()
        ) || super.onOptionsItemSelected(item)
    }


}






