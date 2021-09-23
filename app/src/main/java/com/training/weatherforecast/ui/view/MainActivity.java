package com.training.weatherforecast.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.training.weatherforecast.model.CityModel;
import com.training.weatherforecast.adapter.CityListAdapter;
import com.training.weatherforecast.R;
import com.training.weatherforecast.databinding.ActivityMainBinding;
import com.training.weatherforecast.ui.viewmodel.CityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CityListAdapter adapter;
    private Dialog dialog;
    private CityViewModel cityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        dialog = new Dialog(this);
        cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);

        adapter = new CityListAdapter(this,cityViewModel);
        binding.citiesRV.setHasFixedSize(true);
        binding.citiesRV.setLayoutManager(new LinearLayoutManager(this));
        binding.citiesRV.setAdapter(adapter);

        cityViewModel.getAllCities().observe(this, new Observer<List<CityModel>>() {
            @Override
            public void onChanged(List<CityModel> cities) {
                adapter.setCities(cities);
            }
        });

        binding.floatingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });
        binding.toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(isEmpty()){
                    Toast.makeText(MainActivity.this,"The list is empty",Toast.LENGTH_SHORT).show();
                }else {
                    if(item.getItemId() == R.id.delete_all_bar){
                        openConfirmDialog();
                    }
                }
                return true;
            }
        });
        binding.cityNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(isEmpty()){
                    Toast.makeText(MainActivity.this,"The list is empty",Toast.LENGTH_SHORT).show();
                }else {
                    filter(editable.toString());
                }
            }
        });
    }

    private boolean isEmpty(){
        return cityViewModel.getAllCities().getValue().size() == 0;
    }

    private boolean isExist(CityModel cityModel){
        boolean exist = false;
        for(CityModel c:cityViewModel.getAllCities().getValue()){
            if(c.getName().equals(cityModel.getName())){
                exist = true;
            }
        }
        return exist;
    }

    private void filter(String text) {
        cityViewModel.getAllCities().observe(this, new Observer<List<CityModel>>() {
            @Override
            public void onChanged(List<CityModel> cities) {
                List<CityModel> filteredList = new ArrayList<>();
                for(CityModel cityModel : cities){
                    if(cityModel.getName().contains(text.toUpperCase())){
                        filteredList.add(cityModel);
                    }
                }
                adapter.setCities(filteredList);
            }
        });
    }

    private void openAddDialog() {
        dialog.setContentView(R.layout.add_city_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        ImageView closeButton = dialog.findViewById(R.id.closeDialog);
        EditText newName = dialog.findViewById(R.id.newCityName);
        AppCompatButton addButton = dialog.findViewById(R.id.add_new_city_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!newName.getText().toString().isEmpty()) {
                    String newCity = newName.getText().toString().toUpperCase().trim();
                    if (isExist(new CityModel(newCity))) {
                        Toast.makeText(MainActivity.this, "This city Already Exist", Toast.LENGTH_SHORT).show();
                        newName.setText("");
                    } else {
                        cityViewModel.insert(new CityModel(newCity));
                        adapter.notifyItemInserted(adapter.getItemCount());
                        dialog.dismiss();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "The field is empty !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void openConfirmDialog() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.remove_city_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        TextView text = dialog.findViewById(R.id.textMessage);
        text.setText(R.string.check_delete_all);
        AppCompatButton yes = dialog.findViewById(R.id.yes_button);
        AppCompatButton no = dialog.findViewById(R.id.no_button);
        ImageView close = dialog.findViewById(R.id.closeDialog);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityViewModel.deleteAllCities();
                adapter.notifyDataSetChanged();
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