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
            if(getRawMaterials().containsKey(material)) {
                getRawMaterials().put(material, getRawMaterials().get(material) + quantity);
            }else{
                getRawMaterials().put(material,quantity);
            }
        }
    }
    /**
     *  we check if all the ingredient required for beverage is present , if present then we check the quantity of each ingredient
     *  greater then required to prepare beverage .
     * If all checks pass we allocate the ingredient to beverage
     */

    public  BeverageRequestStatus removeRawMaterialToPrepare(String beverage,Map<String,Integer> ingredient) throws InterruptedException {
        synchronized (this){
            for (Map.Entry<String, Integer> ingr : ingredient.entrySet()) {
                if(!rawMaterials.containsKey(ingr.getKey())){
                    return new BeverageRequestStatus(beverage,beverage+" cannot be prepared because "+ingr.getKey()+" is not available",false);
                }
                if(rawMaterials.get(ingr.getKey())<ingr.getValue()){
                    return new BeverageRequestStatus(beverage,beverage+" cannot be prepared because "+ingr.getKey()+" is not available",false);
                }
            }
            for (Map.Entry<String, Integer> ingr : ingredient.entrySet()) {
                rawMaterials.put(ingr.getKey(),rawMaterials.get(ingr.getKey())-ingr.getValue());
            }
            return new BeverageRequestStatus(beverage,beverage+" ingredients available",true);
        }
    }

}
