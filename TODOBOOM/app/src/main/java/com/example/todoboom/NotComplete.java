package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Date;

public class NotComplete extends AppCompatActivity {


    TextView myContent;
    private EditText editText;
    private String myEdit;
    TextView createDate;
    TextView EditDate;
    Todo myTodo;
    MyAwesomeApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_complete);

        this.myContent = (TextView) findViewById(R.id.my_content);
        this.editText = (EditText) findViewById(R.id.editButton);
        Button applyB = (Button) findViewById(R.id.applyButton);
        this.createDate = (TextView) findViewById(R.id.create_date);
        this.EditDate = (TextView) findViewById(R.id.edit_date);
        Button doneB = (Button) findViewById(R.id.done_button);

        app = (MyAwesomeApp) getApplicationContext();

        Intent intent = getIntent();
        final String idTodo = intent.getStringExtra("todo");

        myTodo = app.tdLst.getTodoFromId(idTodo);

        System.out.println(myTodo.getContent());
        myContent.setText(myTodo.getContent().toUpperCase());
        Date date = new Date(myTodo.getCreateTime());
        String dateStr = "Item created at : " + date;
        createDate.setText(dateStr);
        date = new Date(myTodo.getEditTime());
        dateStr = "Item edited at : " + date;
        EditDate.setText(dateStr);


        applyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTodo.setCreateTime(myTodo.getCreateTime());
                myTodo.setId(myTodo.getId());

                Date date = new Date(myTodo.getEditTime());
                String dateStr = "Item edited at : " + date;
                EditDate.setText(dateStr);


                String newContent = editText.getText().toString();
                if (!newContent.isEmpty()){
                    editText.getText().clear();
                    myContent.setText(newContent.toUpperCase());
                    myTodo.setContent(newContent);
                    myTodo.setEditTime(System.currentTimeMillis());
                    date = new Date(myTodo.getEditTime());
                    dateStr =  "Item edited at : " + date;
                    EditDate.setText(dateStr);

                    // Tells the change was made
                    app.tdLst.editTodo(myTodo);
                    Toast.makeText(NotComplete.this, "EDITED WITH SUCCESS",Toast.LENGTH_SHORT).show();
                }



                // Go back to the main screen ??
//                openMain();

            }
        });

        doneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myTodo.setDone(true);
                myTodo.setEditTime(System.currentTimeMillis());
                app.tdLst.editTodo(myTodo);
                Toast.makeText(NotComplete.this, "Item " + myTodo.getContent().toUpperCase() + " is done !",Toast.LENGTH_SHORT).show();
                openMain();
            }
        });

    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
