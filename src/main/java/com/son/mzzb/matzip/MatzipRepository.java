package com.son.mzzb.matzip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatzipRepository extends JpaRepository<Matzip, Integer> {

    // findConditionalMatzip 과 같은 기능 수행
    List<Matzip> findMatzipByFoodTypeAndPrice(String foodType, String price);

    @Query("select m from Matzip m where m.foodType = :foodType and m.price = :price")
    List<Matzip> findConditionalMatzip(@Param("foodType") String foodType, @Param("price") String price);

}
