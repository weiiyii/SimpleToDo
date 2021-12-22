package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // how layout and mainActivity are connected
        setContentView(R.layout.activity_main)

        // DELETE TASKS

        // Create onLongClickListener function to remove the item on long click
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)

                // 2. Notify the adaptor
                adapter.notifyDataSetChanged()

                // 3. Save to data file
                saveItems()
            }

        }

        // Load items from file before creating the view
        loadItems()

        // CREATE THE VIEW

        // 1. Get the view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // 2. Create adapter passing in user data and longClickListener
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)

        // 3. Attach the adapter to the recyclerView to populate items
        recyclerView.adapter = adapter

        // 4. Set a layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ADD TASKS

        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        val addButton = findViewById<Button>(R.id.button)

        // Set up the button and input field so that the user can enter a task and add it to the list
        addButton.setOnClickListener{

            // 1. Grab the user input text
            val userInput = inputTextField.text.toString()

            // 2. Add the string to the list of tasks
            listOfTasks.add(userInput)

            // 3. Notify the adapter that the data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            // 4. Reset text field
            inputTextField.setText("")

            // 5. Save to data file
            saveItems()
        }




    }

    // SAVE THE DATA USER INPUTTED: By reading/writing from a file

    // 1. Get the data file
    fun getDataFile(): File {
        // Every line is a task
        return File(filesDir, "data.txt")
    }

    // 2. Load the items by reading every line in the data file
    fun loadItems() {
        try {
            val dataFile = getDataFile()
            listOfTasks = FileUtils.readLines(dataFile, Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // 3. Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}