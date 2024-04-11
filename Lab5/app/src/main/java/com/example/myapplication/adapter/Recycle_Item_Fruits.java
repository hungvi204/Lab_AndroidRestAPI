package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.LocationActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Distributor;
import com.example.myapplication.model.Fruit;

import java.io.Serializable;
import java.util.ArrayList;

public class Recycle_Item_Fruits extends RecyclerView.Adapter<Recycle_Item_Fruits.ViewHoder>{
    private Context context;
    private ArrayList<Fruit> ds;

    public Recycle_Item_Fruits(Context context, ArrayList<Fruit> ds) {
        this.context = context;
        this.ds = ds;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fruit, null);
        ViewHoder viewHoder = new ViewHoder(view);
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        if (ds == null || ds.isEmpty() || position < 0 || position >= ds.size()) {
            return;
        }
        Fruit fruit = ds.get(position);
        holder.tvName.setText(fruit.getName());
        // Kiểm tra đối tượng Distributor trước khi gán tên
        Distributor distributor = fruit.getDistributor();
        if (distributor != null) {
            holder.tvDistributor.setText(distributor.getName());
        } else {
            holder.tvDistributor.setText("Unknown");
        }

        holder.tvPrice.setText(fruit.getPrice());
        if (!fruit.getImage().isEmpty()) {
            Glide.with(context)
                    .load(fruit.getImage().get(0))//load file hình
                    .thumbnail(Glide.with(context).load(R.drawable.loading))
                    .into(holder.pic);
        }
        holder.imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", (Serializable) ds.get(holder.getAdapterPosition()));
                Intent intent = new Intent(context, LocationActivity.class);
                intent.putExtras(bundle);
                (context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tvName, tvDistributor, tvPrice;
        ImageView pic, imgCart;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDistributor = itemView.findViewById(R.id.tvDistributor);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            pic = itemView.findViewById(R.id.pic);
            imgCart = itemView.findViewById(R.id.imgCart);
        }
    }
}
