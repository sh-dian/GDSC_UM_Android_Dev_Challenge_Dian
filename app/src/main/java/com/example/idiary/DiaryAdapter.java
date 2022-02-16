package com.example.idiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder>{

    private final Context context;
    private ArrayList<Diary> diariesList;
    private final ArrayList<Diary> diariesListAll;
    private final RecyclerViewInterface recyclerViewInterface;

    public DiaryAdapter(Context context, ArrayList<Diary> diariesList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.diariesList = diariesList;
        this.diariesListAll = new ArrayList<>(diariesList);
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public DiaryAdapter.DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.diary_layout, parent, false);
        return new DiaryViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryAdapter.DiaryViewHolder holder, int position) {
        Diary diary = diariesList.get(position);
        holder.diaryTitle.setText(diary.getTitle());
        holder.diaryNote.setText(diary.getNote());
        String diaryType = diary.getType();

        if (diaryType.equals("text")) {
            holder.diaryLoc.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.login_back_ground).transform(new RoundedCorners(15),
                    new CenterCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(holder.diaryImage);

        } else if (diaryType.equals("image")) {
            holder.diaryLoc.setText(diary.getPlaceName());
            String imageURL = diary.getImage();
            if (!imageURL.isEmpty()) {
                Glide.with(context).load(imageURL).transform(new RoundedCorners(15), new CenterCrop())
                        .transition(DrawableTransitionOptions.withCrossFade()).into(holder.diaryImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        return diariesList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        private final TextView diaryTitle;
        private final TextView diaryNote;
        private final TextView diaryLoc;
        private final ImageView diaryImage;

        public DiaryViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            diaryTitle = itemView.findViewById(R.id.adapter_diary_title);
            diaryNote = itemView.findViewById(R.id.adapter_diary_note);
            diaryLoc = itemView.findViewById(R.id.adapter_diary_location);
            diaryImage = itemView.findViewById(R.id.adapter_diary_image);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
