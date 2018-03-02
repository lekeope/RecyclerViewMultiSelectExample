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
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity(), MainInterface {

    var actionMode: ActionMode? = null
    var myAdapter: MyAdapter? = null


    companion object {
        var isMultiSelectOn = false
        val TAG = "MainActivity"
    }

    override fun mainInterface(size: Int) {
        if (actionMode == null) actionMode = startActionMode(ActionModeCallback())
        if (size > 0) actionMode?.setTitle("$size")
        else actionMode?.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isMultiSelectOn = false

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        myAdapter = MyAdapter(this, this)
        recyclerView.adapter = myAdapter
        myAdapter?.modelList = getDummyData()
        myAdapter?.notifyDataSetChanged()
    }

    inner class ActionModeCallback : ActionMode.Callback {
        var shouldResetRecyclerView = true
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.getItemId()) {
                R.id.action_delete -> {
                    shouldResetRecyclerView = false
                    myAdapter?.deleteSelectedIds()
                    actionMode?.setTitle("") //remove item count from action mode.
                    actionMode?.finish()
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
            menu?.findItem(R.id.action_delete)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            if (shouldResetRecyclerView) {
                myAdapter?.selectedIds?.clear()
                myAdapter?.notifyDataSetChanged()
            }
            isMultiSelectOn = false
            actionMode = null
            shouldResetRecyclerView = true
        }
    }

    private fun getDummyData(): MutableList<MyModel> {
        Log.d(TAG, "inside getDummyData")
        val list = ArrayList<MyModel>()
        list.add(MyModel(getRandomID(), "1. GridView"))
        list.add(MyModel(getRandomID(), "2. Switch"))
        list.add(MyModel(getRandomID(), "3. SeekBar"))
        list.add(MyModel(getRandomID(), "4. EditText"))
        list.add(MyModel(getRandomID(), "5. ToggleButton"))
        list.add(MyModel(getRandomID(), "6. ProgressBar"))
        list.add(MyModel(getRandomID(), "7. ListView"))
        list.add(MyModel(getRandomID(), "8. RecyclerView"))
        list.add(MyModel(getRandomID(), "9. ImageView"))
        list.add(MyModel(getRandomID(), "10. TextView"))
        list.add(MyModel(getRandomID(), "11. Button"))
        list.add(MyModel(getRandomID(), "12. ImageButton"))
        list.add(MyModel(getRandomID(), "13. Spinner"))
        list.add(MyModel(getRandomID(), "14. CheckBox"))
        list.add(MyModel(getRandomID(), "15. RadioButton"))
        Log.d(TAG, "The size is ${list.size}")
        return list
    }

    fun getRandomID() = UUID.randomUUID().toString()

    class MyAdapter(val context: Context, val mainInterface: MainInterface) : RecyclerView.Adapter<MyViewHolder>(), RecyclerViewClick {
        override fun onLongTap(index: Int) {
            if (!isMultiSelectOn) {
                isMultiSelectOn = true
            }
            addIDIntoSelectedIds(index)
        }

        override fun onTap(index: Int) {
            if (isMultiSelectOn) {
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
            if (selectedIds.size < 1) isMultiSelectOn = false
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
            val selectedIdIteration: MutableListIterator<String> = selectedIds.listIterator();

            while (selectedIdIteration.hasNext()) {
                val selectedItemID = selectedIdIteration.next()
                Log.d(TAG, "The ID is $selectedItemID")
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

                isMultiSelectOn = false
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent?.context)
            val itemView = inflater.inflate(R.layout.view_holder_layout, parent, false)
            return MyViewHolder(itemView, this)
        }
    }

    class MyViewHolder(itemView: View, val r_tap: RecyclerViewClick) : RecyclerView.ViewHolder(itemView),
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
}