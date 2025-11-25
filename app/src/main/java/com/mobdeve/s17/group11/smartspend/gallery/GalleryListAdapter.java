package com.mobdeve.s17.group11.smartspend.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesEditActivity;
import com.mobdeve.s17.group11.smartspend.expenses.ExpensesListItem;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;

import java.util.ArrayList;
import java.util.List;

public class GalleryListAdapter extends RecyclerView.Adapter<GalleryListAdapter.GalleryListItemViewHolder> {

    public List<Integer> items = new ArrayList<>();

    private final Context context;

    public GalleryListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GalleryListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryListItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_gallery_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryListItemViewHolder holder, int position) {
        int imageID = items.get(position);

        Glide.with(context)
                .load(SessionCache.galleryDirectory + "/" + imageID + ".jpg")
                .override(256, 256)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .error(R.drawable.ic_image_missing)
                .into(holder.imgThumbnail);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ExpensesEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            ExpensesListItem expenseEdit = null;

            for(ExpensesListItem expense : SessionCache.expensesItems) {
                if(imageID == expense.sqlRowID)
                    expenseEdit = expense;
            }

            if(expenseEdit == null)
                return;

            ExpensesEditActivity.expenseEdit = expenseEdit;
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class GalleryListItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;

        public GalleryListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgThumbnail = itemView.findViewById(R.id.img_thumbnail);
        }

    }

}
