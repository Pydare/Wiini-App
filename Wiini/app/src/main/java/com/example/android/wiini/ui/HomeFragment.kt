package com.example.android.wiini.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.wiini.R
import com.example.android.wiini.databinding.HomeFragmentBinding
import com.example.android.wiini.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onViewCreated(), which we
     * do in this Fragment.
     */
    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        //The ViewModelProviders (plural) is deprecated.
        //ViewModelProviders.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
        ViewModelProvider(this, HomeViewModel.Factory(activity.application)).get(HomeViewModel::class.java)

    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: HomeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.home_fragment, container, false
        )
        // set the lifecycleOwner so DataBinding can observe LiveData
        binding.setLifecycleOwner(viewLifecycleOwner)



        binding.playButton.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPlayAudioFragment())
        }


        binding.savedAudiosButton.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSavedAudiosFragment())
        }

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






