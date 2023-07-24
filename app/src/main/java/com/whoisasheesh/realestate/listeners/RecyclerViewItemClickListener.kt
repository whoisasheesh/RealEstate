package com.whoisasheesh.realestate.listeners

import android.view.View

interface RecyclerViewItemClickListener {
    fun onRecyclerViewItemClicked(position: Int, view: View?, `object`: Any?)
}