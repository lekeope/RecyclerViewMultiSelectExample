package com.edgedevstudio.example.recyclerviewmultiselect

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

/**
 * Created by OPEYEMI OLORUNLEKE on 3/4/2018.
 */
class MyViewHolder(itemView: View, val r_tap: ViewHolderClickListener) : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener, View.OnClickListener {

    val textView: TextView
    val frameLayout: FrameLayout

    init {
        textView = itemView.findViewById(R.id.myTextView)
        frameLayout = itemView.findViewById(R.id.root_layout)
        frameLayout.setOnClickListener(this)
        frameLayout.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        r_tap.onTap(adapterPosition)
    }

    override fun onLongClick(v: View?): Boolean {
        r_tap.onLongTap(adapterPosition)
        return true
    }
}