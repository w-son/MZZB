package com.son.mzzb.tmi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TmiService {

    private final TmiRepository tmiRepository;

    public Tmi save(Tmi tmi) {
        return tmiRepository.save(tmi);
    }

    public List<Tmi> findAll() {
        return tmiRepository.findAll();
    }

    public Optional<Tmi> findById(Integer id) {
        return tmiRepository.findById(id);
    }

}
