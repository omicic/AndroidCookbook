package com.example.androidcookbook.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable {

    private String id;
    private String recipe;
    private String category;
    private String descrtiption;
    private String picture;

    public Recipe() {
        super();
    }

    public Recipe(String id, String recipe, String category, String description, String picture) {
        super();
        this.id = id;
        this.recipe = recipe;
        this.category = category;
        this.descrtiption = description;
        this.picture = picture;
    }

    public Recipe(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        this.id = data[0];
        this.recipe = data[1];
        this.category = data[2];
        this.descrtiption = data[3];
        this.picture = data[4];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescrtiption() {
        return descrtiption;
    }

    public void setDescrtiption(String descrtiption) {
        this.descrtiption = descrtiption;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.id, this.recipe, this.category, this.descrtiption, this.picture
        });
    }

    public static final Creator CREATOR = new Creator() {

        @Override
        public Recipe createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Recipe[size];
        }

    };

}
