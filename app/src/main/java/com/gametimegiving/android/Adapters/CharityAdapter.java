package com.gametimegiving.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gametimegiving.android.Activities.CharityDetail;
import com.gametimegiving.android.Activities.GTGBaseActivity;
import com.gametimegiving.android.Helpers.GlideApp;
import com.gametimegiving.android.R;
import com.gametimegiving.android.models.Charity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CharityAdapter extends RecyclerView.Adapter<CharityAdapter.CharityViewHolder> {
    final String TAG = "CharityAdapter";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context mCtx;
    private List<Charity> mCharityList;
    private GTGBaseActivity mGTGBaseActivity;

    public CharityAdapter(Context ctx, List<Charity> charityList) {
        mCtx = ctx;
        mCharityList = charityList;
    }

    @NonNull
    @Override
    public CharityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.charitylistitem, null);
        CharityViewHolder holder = new CharityViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CharityViewHolder holder, int position) {
        Charity charity = mCharityList.get(position);
        String charityid = charity.getId();
        holder.tvCharityName.setText(charity.getName());
        holder.btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnsave.getText() == mCtx.getString(R.string.followtext)) {
                    holder.btnsave.setText(R.string.unfollowtext);
                } else {
                    holder.btnsave.setText(R.string.followtext);
                }

                mGTGBaseActivity.GTGSnackBar(v, "Saving this charity to your profile");


            }
        });
        holder.btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("charityid", charityid);
                Intent intent = new Intent(v.getContext(), CharityDetail.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);


            }
        });
        // holder.tvCharityPurpose.setText(charity.getPurpose());
        // holder.tvCharityMission.setText(charity.getMission());
        String charityLogo = charity.getLogo();
        StorageReference charityLogoReference;
        try {
            charityLogoReference = storage.getReferenceFromUrl(charityLogo);
        } catch (Exception ex) {
            charityLogoReference = storage.getReferenceFromUrl(String.valueOf(R.string.defaultcharitylogo));

        }
        GlideApp.with(mCtx)
                .load(charityLogoReference)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mCharityList.size();
    }

    class CharityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvCharityName, tvCharityMission, tvCharityPurpose;
        Button btndetail, btnsave;

        public CharityViewHolder(View itemView) {
            super(itemView);
            btndetail = itemView.findViewById(R.id.btnmoredetail);
            btnsave = itemView.findViewById(R.id.btnsavecharity);
            imageView = itemView.findViewById(R.id.charitylogo);
            tvCharityName = itemView.findViewById(R.id.charityname);
            //     tvCharityMission=itemView.findViewById(R.id.charitymission);
            //      tvCharityPurpose=itemView.findViewById(R.id.charitypurpose);
        }

    }
}
