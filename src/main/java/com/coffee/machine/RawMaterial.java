package com.coffee.machine;

import java.util.HashMap;
import java.util.Map;

public class RawMaterial {
    private Map<String,Integer> rawMaterials;

    public RawMaterial() {
        rawMaterials=new HashMap<>();
    }
    private Map<String, Integer> getRawMaterials() {
        return rawMaterials;
    }

    public void addMaterial(String material,int quantity){
        synchronized (this) {
            getRawMaterials().merge(material, quantity, (x, y) -> x + y);
        }
    }
    public  boolean removeRawMaterialToPrepare(String beverage,Map<String,Integer> ingredient) throws InterruptedException {
        synchronized (this){
            for (Map.Entry<String, Integer> ingr : ingredient.entrySet()) {
                if(!rawMaterials.containsKey(ingr.getKey())){
                    System.out.println(beverage+" cannot be prepared because "+ingr.getKey()+" is not available");
                    return false;
                }
                if(rawMaterials.get(ingr.getKey())<ingr.getValue()){
                    System.out.println(beverage+" cannot be prepared because "+ingr.getKey()+" is not available");
                    return false;
                }
            }
            for (Map.Entry<String, Integer> ingr : ingredient.entrySet()) {
                rawMaterials.merge(ingr.getKey(),ingr.getValue(),(x,y)->x-y);
            }
            return true;
        }
    }

}
