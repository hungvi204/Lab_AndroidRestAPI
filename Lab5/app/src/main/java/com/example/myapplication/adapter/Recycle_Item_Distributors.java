package com.example.myapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.handel.Item_Distributor_Hander;
import com.example.myapplication.model.Distributor;

import java.util.ArrayList;

import retrofit2.Callback;

public class Recycle_Item_Distributors extends RecyclerView.Adapter<Recycle_Item_Distributors.ViewHoder> {
    private Context context;
    private ArrayList<Distributor> ds;
    Item_Distributor_Hander hander;

    public Recycle_Item_Distributors(Context context, ArrayList<Distributor> ds, Item_Distributor_Hander hander) {
        this.context = context;
        this.ds = ds;
        this.hander = hander;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_distributor, null);
        ViewHoder viewItem = new ViewHoder(view);
        return viewItem;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        Distributor distributor = ds.get(position);
        if (distributor == null) {
            return;
        }
//        holder.tvId.setText(distributor.getId());
        holder.tvTitle.setText(distributor.getName());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn xóa ?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hander.Delete(distributor.getId());
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogForUpdate(distributor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tvId, tvTitle;
        ImageView imgUpdate, imgDelete;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgUpdate = itemView.findViewById(R.id.imgUpdate);
        }
    }
    // Phương thức để hiển thị dialog update
    private void showDialogForUpdate(Distributor distributorToUpdate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_item_distributor, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        EditText edtName = view.findViewById(R.id.edtName);
        edtName.setText(distributorToUpdate.getName()); // Đặt giá trị cho EditText với thông tin hiện tại của đối tượng

        Button btnUpdate = view.findViewById(R.id.dialogUpdate);
        Button btnBack = view.findViewById(R.id.dialogBack);
        btnUpdate.setText("Cập nhật"); // Thay đổi text của nút thành "Cập nhật"

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                alertDialog.dismiss();
            }
        });
        btnUpdate.setOnClickListener(v->{
            if(!edtName.getText().toString().isEmpty()){
                // Cập nhật thông tin của đối tượng với giá trị mới từ EditText
                distributorToUpdate.setName(edtName.getText().toString().trim());
                // Gọi phương thức Update từ Interface để cập nhật đối tượng
                hander.Update(distributorToUpdate.getId(), distributorToUpdate);
                alertDialog.dismiss();
            } else {
                Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
