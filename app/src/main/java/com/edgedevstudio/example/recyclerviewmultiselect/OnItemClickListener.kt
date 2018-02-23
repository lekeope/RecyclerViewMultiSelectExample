package com.edgedevstudio.example.recyclerviewmultiselect

import android.view.View

/**
 * Created by OPEYEMI OLORUNLEKE on 2/17/2018.
 */
interface OnItemClickListener {
    fun onItemClick(view: View, position: Int)
    fun onItemLongClick(view: View?, position: Int)
}