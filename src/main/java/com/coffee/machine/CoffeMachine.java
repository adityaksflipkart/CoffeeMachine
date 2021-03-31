package com.coffee.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class CoffeMachine {
   private int outlets;
   private BeverageIngredient beverageIngredient;
   private RawMaterial rawMaterial;
   private ExecutorService executorService;
   private ExecutorCompletionService<Boolean> executorCompletionService;

   public CoffeMachine(int outlets) {
      this.outlets = outlets;
      beverageIngredient = new BeverageIngredient();
      rawMaterial = new RawMaterial();
      executorService = Executors.newFixedThreadPool(outlets);
      executorCompletionService=new ExecutorCompletionService<Boolean>(executorService);
   }

   public  List<Boolean> prepareBeverage(List<String> beverages) throws ExecutionException, InterruptedException {
       List<Boolean> prepareStatus=new ArrayList<>();
       beverages
               .stream()
               .map(beverageName->new Callable<Boolean>() {
                   @Override
                   public Boolean call() throws Exception {
                       if(!beverageIngredient.getIngredients().containsKey(beverageName)){
                           System.out.println("beverage prepration failed as "+beverageName+" not in Menu");
                           return false;
                       }
                       Map<String, Integer> ingredient = beverageIngredient.getIngredients().get(beverageName);
                       boolean rawMaterialToPrepare = rawMaterial.removeRawMaterialToPrepare(beverageName, ingredient);
                       if(rawMaterialToPrepare)
                           return prepare(beverageName);
                       return false;
                   }
               }).forEach(beverage->executorCompletionService.submit(beverage));
       Future<Boolean> preparedBeverage=null;
       while((preparedBeverage=executorCompletionService.poll())!=null){
           Boolean status = preparedBeverage.get();
           prepareStatus.add(status);
           if(!status){
               System.out.println("beverage prepration failed!!!");
           }
       }
       return prepareStatus;
   }

   public void addRawMaterials(Map<String,Integer> rawMaterials){
       rawMaterials
               .entrySet()
               .stream()
               .forEach(x-> {
                     rawMaterial.addMaterial(x.getKey(),x.getValue());
                  });
    }

    public void addRawMaterial(String item,int quantity){
        rawMaterial.addMaterial(item,quantity);
    }

    public void addIngredient(String beverage,Map<String,Integer> ingredient){
       beverageIngredient.getIngredients().put(beverage, ingredient);
    }

    public int getOutlets() {
        return outlets;
    }

    public BeverageIngredient getBeverageIngredient() {
        return beverageIngredient;
    }

    public RawMaterial getRawMaterial() {
        return rawMaterial;
    }

    public void setOutlets(int outlets) {
        this.outlets = outlets;
    }

    public void setBeverageIngredient(BeverageIngredient beverageIngredient) {
        this.beverageIngredient = beverageIngredient;
    }

    public void setRawMaterial(RawMaterial rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public void shutdown() throws InterruptedException {
       this.executorService.shutdown();
       this.executorService.awaitTermination(10l,TimeUnit.MINUTES);
    }

    public boolean prepare(String beverageName){
        System.out.println(beverageName+" is prepared");
       return true;
    }
}
