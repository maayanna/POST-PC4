package com.example.todoboom;

import android.util.Log;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TdLstStore {

    private ArrayList<Todo> myLst;
    public MainActivity.Adaptater myAdaptater;
    public TodoDAO dao;

    TdLstStore(Context context){

        myLst = new ArrayList<Todo>();
        Query();
        dao = new TodoDAO();

    }

    public ArrayList<Todo> getAllTodos(){
        return myLst;
    }

    private void Query(){
        // Get the instance of Cloud Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get the todos collection reference
        CollectionReference tdLstCollectionRef = db.collection("todos");

        // Listen for todos
        tdLstCollectionRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    Log.w("Firestore", "Listen failed.", e);
                    return;
                }

                if (queryDocumentSnapshots == null) {

                    Log.d("Firestore", "Current data: null");

                } else {

                    // Update the list (restore)
                    myLst.clear();

                    for (QueryDocumentSnapshot myDoc : queryDocumentSnapshots){
                        Todo todo = myDoc.toObject(Todo.class);
                        myLst.add(todo);
                    }

                    myAdaptater.setMyList(myLst);
                    myAdaptater.notifyDataSetChanged();

                    Log.d("Firestore", "Current data: " + queryDocumentSnapshots);

                }
            }
        });

        tdLstCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                myAdaptater.setMyList(myLst);
                myAdaptater.notifyDataSetChanged();
            }
        });

        tdLstCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                myAdaptater.setMyList(myLst);
                myAdaptater.notifyDataSetChanged();
            }
        });
    }


    void addTodo(Todo todo){
        dao.addTodo(todo);
        myLst.add(todo);
        myLst.add(todo);
        myAdaptater.setMyList(myLst);
        myAdaptater.notifyDataSetChanged();

    }

    void deleteTodoForever(Todo todo){
        dao.deleteTodoForever(todo);
        myLst.remove(todo);
        myAdaptater.setMyList(myLst);
        myAdaptater.notifyDataSetChanged();
    }

    void editTodo(Todo newTodo){

        for(int i = 0; i < myLst.size(); i++){

            Todo todo = myLst.get(i);

            if(todo.getId().equals(newTodo.getId())){

                myLst.set(i, newTodo); // At the same place
                myAdaptater.setMyList(myLst);
                myAdaptater.notifyDataSetChanged();

                break;

            }
        }
        dao.editTodo(newTodo);

    }

    public Todo getTodoFromId(String id){


        return dao.getTodoById(id);
    }

}
