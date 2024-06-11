package com.example.jdaplikasi;

public class ItemList {
    private String itemName;
    private String itemLocation;
    private String itemImage;
    private  String itemId;

    public ItemList() {
        // Default constructor required for calls to DataSnapshot.getValue(WishlistItem.class)
    }

    public ItemList(String itemName, String itemLocation, String itemImage) {
        this.itemName = itemName;
        this.itemLocation = itemLocation;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getItemId() {
        return itemId;
    }

    // Setter method for itemId (optional, if needed)
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    // getter dan setter
}
