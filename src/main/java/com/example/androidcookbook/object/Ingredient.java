package com.example.androidcookbook.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private String ing_id;
    private String ingredient;
    private String mu;
    private String kcal;

    public Ingredient() {
        super();
    }

    public Ingredient(String in, String mu, String kcal) {
        this.ingredient = in;
        this.mu = mu;
        this.kcal = kcal;
    }

    public Ingredient(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        this.ing_id = data[0];
        this.ingredient = data[1];
        this.mu = data[2];
        this.kcal = data[3];
    }


    public Ingredient(String ing_id2, String ingU, String muU, String kcal) {
        this.ing_id = ing_id2;
        this.ingredient = ingU;
        this.mu = muU;
        this.kcal = kcal;
    }

    public String getIng_id() {
        return ing_id;
    }

    public void setIng_id(String ing_id) {
        this.ing_id = ing_id;
    }

    public String getIngreident() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getmMu() {
        return mu;
    }

    public void setMu(String mu) {
        this.mu = mu;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.ing_id, this.ingredient, this.mu, this.kcal
        });
    }

    public static final Creator CREATOR = new Creator() {

        @Override
        public Ingredient createFromParcel(Parcel in) {
            // TODO Auto-generated method stub
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Ingredient[size];
        }

    };

}
