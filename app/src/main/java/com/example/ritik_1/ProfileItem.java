package com.example.ritik_1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProfileItem extends RecyclerView.Adapter<MyViewHolderUser> {
    private final Context context;
    private final List<UserProfileData> dataList;

    public ProfileItem(Context context, List<UserProfileData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_item, parent, false);
        return new MyViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderUser holder, int position) {
        UserProfileData data = dataList.get(position);
        Glide.with(context).load(data.getImage()).into(holder.recImage);
        holder.recName.setText(data.getName());
        holder.recNumber.setText(data.getPhone());
        holder.recAddress.setText(data.getAddress());
        holder.recEmail.setText(data.getEmail());

        // Set onClickListener for the CardView
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open UserProfileDetail activity with data
                Intent intent = new Intent(context, UserProfileDetail.class);
                intent.putExtra("image", data.getImage());
                intent.putExtra("number", data.getPhone());
                intent.putExtra("name", data.getName());
                intent.putExtra("Key", data.getKey());
                intent.putExtra("address", data.getAddress());
                intent.putExtra("email", data.getEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolderUser extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recName, recNumber, recAddress, recEmail;
    CardView recCard;

    public MyViewHolderUser(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recUserImage);
        recCard = itemView.findViewById(R.id.recCardUser);
        recNumber = itemView.findViewById(R.id.recUserNumber);
        recAddress = itemView.findViewById(R.id.recUserAddress);
        recName = itemView.findViewById(R.id.recUserName);
        recEmail = itemView.findViewById(R.id.recUserEmail);
    }
}
