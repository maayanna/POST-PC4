package com.example.todoboom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

public class Complete extends AppCompatActivity {

    TextView myContent;
    Todo myTodo;
    MyAwesomeApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        this.myContent = (TextView) findViewById(R.id.my_content2);
        Button undoneB = (Button) findViewById(R.id.undoneButton);
        Button deleteB = (Button) findViewById(R.id.deleteButton);


        app = (MyAwesomeApp) getApplicationContext();

        Intent intent = getIntent();
        final String idTodo = intent.getStringExtra("todo");


        myTodo = app.tdLst.getTodoFromId(idTodo);

        System.out.println(myTodo.getContent());

        myContent.setText(myTodo.getContent().toUpperCase());



        undoneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTodo.setDone(false);
                app.tdLst.editTodo(myTodo);
                Toast.makeText(Complete.this, "Item " + myTodo.getContent().toUpperCase() + " is undone !",Toast.LENGTH_SHORT).show();
            }
        });

        deleteB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Complete.this);
                    builder.setTitle("DELETE")
                            .setMessage("ARE YOU SURE TO DELETE ?")
                            .setNegativeButton("NO", null)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    app.tdLst.deleteTodoForever(myTodo);
                                    Toast.makeText(Complete.this, "DELETED WITH SUCCESS",Toast.LENGTH_SHORT).show();
                                    //  Go back to the main screen
                                    openMain();

                                }
                            });

                    builder.show();




            }
        });

    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}
