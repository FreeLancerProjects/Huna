package com.semicolon.huna.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.semicolon.huna.Fragments.Fragment_Driver_Register;
import com.semicolon.huna.Fragments.Fragment_Profile;
import com.semicolon.huna.Models.CountryModel;
import com.semicolon.huna.R;

import java.util.List;

public class Country_Adapter extends RecyclerView.Adapter<Country_Adapter.MyHolder> {
    private Context context;
    private List<CountryModel> countryModelList;
    private int lastSelectedPos=-1;
    private Fragment_Driver_Register fragment;
    private Fragment_Profile fragment_profile;
    private String fragment_type;

    public Country_Adapter(Context context, List<CountryModel> countryModelList, Fragment fragment,String fragment_type) {
        this.context = context;
        this.countryModelList = countryModelList;
        this.fragment_type = fragment_type;
        if (fragment_type.equals("fragment_driver_register"))
        {
            this.fragment = (Fragment_Driver_Register) fragment;

        }else
            {
                this.fragment_profile = (Fragment_Profile) fragment;
            }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.country_neighbourhood_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        CountryModel countryModel = countryModelList.get(position);
        holder.BindData(countryModel);
        holder.rb.setChecked(lastSelectedPos==position);
    }

    @Override
    public int getItemCount() {
        return countryModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private RadioButton rb;
        private LinearLayout ll_country_container;
        public MyHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            rb = itemView.findViewById(R.id.rb);
            ll_country_container = itemView.findViewById(R.id.ll_country_container);
            ll_country_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fragment_type.equals("fragment_driver_register"))
                    {
                        fragment.setItem(countryModelList.get(getAdapterPosition()));

                    }else
                    {
                        fragment_profile.setItem(countryModelList.get(getAdapterPosition()));
                    }
                    lastSelectedPos = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });





        }

        public void BindData(CountryModel countryModel)
        {
            tv_name.setText(countryModel.getCity_title());


        }
    }
}
