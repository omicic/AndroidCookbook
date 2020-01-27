package com.example.androidcookbook.work;

import com.example.androidcookbook.object.Ingredient;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class MySQLPullParserIngredient extends DefaultHandler {
    boolean currentElement = false;
    String currentValue = "";

    //String cartId;
    String ingredientname;
    String mu;
    Ingredient ingredient;
    ArrayList<Ingredient> ingList = new ArrayList<Ingredient>();

    public String getIngredientname() {
        return ingredientname;
    }

    public void setIngredientname(String ingredientname) {
        this.ingredientname = ingredientname;
    }

    public String getMu() {
        return mu;
    }

    public void setMu(String mu) {
        this.mu = mu;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public ArrayList<Ingredient> getIngList() {
        return ingList;
    }

    public void setIngList(ArrayList<Ingredient> ingList) {
        this.ingList = ingList;
    }

    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = true;

        if (qName.equals("ingredient")) {
            ingredient = new Ingredient();
        }

    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        if (qName.equalsIgnoreCase("Mu")) {
            ingredient.setMu(currentValue.trim());
        } else if (qName.equalsIgnoreCase("Ingredientname")) {
            ingredient.setIngredient(currentValue.trim());
        } else if (qName.equalsIgnoreCase("ingredient")) {

            ingList.add(ingredient);

        }


        currentValue = "";
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (currentElement) {
            currentValue = currentValue + new String(ch, start, length);
        }

    }
}	