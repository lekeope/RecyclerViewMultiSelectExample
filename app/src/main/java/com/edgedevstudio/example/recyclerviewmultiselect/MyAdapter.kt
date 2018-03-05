package com.edgedevstudio.example.recyclerviewmultiselect

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import java.util.*

/**
 * Created by OPEYEMI OLORUNLEKE on 3/4/2018.
 */
class MyAdapter(val context: Context, val mainInterface: MainInterface) : RecyclerView.Adapter<MyViewHolder>(), ViewHolderClickListener {
    override fun onLongTap(index: Int) {
        if (!MainActivity.isMultiSelectOn) {
            MainActivity.isMultiSelectOn = true
        }
        addIDIntoSelectedIds(index)
    }

    override fun onTap(index: Int) {
        if (MainActivity.isMultiSelectOn) {
            addIDIntoSelectedIds(index)
        } else {
            Toast.makeText(context, "Clicked Item @ Position ${index + 1}", Toast.LENGTH_SHORT).show()
        }
    }

    fun addIDIntoSelectedIds(index: Int) {
        val id = modelList[index].id
        if (selectedIds.contains(id))
            selectedIds.remove(id)
        else
            selectedIds.add(id)

        notifyItemChanged(index)
        if (selectedIds.size < 1) MainActivity.isMultiSelectOn = false
        mainInterface.mainInterface(selectedIds.size)
    }

    var modelList: MutableList<MyModel> = ArrayList<MyModel>()
    val selectedIds: MutableList<String> = ArrayList<String>()

    override fun getItemCount() = modelList.size

    override fun onBindViewHolder(holder: MyViewHolder?, index: Int) {
        holder?.textView?.setText(modelList[index].title)

        val id = modelList[index].id

        if (selectedIds.contains(id)) {
            //if item is selected then,set foreground color of FrameLayout.
            holder?.frameLayout?.foreground = ColorDrawable(ContextCompat.getColor(context, R.color.colorControlActivated))
        } else {
            //else remove selected item color.
            holder?.frameLayout?.foreground = ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }

    fun deleteSelectedIds() {
        if (selectedIds.size < 1) return
        val selectedIdIteration = selectedIds.listIterator();

        while (selectedIdIteration.hasNext()) {
            val selectedItemID = selectedIdIteration.next()
            Log.d(MainActivity.TAG, "The ID is $selectedItemID")
            var indexOfModelList = 0
            val modelListIteration: MutableListIterator<MyModel> = modelList.listIterator();
            while (modelListIteration.hasNext()) {
                val model = modelListIteration.next()
                if (selectedItemID.equals(model.id)) {
                    modelListIteration.remove()
                    selectedIdIteration.remove()
                    notifyItemRemoved(indexOfModelList)
                }
                indexOfModelList++
            }

            MainActivity.isMultiSelectOn = false
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val itemView = inflater.inflate(R.layout.view_holder_layout, parent, false)
        return MyViewHolder(itemView, this)
    }
}