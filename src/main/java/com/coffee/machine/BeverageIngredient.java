package com.coffee.machine;

import java.util.HashMap;
import java.util.Map;

public class BeverageIngredient {
    private Map<String,Map<String,Integer>> ingredients;

    public BeverageIngredient() {
        ingredients=new HashMap<>();
    }

    public Map<String, Map<String, Integer>> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Map<String, Integer>> ingredients) {
        this.ingredients = ingredients;
    }
}
