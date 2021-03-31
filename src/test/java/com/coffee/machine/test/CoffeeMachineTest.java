package com.coffee.machine.test;

import com.coffee.machine.BeverageRequestStatus;
import com.coffee.machine.CoffeeMachine;
import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoffeeMachineTest {
    private CoffeeMachine coffeeMachine;

    @Test
    public void allBeveragePrepared() throws IOException, ExecutionException, InterruptedException {
        coffeeMachine = TestHelper.initTest(Constants.INPUT_FILE_PATH+"input.json");
        List<BeverageRequestStatus> beverageStatuses = coffeeMachine.prepareBeverage(Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea"));
        assert (beverageStatuses.isEmpty() == Boolean.FALSE);
        for (BeverageRequestStatus beverageStatus : beverageStatuses) {
            assert (beverageStatus.isPrepared() == Boolean.TRUE);
        }
    }

    @Test
    public void noBeveragePrepared() throws IOException, ExecutionException, InterruptedException {
        coffeeMachine = TestHelper.initTest(Constants.INPUT_FILE_PATH+"input2.json");
        List<BeverageRequestStatus> beverageStatuses = coffeeMachine.prepareBeverage(Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea"));
        assert (beverageStatuses.isEmpty() == Boolean.FALSE);
        for (BeverageRequestStatus beverageStatus : beverageStatuses) {
            assert (beverageStatus.isPrepared() == Boolean.FALSE);
        }
    }

    @Test
    public void allBeveragePreparedMaterialNotAddedForOne() throws IOException, ExecutionException, InterruptedException {
        coffeeMachine = TestHelper.initTest(Constants.INPUT_FILE_PATH+"input.json");
        List<BeverageRequestStatus> beverageStatuses = coffeeMachine.prepareBeverage(Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea","elaichi_tea"));
        assert (beverageStatuses.isEmpty() == Boolean.FALSE);
        for (BeverageRequestStatus beverageStatus : beverageStatuses) {
            if(beverageStatus.getBeverageName().equals("elaichi_tea")){
                assert (beverageStatus.isPrepared() == Boolean.FALSE);
            }else
            assert (beverageStatus.isPrepared() == Boolean.TRUE);
        }
    }

    @Test
    public void materialExhaustedAfterFewRequest() throws IOException, ExecutionException, InterruptedException {
        coffeeMachine = TestHelper.initTest(Constants.INPUT_FILE_PATH+"input3.json");
        List<BeverageRequestStatus> beverageStatuses = coffeeMachine.prepareBeverage(Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea"));
        assert (beverageStatuses.isEmpty() == Boolean.FALSE);
        int prepared =0;
        int notPrepared =0;

        for (BeverageRequestStatus beverageStatus : beverageStatuses) {
            if(beverageStatus.isPrepared()){
                prepared++;
            }else{
                notPrepared++;
            }
        }
        assert (prepared >0);
        assert (notPrepared >0);
    }

    @After
    public void after() throws InterruptedException {
        coffeeMachine.shutdown();
    }
}
