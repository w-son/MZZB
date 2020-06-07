package com.son.mzzb.tmi;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TmiService {

    private final TmiRepository tmiRepository;

    public Tmi save(Tmi tmi) {
        return tmiRepository.save(tmi);
    }

    public List<Tmi> findAll() {
        return tmiRepository.findAll();
    }

    public Page<Tmi> findAllByPage(Pageable pageable) {
        return tmiRepository.findAll(pageable);
    }

    public Optional<Tmi> findById(Integer id) {
        return tmiRepository.findById(id);
    }

    public Tmi findOneRandom() {
        /*
         난수(0~1)를 생성한 다음에 전체 Tmi 개수에 곱한 값을 페이지 인덱스로 정한다
         페이지 인덱스, 페이지 크기(1) 으로 무작위로 하나의 Tmi 인스턴스를 리턴한다
         */
        long count = tmiRepository.countAll();
        double rnum = Math.random();
        if(rnum == 1) rnum = 0;

        int idx = (int) (rnum * count);
        PageRequest request = PageRequest.of(idx, 1);
        Page<Tmi> pages = tmiRepository.findAll(request);
        Tmi tmi = null;
        if(pages.hasContent()) {
            tmi = pages.getContent().get(0);
        }
        return tmi;
    }

}
