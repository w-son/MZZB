package com.son.mzzb.matzip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatzipRepository extends JpaRepository<Matzip, Integer> {

    // Query annotation으로 조건식을 처리
    @Query("select m from Matzip m where m.foodType = :foodType and m.price = :price")
    List<Matzip> findOptionsMatzip(@Param("foodType") String foodType, @Param("price") Integer price);

    /*
     Method name으로 조건식을 처리
     Controller에서 price 관련 validation을 거치기 때문에 String으로 비교해도 무방
     근데 메서드 이름이 너무 길다...
     같은 기능으로 findConditionsMatzip 메서드 생성
     */
    List<Matzip> findMatzipByFoodTypeAndPriceLessThanEqual(String foodType, String price);

    @Query("select m from Matzip m where m.foodType = :foodType and m.price <= :price")
    List<Matzip> findConditionsMatzip(@Param("foodType") String foodType, @Param("price") Integer price);

}
