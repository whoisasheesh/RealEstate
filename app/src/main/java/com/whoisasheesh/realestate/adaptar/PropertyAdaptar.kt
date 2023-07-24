package com.whoisasheesh.realestate.adaptar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.whoisasheesh.realestate.R
import com.whoisasheesh.realestate.listeners.RecyclerViewItemClickListener
import com.whoisasheesh.realestate.data.Property
import com.whoisasheesh.realestate.databinding.ItemPropertyListBinding
import java.util.*

class PropertyAdaptar(private val propertyLists: ArrayList<Property>) :
    RecyclerView.Adapter<PropertyAdaptar.ViewHolder>() {
    private var clickListener: RecyclerViewItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_property_list, parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val propertyModel = propertyLists[position]
        holder.binding.tvPropertyStatus.text = propertyModel.propertyType + " " + propertyModel.saleType
        holder.binding.tvAddress.text = propertyModel.address
        holder.binding.tvSuburbPostcode.text =
            propertyModel.suburb + ", " + propertyModel.postcode
        holder.binding.tvBathroomCount.text = propertyModel.bathroom
        holder.binding.tvBedroomCount.text = propertyModel.bedroom
        holder.binding.tvCarspaceCount.text = propertyModel.carspace
        holder.binding.tvAgentName.text = propertyModel.agentInfo

        Picasso.get()
            .load(propertyModel.propertyImg).resize(800, 800)
            .error(R.drawable.ic_image_not_available)
            .into(holder.binding.ivPropertyThumbnail)
        Picasso.get().load(propertyModel.agentImg).resize(400, 400)
            .error(R.drawable.ic_image_not_available)
            .into(holder.binding.ivAgentIcon)
    }

    override fun getItemCount(): Int {
        return propertyLists.size
    }

    fun setClickListener(clickListener: RecyclerViewItemClickListener?) {
        this.clickListener = clickListener
    }

    inner class ViewHolder(var binding: ItemPropertyListBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        init {
            binding.root.setOnClickListener { v: View? ->
                if (clickListener != null) {
                    clickListener!!.onRecyclerViewItemClicked(
                        layoutPosition,
                        v,
                        propertyLists[layoutPosition]
                    )
                }
            }
        }
    }
}