package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private String myEdit;
    ArrayList<Todo> myList = new ArrayList<>();
    AlertDialog.Builder builder = null;
    Adaptater myAdaptater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editText = (EditText) findViewById(R.id.my_input);
        Button button = (Button) findViewById(R.id.create_button);

        final Context myView = this;
        final MyAwesomeApp app = (MyAwesomeApp) getApplicationContext();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("todos");




        // When the app launches, regardsless of the specific screen, you will load and log
        // (to the default logger, A.K.A android.util.Log) the current size of your TODOs list.
        Log.d("MainActivitySize", Integer.toString(myList.size())); // When to use this

        final RecyclerView myRecycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        myRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        if (savedInstanceState != null) {

            myEdit = savedInstanceState.getString("myText");
            editText.setText(savedInstanceState.getString("myText"));


        }

        myAdaptater = new Adaptater(myList, app);
        myRecycler.setAdapter(myAdaptater);
        app.tdLst.myAdaptater = myAdaptater;


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextString = editText.getText().toString();
                if (editTextString.isEmpty()) {
                    Toast.makeText(myView, "You can't create an empty TODO item, oh silly !", Toast.LENGTH_LONG).show();
                } else {
                    editText.getText().clear();
                    app.tdLst.addTodo(new Todo(editTextString, false));
                    myAdaptater.setMyList(myList);
                    myAdaptater.notifyDataSetChanged();

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final MyAwesomeApp app = (MyAwesomeApp) getApplicationContext();
        myList = app.tdLst.getAllTodos();
        myAdaptater.setMyList(myList);
        myAdaptater.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("myText", editText.getText().toString());
        super.onSaveInstanceState(outState);
    }


    public void openNotComplete(String id) {
        Intent intent = new Intent(this, NotComplete.class);
        intent.putExtra("todo", id);
        startActivity(intent);
    }


    public void openComplete(String id){
        Intent intent = new Intent(this, Complete.class);
        intent.putExtra("todo", id);
        startActivity(intent);
    }

    public class Adaptater extends RecyclerView.Adapter<Adaptater.Holder> {

        private ArrayList<Todo> myList;
        private MyAwesomeApp app;


        Adaptater(ArrayList<Todo> myList, MyAwesomeApp app) {
            this.myList = myList;
            this.app = app;
        }

        public ArrayList getMyList() {
            return myList;
        }

        public void setMyList(ArrayList<Todo> newLst) {
            myList = newLst;
        }

        public void addItem(Todo item) {
            myList.add(item);
        }

        public void deleteItem(Todo item) {
            myList.remove(item);
        }


        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final Context myContext = parent.getContext();
            View myView = LayoutInflater.from(myContext).inflate(R.layout.item_one_todo, parent, false);
            final Holder myHolder = new Holder(myView);

            myView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = myList.get(myHolder.getAdapterPosition()).getId();

                    if (!(myList.get(myHolder.getAdapterPosition())).getDone()) {
                        openNotComplete(id);
                    }
                    else{
                        openComplete(id);
                    }
                }
            });

            return myHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Todo myTodo = myList.get(position);
            holder.getMyText().setText(myTodo.getContent());

            ImageView myImg = holder.getMyImage();
            if (myTodo.getDone()) {
                // Mark td as done
                myImg.setImageResource(R.drawable.ic_check_box_black_24dp);
            } else {
                myImg.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
            }
        }

        @Override
        public int getItemCount() {
            return getMyList().size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            private TextView myText;
            private ImageView myImage;

            Holder(@NonNull View view) {
                super(view);
                myText = view.findViewById(R.id.todo_text);
                myImage = view.findViewById(R.id.check_image);
            }

            public ImageView getMyImage() {
                return myImage;
            }

            public TextView getMyText() {
                return myText;
            }

            public void setMyImage(ImageView myImage) {
                this.myImage = myImage;
            }

            public void setMyText(TextView myText) {
                this.myText = myText;
            }
        }

    }

}
