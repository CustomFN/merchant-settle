package com.z.merchantsettle;

import com.alibaba.fastjson.JSON;
import com.z.merchantsettle.modules.base.controller.UiController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UiTest {

    @Autowired
    private UiController uiController;

    @Test
    public void testUi() {
//        Object provinces = uiController.getProvinces();
        Object categories = uiController.getCategories();
        System.out.println("===== " + JSON.toJSONString(categories));
//        uiController.getCities(11);
//        uiController.getBanks(1101);
//        uiController.getSubBanks(1001);
    }
}
