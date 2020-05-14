package com.son.mzzb.matzip;

import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/matzip", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class MatzipController {

    private final MatzipService matzipService;

    @GetMapping
    public ResponseEntity getMatzips() {
        List<Matzip> matzips = matzipService.findAll();
        return ResponseEntity.ok(matzips);
    }

    @GetMapping("/{id}")
    public ResponseEntity getMatzipById(@PathVariable("id") Integer id) {
        Optional<Matzip> matzip = matzipService.findOne(id);
        return ResponseEntity.ok(matzip);
    }

}
