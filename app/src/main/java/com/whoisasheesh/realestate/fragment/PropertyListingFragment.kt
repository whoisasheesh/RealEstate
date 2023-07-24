package com.whoisasheesh.realestate.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.whoisasheesh.realestate.R
import com.whoisasheesh.realestate.adaptar.PropertyAdaptar
import com.whoisasheesh.realestate.data.Property
import com.whoisasheesh.realestate.databinding.FragmentPropertyListingBinding
import com.whoisasheesh.realestate.listeners.RecyclerViewItemClickListener
import com.whoisasheesh.realestate.listeners.ScreenChangeListener
import com.whoisasheesh.realestate.viewmodel.PropertyViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PropertyListingFragment : Fragment() {
    private var binding: FragmentPropertyListingBinding? = null
    private lateinit var propertyViewModel: PropertyViewModel
    private var host: ScreenChangeListener? = null
    private var propertyAdapter: PropertyAdaptar? = null
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private val compositeDisposable = CompositeDisposable()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyListingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("PrivateResource")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeToRefresh = view.findViewById(R.id.swipe_to_refresh)

        swipeToRefresh.setOnRefreshListener {
            propertyViewModel.fetchDataFromAPI()
            propertyViewModel.isLoading().observe(viewLifecycleOwner) { isLoading ->
                binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (!isLoading) {
                    swipeToRefresh.isRefreshing = false
                }
            }

        }

        host?.setupCustomToolbarWithoutNavigation(
            resources.getString(R.string.property_listings),
            0,
            isNavigationIconEnabled = false
        )
        propertyViewModel = ViewModelProvider(this)[PropertyViewModel::class.java]

        propertyViewModel.isLoading().observe(viewLifecycleOwner) { isLoading ->
            binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding?.rvPropertyLists?.visibility = if (isLoading) View.GONE else View.VISIBLE
            swipeToRefresh.isRefreshing = false
        }

        propertyViewModel.listener?.observe(viewLifecycleOwner) {
            initRecyclerView()
        }

        propertyViewModel.properties

    }

    private fun initRecyclerView() {
        binding?.rvPropertyLists?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvPropertyLists?.isNestedScrollingEnabled = false
        propertyAdapter = PropertyAdaptar(propertyViewModel.value)
        propertyAdapter?.setClickListener(object : RecyclerViewItemClickListener {
            override fun onRecyclerViewItemClicked(position: Int, view: View?, `object`: Any?) {
                val propertyDetailsFragment =
                    PropertyDetailsFragment.newInstance(`object` as Property)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, propertyDetailsFragment)
                    .addToBackStack("PropertyListingFragment")
                    .commit()
            }

        })
        binding?.rvPropertyLists?.adapter = propertyAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        host = context as ScreenChangeListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setTitle(R.string.exit_application)
        alertDialogBuilder.setMessage(R.string.are_you_sure)
        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            // Clear cache and exit the application
            propertyViewModel.clearPropertiesCache()
            requireActivity().finish()
        }
        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }
}


