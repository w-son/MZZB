package com.son.mzzb.matzip;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatzipService {

    private final MatzipRepository matzipRepository;

    public Matzip save(Matzip matzip) {
        return matzipRepository.save(matzip);
    }

    public List<Matzip> findAll() {
        return matzipRepository.findAll();
    }

    public Page<Matzip> findAllByPage(Pageable pageable) { return matzipRepository.findAll(pageable); }

    public List<Matzip> findAllByConditions(String foodType, Integer price) {
        return matzipRepository.findConditionsMatzip(foodType, price);
    }

    public Optional<Matzip> findOne(Integer id) {
        return matzipRepository.findById(id);
    }

}
