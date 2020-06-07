package com.son.mzzb.tmi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TmiRepository extends JpaRepository<Tmi, Integer> {


    @Query("select count(t) from Tmi t")
    long countAll();

}
