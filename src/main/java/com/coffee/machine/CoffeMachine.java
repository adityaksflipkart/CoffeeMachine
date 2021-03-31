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
   private ExecutorCompletionService<BeverageRequestStatus> executorCompletionService;

   public CoffeMachine(int outlets) {
      this.outlets = outlets;
      beverageIngredient = new BeverageIngredient();
      rawMaterial = new RawMaterial();
      executorService = Executors.newFixedThreadPool(outlets);
      executorCompletionService=new ExecutorCompletionService<BeverageRequestStatus>(executorService);
   }

   public  List<BeverageRequestStatus> prepareBeverage(List<String> beverages) throws ExecutionException, InterruptedException {
       List<BeverageRequestStatus> prepareStatus=new ArrayList<>();
       beverages
               .stream()
               .map(beverageName->new Callable<BeverageRequestStatus>() {
                   @Override
                   public BeverageRequestStatus call() throws Exception {
                       if(!beverageIngredient.getIngredients().containsKey(beverageName)){
                           return new BeverageRequestStatus(beverageName,"beverage prepration failed as "+beverageName+" not in Menu",false);
                       }
                       Map<String, Integer> ingredient = beverageIngredient.getIngredients().get(beverageName);
                       BeverageRequestStatus beverageRequestStatus = rawMaterial.removeRawMaterialToPrepare(beverageName, ingredient);
                       if(beverageRequestStatus.isPrepared())
                           return prepare(beverageName);
                       return beverageRequestStatus;
                   }
               }).forEach(beverage->executorCompletionService.submit(beverage));
       Future<Boolean> preparedBeverage=null;

       beverages.forEach(x->{
           try {
               Future<BeverageRequestStatus> take = executorCompletionService.take();
               prepareStatus.add(take.get());
           } catch (Exception e) {
               e.printStackTrace();
           }
       });
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

    public BeverageRequestStatus prepare(String beverageName){
        return new BeverageRequestStatus(beverageName,beverageName+" is prepared",true);
    }
}
