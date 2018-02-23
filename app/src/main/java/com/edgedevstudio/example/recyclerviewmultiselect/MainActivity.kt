package com.edgedevstudio.example.recyclerviewmultiselect

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
    var recyclerViewAdapter: RecyclerViewAdapter? = null
    var isMultiSelect = false
    var actionMode: ActionMode? = null
    var selectedIds: MutableList<String> = ArrayList()

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "inside onCreate")

        val recyclerView = findViewById<RecyclerView>(R.id.r_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dummyData = getDummyData()
        recyclerViewAdapter = RecyclerViewAdapter(this, dummyData)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.addOnItemTouchListener(RecyclerViewItemClickListener(this,
                recyclerView, object : OnItemClickListener {
            override fun onItemLongClick(view: View?, position: Int) {
                if (!isMultiSelect) {
                    selectedIds = ArrayList<String>()
                    isMultiSelect = true
                    if (actionMode == null) {
                        actionMode = startActionMode(StartActionModeCallback()) //show ActionMode.
                    }
                }
                multiSelect(position)
            }

            override fun onItemClick(view: View, position: Int) {
                if (isMultiSelect) {
                    multiSelect(position)
                }
            }
        }))
    }

    inner class StartActionModeCallback : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.action_delete -> {
                    recyclerViewAdapter?.deleteSelecteIds()
                    actionMode?.setTitle("") //remove item count from action mode.
                    actionMode?.finish()
//                    val stringBuilder = StringBuilder()
//                    for (data in getDummyData()) {
//                        if (selectedIds.contains(data.id))
//                            stringBuilder.append("\n").append(data.title)
//                    }
//                    Toast.makeText(this@MainActivity, "Removed items are :" + stringBuilder.toString(), Toast.LENGTH_SHORT).show()
                    return true
                }
            }
            return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            val inflater = mode?.getMenuInflater()
            inflater?.inflate(R.menu.action_mode_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            isMultiSelect = false
            selectedIds = ArrayList()
            recyclerViewAdapter?.setSelectedIds(ArrayList<String>())
        }
    }

    private fun multiSelect(position: Int) {
        val model = recyclerViewAdapter?.getItem(position)
        if (model != null) {
            if (actionMode != null)  {
                if (selectedIds.contains(model.id))
                    selectedIds.remove(model.id)
                else
                    selectedIds.add(model.id)

                if (selectedIds.size > 0)
                    actionMode?.setTitle(selectedIds.size.toString()) //show selected item count on action mode.
                else {
                    actionMode?.setTitle("") //remove item count from action mode.
                    actionMode?.finish() //hide action mode.
                }
                recyclerViewAdapter?.setSelectedIds(selectedIds)
            }
        }
    }

    private fun getDummyData(): MutableList<MyModel> {
        Log.d(TAG, "inside getDummyData")
        val list = ArrayList<MyModel>()
        list.add(MyModel(getRandomID(), "GridView"))
        list.add(MyModel(getRandomID(), "Switch"))
        list.add(MyModel(getRandomID(), "SeekBar"))
        list.add(MyModel(getRandomID(), "EditText"))
        list.add(MyModel(getRandomID(), "ToggleButton"))
        list.add(MyModel(getRandomID(), "ProgressBar"))
        list.add(MyModel(getRandomID(), "ListView"))
        list.add(MyModel(getRandomID(), "RecyclerView"))
        list.add(MyModel(getRandomID(), "ImageView"))
        list.add(MyModel(getRandomID(), "TextView"))
        list.add(MyModel(getRandomID(), "Button"))
        list.add(MyModel(getRandomID(), "ImageButton"))
        list.add(MyModel(getRandomID(), "Spinner"))
        list.add(MyModel(getRandomID(), "CheckBox"))
        list.add(MyModel(getRandomID(), "RadioButton"))
        Log.d(TAG, "The size is ${list.size}")
        return list
    }

    fun getRandomID() = UUID.randomUUID().toString()


    class RecyclerViewAdapter(val context: Context, val modelList: MutableList<MyModel>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
        var selectedIDs: MutableList<String> = ArrayList<String>()
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent?.context)
            val view = inflater.inflate(R.layout.adapter_item_layout, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return modelList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            holder?.title?.setText(modelList[position].title)
            val id = modelList[position].id

            if (selectedIDs.contains(id)) {
                //if item is selected then,set foreground color of FrameLayout.
                holder?.rootView?.foreground = ColorDrawable(ContextCompat.getColor(context, R.color.colorControlActivated))
            } else {
                //else remove selected item color.
                holder?.rootView?.foreground = ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent))
            }
        }

        fun getItem(position: Int): MyModel {
            return modelList[position]
        }

        fun setSelectedIds(selectedIds: MutableList<String>) {
            this.selectedIDs = selectedIds
            notifyDataSetChanged()
        }

        fun deleteSelecteIds() {
            if (selectedIDs.size < 1) return
            val m : MutableList<MyModel>  = ArrayList()
            for (id in selectedIDs) {
                for (model in modelList) {
                    if (model.id == id) {
                        m.add(model)
                    }
                }
            }
            modelList.removeAll(m)
            notifyDataSetChanged()
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var title: TextView
            var rootView: FrameLayout

            init {
                title = itemView.findViewById(R.id.title)
                rootView = itemView.findViewById(R.id.root_view)
            }
        }


    }
}

