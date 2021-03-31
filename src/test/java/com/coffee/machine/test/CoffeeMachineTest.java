package com.coffee.machine.test;

import com.coffee.machine.BeverageRequestStatus;
import com.coffee.machine.CoffeeMachine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoffeeMachineTest {
    private CoffeeMachine coffeeMachine;
    @Before
    public void setup() throws IOException {
        coffeeMachine = TestHelper.initTest(Constants.INPUT_FILE_PATH+"input.json");
    }

    @Test
    public void allBeveragePrepared() throws IOException, ExecutionException, InterruptedException {
        List<BeverageRequestStatus> beverageStatuses = coffeeMachine.prepareBeverage(Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea"));
        assert (beverageStatuses.isEmpty() == Boolean.FALSE);
        for (BeverageRequestStatus beverageStatus : beverageStatuses) {
            assert (beverageStatus.isPrepared() == Boolean.TRUE);
        }
    }

    @Test
    public void allBeveragePreparedMaterialNotAddedForOne() throws IOException, ExecutionException, InterruptedException {
        List<BeverageRequestStatus> beverageStatuses = coffeeMachine.prepareBeverage(Arrays.asList("hot_tea", "hot_coffee", "black_tea", "green_tea","elaichi_tea"));
        assert (beverageStatuses.isEmpty() == Boolean.FALSE);
        for (BeverageRequestStatus beverageStatus : beverageStatuses) {
            if(beverageStatus.getBeverageName().equals("elaichi_tea")){
                assert (beverageStatus.isPrepared() == Boolean.FALSE);
            }else
            assert (beverageStatus.isPrepared() == Boolean.TRUE);
        }
    }

    @After
    public void after() throws InterruptedException {
        coffeeMachine.shutdown();
    }
}
