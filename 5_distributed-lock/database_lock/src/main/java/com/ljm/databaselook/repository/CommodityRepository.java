package com.ljm.databaselook.repository;

import com.ljm.databaselook.model.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommodityRepository extends JpaRepository<Commodity, Integer> {

    Commodity findByCommodityName(String name);
}
