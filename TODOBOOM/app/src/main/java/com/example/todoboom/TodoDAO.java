package com.example.todoboom;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class TodoDAO {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    HashMap<String, Todo> myHash = new HashMap<String, Todo>();


    public void addTodo(Todo todo){
        DocumentReference myDoc = db.collection("todos").document();
        todo.setId(myDoc.getId());
        long time = System.currentTimeMillis();
        todo.setCreateTime(time);
        todo.setEditTime(time);
        myHash.put(todo.getId(), todo);
        // Set the data
        myDoc.set(todo);

    }

    public void deleteTodoForever(Todo todo){
        String id = todo.getId();
        myHash.remove(todo.getId());
        db.collection("todos").document(id).delete();
    }


    public void editTodo(Todo newTodo){

        db.collection("todos").document(newTodo.getId()).set(newTodo);

    }


    public Todo getTodoById(String id){
        return myHash.get(id);
    }

}