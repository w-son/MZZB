package com.son.mzzb.tmi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmiRepository extends JpaRepository<Tmi, Integer> {

    long countAll();

    // Page<Tmi> findAll(Pageable pageable);

}
