package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.todoapp.Adapter.ToDoAdapter;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.example.todoapp.Utils.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private TextView todoText;
    private RecyclerView todoRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskList = new ArrayList<>();
        bindView();
        String username = Preferences.getLoggedInUser(getBaseContext());
        todoText.setText(username + "'s To Do List");

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(todoRecyclerView);

        taskList = db.getUserTasks(Preferences.getLoggedInUserId(getBaseContext()));
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
    }

    private void bindView() {
        todoText = findViewById(R.id.todoText);
        todoRecyclerView = findViewById(R.id.todoRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, MainActivity.this);
        todoRecyclerView.setAdapter(tasksAdapter);
        fab = findViewById(R.id.fab);
    }

    public void clickFAB(View view) {
        AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
    }

    public void clickLogout(View view) {
        Preferences.clearLoggedInUser(getBaseContext());
        startActivity(new Intent(getBaseContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getUserTasks(Preferences.getLoggedInUserId(getBaseContext()));
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}