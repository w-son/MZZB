package com.son.mzzb.tmi;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/tmi", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class TmiController {

    private final TmiService tmiService;

    @GetMapping
    public ResponseEntity getTmis() {
        List<Tmi> tmis = tmiService.findAll();
        return ResponseEntity.ok(tmis);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTmiById(@PathVariable("id") Integer id) {
        Optional<Tmi> tmi = tmiService.findById(id);
        return ResponseEntity.ok(tmi);
    }

}
