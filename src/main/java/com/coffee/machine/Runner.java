package com.coffee.machine;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Runner {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        CoffeMachine coffeMachine = Init(Constants.INPUT_FILE);
        List<BeverageRequestStatus> status = coffeMachine.prepareBeverage(Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea"));
        status.stream().forEach(x-> System.out.println("status "+x.isPrepared()+" "+x.getMessage()));
        coffeMachine.shutdown();
    }
    public static CoffeMachine  Init(String inoutFile) throws IOException {
        File file = new File(inoutFile);
        Map input = new ObjectMapper().readValue(file, Map.class);
        Map machine = (Map)input.get(Constants.MACHINE_KEY);
        int outlets = (Integer) ((Map) machine.get(Constants.OUTLETS_KEY)).get(Constants.COUNT_KEY);
        CoffeMachine coffeMachine = new CoffeMachine(outlets);
        Map<String, Integer> total_items_quantity = (Map<String, Integer>) machine.get(Constants.TOTAL_ITEMS_QUANTITY_KEY);
        coffeMachine.addRawMaterials(total_items_quantity);
        Map beverages = (Map) machine.get(Constants.BEVERAGES_KEY);
        beverages.
                entrySet()
                .stream()
                .forEach(x->{
                    String beverage = (String) ((Map.Entry)x).getKey();
                    Map<String,Integer> ingredient = (Map<String,Integer>) ((Map.Entry)x).getValue();
                    coffeMachine.addIngredient(beverage, ingredient);
                });
        return coffeMachine;
    }
}
