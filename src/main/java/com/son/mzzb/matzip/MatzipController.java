package com.son.mzzb.matzip;

import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/matzip", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class MatzipController {

    private final MatzipService matzipService;

    /*
     - ResponseEntity의 응답 종류 : 200류 400류
     - Resource로 최종 데이터를 감싸주고 리턴 해주자 : JsonUnwrapped, links 용이
     - Resources로 리스트형 Resource 클래스 묶기 : 일반 리스트로 리턴시 Map 구조가 아닌 그냥 리스트, 확장성 부족
       TODO : 불필요하게 키값으로 매핑되어있는 구조는 어떻게 해야할까
     - PagedResources : assembler를 통해 page 정보(크기, 현재위치, 처음, 마지막 등) 및 이동 url을 링크형태로 제공
                        Page객체를 Resources로 매핑시켜주는 역할이라고 생각하면 된다
     */

    @GetMapping
    public ResponseEntity getMatzips() {
        List<Matzip> matzips = matzipService.findAll();
        List<MatzipResource> matzipResources = new ArrayList<>();
        for(Matzip matzip : matzips) {
            matzipResources.add(new MatzipResource(matzip));
        }
        return ResponseEntity.ok(new Resources<>(matzipResources));
    }

    @GetMapping("/page")
    public ResponseEntity getMatzipsByPage(Pageable pageable,
                                           PagedResourcesAssembler<Matzip> assembler) {
        Page<Matzip> matzips = matzipService.findAllByPage(pageable);
        PagedResources<MatzipResource> pagedResources = assembler.toResource(matzips, m -> new MatzipResource(m));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getMatzipById(@PathVariable("id") Integer id) {
        Optional<Matzip> optionalMatzip = matzipService.findOne(id);
        if(optionalMatzip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Matzip matzip = optionalMatzip.get();
        MatzipResource matzipResource = new MatzipResource(matzip);
        return ResponseEntity.ok(matzipResource);
    }

}
