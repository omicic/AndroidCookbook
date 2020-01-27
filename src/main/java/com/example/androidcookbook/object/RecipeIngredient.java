package com.example.androidcookbook.object;

public class RecipeIngredient {

    private String ing;
    private String mu;
    private String qua;

    public RecipeIngredient(String ing, String mu, String gua) {
        this.ing = ing;
        this.mu = mu;
        this.qua = qua;
    }

    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public String getMu() {
        return mu;
    }

    public void setMu(String mu) {
        this.mu = mu;
    }

    public String getQua() {
        return qua;
    }

    public void setQua(String qua) {
        this.qua = qua;
    }


}
