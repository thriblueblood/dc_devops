package com.example.dechproduct.s23dquizapplication.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dechproduct.s23dquizapplication.AddNewList;
import com.example.dechproduct.s23dquizapplication.DBUtils.DatabaseHandler;
import com.example.dechproduct.s23dquizapplication.MainActivity;
import com.example.dechproduct.s23dquizapplication.Model.ModelMock;
import com.example.dechproduct.s23dquizapplication.R;

import java.util.List;

public class S23DAdapter extends RecyclerView.Adapter<S23DAdapter.ViewHolder> {

    private List<ModelMock> modelMockList;
    private MainActivity activity;
    private DatabaseHandler db;

    public S23DAdapter(DatabaseHandler db, MainActivity activity){
        this.db = db;
        this.activity = activity;
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout,parent,false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     db.openDatabase();
     ModelMock item = modelMockList.get(position);
     holder.task.setText(item.getTask());
     holder.task.setChecked(toBoolean(item.getStatus()));
     //add holder
     holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if (isChecked){
                 db.updateStatus(item.getId(),1);
             }
             else {
                 db.updateStatus(item.getId(),0);
             }
         }
     });
    }

    private  Boolean toBoolean(int n){
        return n!=0;
    }

    @Override
    public int getItemCount() {
        return modelMockList.size();
    }


    public void setTasks(List<ModelMock> modelMockList){
        this.modelMockList = modelMockList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }

    public void editItem(int position){
        ModelMock item = modelMockList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task", item.getTask());
        AddNewList fragment = new AddNewList();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewList.TAG);

    }

    //delete item
    public void deleteItem(int position){
        ModelMock item  = modelMockList.get(position);
        db.deleteTask(item.getId());
        modelMockList.remove(position);
        notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

         ViewHolder(View view) {
             super(view);
             task = view.findViewById(R.id.firstCheckBox);
        }
    }


}
