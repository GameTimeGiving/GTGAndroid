package com.gametimegiving.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CharityAdapter extends RecyclerView.Adapter<CharityAdapter.CharityViewHolder> {
    final String TAG = "CharityAdapter";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Context mCtx;
    private List<Charity> mCharityList;

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
        holder.tvCharityName.setText(charity.getName());
        holder.btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "Saving this charity for you", Toast.LENGTH_SHORT).show();

            }
        });
        holder.btndetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "Taking your to the charity details", Toast.LENGTH_SHORT).show();
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
