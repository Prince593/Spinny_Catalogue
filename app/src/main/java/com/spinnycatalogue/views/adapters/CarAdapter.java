package com.spinnycatalogue.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.spinnycatalogue.R;
import com.spinnycatalogue.data.local.CarData;
import com.spinnycatalogue.databinding.LayoutItemCarBinding;
import com.spinnycatalogue.utils.OnClickImageUtils;
import com.spinnycatalogue.views.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends BaseAdapter<CarAdapter.CarViewHolder, CarData> {


    Context context;
    OnClickImageUtils onClickImageUtils;
    List<CarData> list = new ArrayList<>();

    public CarAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void setData(List<CarData> data) {
        list = data;
    }

    public void setOnClickImageUtils(OnClickImageUtils onClickImageUtils) {
        this.onClickImageUtils = onClickImageUtils;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarViewHolder(LayoutItemCarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CarData carData = list.get(position);
        holder.binding.setCar(carData);


        boolean carImagePresent = !carData.getCarImage().trim().isEmpty();

        if (carImagePresent) {
            if (isValidContext(context)) {
                Glide.with(context)
                        .load(carData.getCarImage())
                        .into(holder.binding.image);
            }
        } else {
            Glide.with(context)
                    .load(R.drawable.drawable_default_add_image)
                    .into(holder.binding.image);
        }


            holder.binding.cardImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickImageUtils != null) {
                        onClickImageUtils.addImage(position);
                    }
                }
            });



        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickImageUtils != null) {
                    onClickImageUtils.deleteCar(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {
        public LayoutItemCarBinding binding;

        public CarViewHolder(@NonNull LayoutItemCarBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
