package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


class MainActivity : AppCompatActivity() {
    // store list of tasks in this variable
    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter // lateinit - initialize later

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // specify what happens when item is long clicked
                // 1. remove item from list
                listOfTasks.removeAt(position)
                // 2. notify adapter that data set has changed
                adapter.notifyDataSetChanged()
                saveItems()
            }

        }

        // 1. detect when user clicks on the add button
        /*findViewById<Button>(R.id.button).setOnClickListener {
            // code inside will be executed when user clicks on button
            // helpful to have a log message to let ourselves know if
            // user has clicked button
            Log.i("Caren", "User clicked on button")

        }*/

        loadItems()

        // look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks,onLongClickListener)
        // attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter // connects recyclerview and adapter
        // set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // set up the button and input field so that the user can enter a task and add it
        val inputTextField = findViewById<EditText>(R.id.addTaskField) // call this variable later in like 46

        findViewById<Button>(R.id.button).setOnClickListener{
            // 1. grab the text that the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()
            // 2. add the string to our list of tasks: listOfTasks
            listOfTasks.add(userInputtedTask)

            // notify the adapter that the data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1) // because array starts at index 0
                // must notify adapter to actually be able to
                // add to the list of strings

            // 3. reset text field
            inputTextField.setText("")
            saveItems()
        }


    }

    // save the data user has inputted by writing to and reading from a file

    // get the file we need
    fun getDataFile() : File {

        // every line is going to represent a specific task in our list of tasks

        return File(filesDir, "data.txt")
    }

    // load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // save items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks )
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

}