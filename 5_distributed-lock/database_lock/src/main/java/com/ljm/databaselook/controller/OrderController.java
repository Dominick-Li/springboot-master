package com.ljm.databaselook.controller;

import com.ljm.databaselook.model.Commodity;
import com.ljm.databaselook.repository.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class OrderController {

    @Autowired
    private CommodityRepository commodityRepository;

    @RequestMapping("/deduct_stock")
    public String deductStock() {
        Optional<Commodity> commodityOptional = commodityRepository.findById(1);
        if(!commodityOptional.isPresent()){
            return "商品编号不存在!";
        }
        Commodity commodity=commodityOptional.get();
        if (commodity.getNumber() > 0) {
            commodity.setNumber(commodity.getNumber() - 1);
            commodityRepository.save(commodity);
            System.out.println("抢购成功,剩余库存:" + commodity.getNumber()+",Thrad Id"+Thread.currentThread().getId());
            return "success";
        } else {
            System.out.println("剩余库存不足,抢购失败!");
            return "error";
        }
    }
}
