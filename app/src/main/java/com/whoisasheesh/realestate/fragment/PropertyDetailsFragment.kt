package com.whoisasheesh.realestate.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.whoisasheesh.realestate.R
import com.whoisasheesh.realestate.data.Property
import com.whoisasheesh.realestate.databinding.FragmentPropertyDetailsBinding
import com.whoisasheesh.realestate.listeners.ScreenChangeListener

class PropertyDetailsFragment : Fragment() {
    private var host: ScreenChangeListener? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPropertyDetailsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_property_details,
            container,
            false
        )

        setPropertyDetails(binding)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setPropertyDetails(binding: FragmentPropertyDetailsBinding) {
        // Get the propertyModel from arguments passed to the fragment
        val property: Property? = arguments?.getParcelable("property")
        binding.property = property
        binding.tvPropertyStatus.text = property?.propertyType + " " + property?.saleType
        binding.tvAddress.text = property?.address
        binding.tvAgentName.text = property?.agentInfo
        binding.tvBathroomCount.text = property?.bathroom
        binding.tvBedroomCount.text = property?.bedroom
        binding.tvCarspaceCount.text = property?.carspace
        binding.tvPrice.text = property?.propertyPrice
        binding.tvDescription.text = property?.propertyDesc
        binding.tvSuburbPostcode.text = property?.suburb + " " + property?.postcode
        Picasso.get()
            .load(property?.propertyImg).resize(800, 800)
            .error(R.drawable.ic_image_not_available)
            .into(binding.ivPropertyThumbnail)
        Picasso.get().load(property?.agentImg).resize(400, 400)
            .error(R.drawable.ic_image_not_available)
            .into(binding.ivAgentIcon)

        property?.address?.let {
            host?.setupCustomToolbarWithoutNavigation(
                it,
                R.drawable.nav_icon,
                isNavigationIconEnabled = true
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        host = context as ScreenChangeListener
    }

    companion object {
        fun newInstance(property: Property): PropertyDetailsFragment {
            val fragment = PropertyDetailsFragment()
            val args = Bundle()
            args.putParcelable("property", property)
            fragment.arguments = args
            return fragment
        }
    }
}