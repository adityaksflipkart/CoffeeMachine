package com.coffee.machine.test;

import com.coffee.machine.CoffeeMachine;
import com.coffee.machine.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TestHelper {

    public static CoffeeMachine initTest(String inputFile) throws IOException {
        File file = new File(inputFile);
        Map input = new ObjectMapper().readValue(file, Map.class);
        Map machine = (Map)input.get(com.coffee.machine.Constants.MACHINE_KEY);
        int outlets = (Integer) ((Map) machine.get(com.coffee.machine.Constants.OUTLETS_KEY)).get(com.coffee.machine.Constants.COUNT_KEY);
        CoffeeMachine coffeeMachine = new CoffeeMachine(outlets);
        Map<String, Integer> total_items_quantity = (Map<String, Integer>) machine.get(com.coffee.machine.Constants.TOTAL_ITEMS_QUANTITY_KEY);
        coffeeMachine.addRawMaterials(total_items_quantity);
        Map beverages = (Map) machine.get(Constants.BEVERAGES_KEY);
        beverages.
                entrySet()
                .stream()
                .forEach(x->{
                    String beverage = (String) ((Map.Entry)x).getKey();
                    Map<String,Integer> ingredient = (Map<String,Integer>) ((Map.Entry)x).getValue();
                    coffeeMachine.addIngredient(beverage, ingredient);
                });
        return coffeeMachine;
    }
}
