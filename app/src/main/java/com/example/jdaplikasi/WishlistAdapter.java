package com.example.jdaplikasi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private List<ItemList> wishlistItems;
    private DatabaseReference wishlistRef;

    public WishlistAdapter(List<ItemList> wishlistItems, DatabaseReference wishlistRef) {
        this.wishlistItems = wishlistItems;
        this.wishlistRef = wishlistRef;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        ItemList item = wishlistItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    class WishlistViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTextView;
        private TextView itemLocationTextView;
        private ImageView itemImageView;
        private ImageButton deleteButton;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item_name);
            itemLocationTextView = itemView.findViewById(R.id.item_location);
            itemImageView = itemView.findViewById(R.id.item_image);
            deleteButton = itemView.findViewById(R.id.item_button);
        }

        public void bind(ItemList item) {
            itemNameTextView.setText(item.getItemName());
            itemLocationTextView.setText(item.getItemLocation());
            Glide.with(itemView.getContext()).load(item.getItemImage()).into(itemImageView);

            // Set action to delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    wishlistRef.child(wishlistItems.get(position).getItemId()).removeValue();
                }
            });
        }
    }
}
