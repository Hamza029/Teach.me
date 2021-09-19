package com.example.teachme;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class UserRecViewAdapter extends RecyclerView.Adapter<UserRecViewAdapter.ViewHolder> {

    private ArrayList<User> users = new ArrayList<>();

    private Context context;

    public UserRecViewAdapter(Context context) {
        this.context = context;
    }

    public UserRecViewAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameText.setText(users.get(position).getName());
        holder.institutionText.setText(users.get(position).getInstitution());
        holder.addressText.setText(users.get(position).getAdress());

        Glide.with(context).load(users.get(position).getImageURL())
                .into(holder.imageV);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // click listener
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameText, institutionText, addressText;
        private ImageView imageV;

        private RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.nameID);
            institutionText = itemView.findViewById(R.id.institutionID);
            addressText = itemView.findViewById(R.id.addressID);
            imageV = itemView.findViewById(R.id.imageVID);

            parent = itemView.findViewById(R.id.parent);

//            Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/teachme-f19d5.appspot.com/o/images%2F20ea3b3a-60ff-4050-831b-bf8a7610ac4b?alt=media&token=da6da9b5-9160-42c3-aad0-b505fae37e79")
//                .into(imageV);
        }
    }
}
