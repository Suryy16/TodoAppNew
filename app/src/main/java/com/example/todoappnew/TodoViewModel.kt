package com.example.todoappnew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class TodoItem(
    val id: String = "",
    val title: String = "",
    val completed: Boolean = false
)

class TodoViewModel : ViewModel() {

    private val _todoList = MutableLiveData<List<TodoItem>>()
    val todoList: LiveData<List<TodoItem>> get() = _todoList

    private val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
    private val database = FirebaseDatabase.getInstance().getReference("todos").child(userId ?: "")

    init {
        loadTodos()
    }

    private fun loadTodos() {
        if (userId == null) return  // Return if user is not authenticated

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val todos = mutableListOf<TodoItem>()
                for (todoSnapshot in snapshot.children) {
                    val todo = todoSnapshot.getValue(TodoItem::class.java)
                    if (todo != null) {
                        todos.add(todo)
                    }
                }
                _todoList.value = todos
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }

    fun addTodo(todoItem: TodoItem) {
        if (userId == null) return  // Return if user is not authenticated

        val newTodoRef = database.push()
        newTodoRef.setValue(todoItem.copy(id = newTodoRef.key ?: ""))
    }

    fun updateTodo(todoItem: TodoItem) {
        if (userId == null) return  // Return if user is not authenticated

        val todoRef = database.child(todoItem.id)
        todoRef.setValue(todoItem)
    }

    fun deleteTodo(todoId: String) {
        if (userId == null) return  // Return if user is not authenticated

        val todoRef = database.child(todoId)
        todoRef.removeValue()
    }
}