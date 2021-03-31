package com.coffee.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class CoffeeMachine {
   private int outlets;
   //use for string the ingridient for each bevarage
   private BeverageIngredient beverageIngredient;
   //sotre the raw material , and exposes method to get the raw items
   private RawMaterial rawMaterial;
   //represents the number of outlet for machine, which can work in parallel
   private ExecutorService executorService;
   private ExecutorCompletionService<BeverageRequestStatus> executorCompletionService;

   public CoffeeMachine(int outlets) {
      this.outlets = outlets;
      beverageIngredient = new BeverageIngredient();
      rawMaterial = new RawMaterial();
      executorService = Executors.newFixedThreadPool(this.outlets);
      executorCompletionService=new ExecutorCompletionService<BeverageRequestStatus>(executorService);
   }
   /*to prepare beverage we create callable for each request beverage and in parallel execute them on N outlets
      IN each callable we get the ingridient from   rawMaterial and then call prepare for the outlet
    */
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
    //addition of raw material
   public void addRawMaterials(Map<String,Integer> rawMaterials){
       for (Map.Entry<String, Integer> materials : rawMaterials.entrySet()) {
           rawMaterial.addMaterial(materials.getKey(),materials.getValue());
       }
    }
    //addition of ingridient
    public void addIngredient(String beverage,Map<String,Integer> ingredient){
       beverageIngredient.getIngredients().put(beverage, ingredient);
    }
    //shutting down of machine
    public void shutdown() throws InterruptedException {
       this.executorService.shutdown();
       this.executorService.awaitTermination(10l,TimeUnit.MINUTES);
    }

    private BeverageRequestStatus prepare(String beverageName){
        return new BeverageRequestStatus(beverageName,beverageName+" is prepared",true);
    }
}
