package com.training.weatherforecast.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.training.weatherforecast.model.CityModel;
import com.training.weatherforecast.ui.viewmodel.CityViewModel;
import com.training.weatherforecast.R;
import com.training.weatherforecast.ui.view.CityPage;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    private Context context;
    private static List<CityModel> cities= new ArrayList<>();
    private CityViewModel cityViewModel;

    public CityListAdapter(Context context,CityViewModel cityViewModel) {
        this.context = context;
        this.cityViewModel = cityViewModel;
    }

    @NonNull
    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListAdapter.ViewHolder holder, int position) {
        CityModel cityModel = cities.get(position);
        holder.name.setText(cityModel.getName());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog(cityModel,holder.getAdapterPosition());
            }
        });
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CityPage.class);
                intent.putExtra("cityName", cityModel.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView remove;
        private CoordinatorLayout background;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cityName);
            remove = (ImageView) itemView.findViewById(R.id.remove_city);
            background = (CoordinatorLayout) itemView.findViewById(R.id.background);
        }
    }

    public void setCities(List<CityModel> cities){
        this.cities = cities;
        notifyDataSetChanged();
    }

    private void openConfirmDialog(CityModel cityModel, int adapterPosition) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.remove_city_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        TextView text = dialog.findViewById(R.id.textMessage);
        text.setText(R.string.check_delete_one);
        AppCompatButton yes = dialog.findViewById(R.id.yes_button);
        AppCompatButton no = dialog.findViewById(R.id.no_button);
        ImageView close = dialog.findViewById(R.id.closeDialog);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityViewModel.delete(cityModel);
                notifyItemRemoved(adapterPosition);
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
