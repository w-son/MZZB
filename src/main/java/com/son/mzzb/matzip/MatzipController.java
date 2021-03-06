package com.son.mzzb.matzip;

import com.son.mzzb.common.ErrorsResource;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/v1/matzip", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class MatzipController {

    private final MatzipService matzipService;
    private final MatzipDtoValidator matzipDtoValidator;
    private final MatzipParamsValidator matzipParamsValidator;
    private final ModelMapper modelMapper;

    /*
     * 요청 수신
       - Dto를 통해 RequestBody의 내용을 Validate(@Valid 이나 Validator로)시킨 후에
         로직을 수행하고 정상 처리 된 경우에만 수행하도록 한다

     * 응답 송신
       1) ResponseEntity의 응답 종류 : 200류 400류
         - 생성 요청이었을 경우 : self 링크 제공
         - 조회 요청이었을 경우 : update 링크 제공
       2) Body에 content를 담아서 리턴할 때
         -> java bean 스펙을 준수하는 클래스(도메인 객체들이나 DTO 객체들)은 serializing이 자동으로 되지만
            errors객체는 그렇지 않기 때문에 ErrorsSerializer과 같은 클래스를 @JsonComponent로 등록해야함
       3) Resource로 최종 데이터를 감싸주고 리턴 해주자 : JsonUnwrapped, links 용이
         - 도메인형, 페이지형, 에러형
         - Resources로 리스트형 Resource 클래스 묶기 : 일반 리스트로 리턴시 Map 구조가 아닌 그냥 리스트, 확장성 부족
                                                 TODO : 불필요하게 키값으로 매핑되어있는 구조는 어떻게 해야할까
         - PagedResources : assembler를 통해 page 정보(크기, 현재위치, 처음, 마지막 등) 및 이동 url을 링크형태로 제공
                            Page객체를 Resources로 매핑시켜주는 역할이라고 생각하면 된다

       링크들
       1) create : self, profile, matzips-all, matzips-bingo, matzips-conditions
       2) 단건 read : self, profile, matzips-all, matzips-bingo, matzips-conditions
          빙고 read : 엔티티 각각 self, 페이지 관련 링크
          조건 read : 엔티티 각각 self, profile, matzips-all, matzips-bingo, matzips-conditions
       3) update : self, profile, matzips-all, matzips-bingo, matzips-conditions

     */

    // Create
    @PostMapping
    public ResponseEntity createMatzip(@RequestBody @Valid MatzipDto matzipDto, Errors errors) {
        // Validation 처리
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }
        /* Deprecated
        matzipDtoValidator.validate(matzipDto, errors);
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }
        */

        // 추가 후 self 링크 생성
        Matzip matzip = modelMapper.map(matzipDto, Matzip.class);
        matzipService.save(matzip);
        ControllerLinkBuilder self = linkTo(MatzipController.class).slash(matzip.getId());
        URI uri = self.toUri();

        MatzipResource matzipResource = new MatzipResource(matzip);
        matzipResource.add(linkTo(MatzipController.class).withRel("Matzip-All"));
        matzipResource.add(linkTo(MatzipController.class).slash("page").withRel("Matzip-Bingo"));
        matzipResource.add(linkTo(MatzipController.class).slash("conditions").withRel("Matzip-Conditions"));
        matzipResource.add(new Link("/docs/index.html#resources-matzip-create").withRel("profile"));
        return ResponseEntity.created(uri).body(matzipResource);
    }

    // Read
    @GetMapping
    public ResponseEntity getMatzips() {
        List<Matzip> matzips = matzipService.findAll();
        List<MatzipResource> matzipResources = new ArrayList<>();
        for(Matzip matzip : matzips) {
            matzipResources.add(new MatzipResource(matzip));
        }

        Resources<MatzipResource> resources = new Resources<>(matzipResources);
        resources.add(linkTo(MatzipController.class).withRel("Matzip-All"));
        resources.add(linkTo(MatzipController.class).slash("page").withRel("Matzip-Bingo"));
        resources.add(linkTo(MatzipController.class).slash("conditions").withRel("Matzip-Conditions"));
        return ResponseEntity.ok(resources);
    }

    // Read
    @GetMapping("/page")
    public ResponseEntity getMatzipsByPage(Pageable pageable,
                                           PagedResourcesAssembler<Matzip> assembler) {
        Page<Matzip> matzips = matzipService.findAllByPage(pageable);
        PagedResources<MatzipResource> pagedResources = assembler.toResource(matzips, m -> new MatzipResource(m));
        return ResponseEntity.ok(pagedResources);
    }

    // Read
    @GetMapping("/conditions")
    public ResponseEntity getMatzipsByConditions(@Valid @ModelAttribute MatzipParams matzipParams, Errors errors) {

        /*
         Validation과정에 errors를 담기 위해서
         Query String을 @RequestParam 형태로 받지 않고
         MatzipParams에 받았음
         */
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        /* Deprecated
        matzipParamsValidator.validate(matzipParams, errors);
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }
        */

        String foodType = matzipParams.getFoodType();
        Integer price = matzipParams.getPrice();
        List<Matzip> matzips = matzipService.findAllByConditions(foodType, price);
        List<MatzipResource> matzipResources = new ArrayList<>();
        for(Matzip matzip : matzips) {
            matzipResources.add(new MatzipResource(matzip));
        }

        Resources<MatzipResource> resources = new Resources<>(matzipResources);
        resources.add(linkTo(MatzipController.class).withRel("Matzip-All"));
        resources.add(linkTo(MatzipController.class).slash("page").withRel("Matzip-Bingo"));
        resources.add(linkTo(MatzipController.class).slash("conditions").withRel("Matzip-Conditions"));
        resources.add(new Link("/docs/index.html#resources-get-matzip-by-conditions").withRel("profile"));
        return ResponseEntity.ok(resources);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity getMatzipById(@PathVariable("id") Integer id) {
        Optional<Matzip> optionalMatzip = matzipService.findOne(id);
        if(optionalMatzip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Matzip matzip = optionalMatzip.get();
        MatzipResource matzipResource = new MatzipResource(matzip);
        matzipResource.add(linkTo(MatzipController.class).withRel("Matzip-All"));
        matzipResource.add(linkTo(MatzipController.class).slash("page").withRel("Matzip-Bingo"));
        matzipResource.add(linkTo(MatzipController.class).slash("conditions").withRel("Matzip-Conditions"));
        matzipResource.add(new Link("/docs/index.html#resources-matzip-get").withRel("profile"));
        return ResponseEntity.ok(matzipResource);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity updateMatzip(@PathVariable("id") Integer id,
                                       @RequestBody @Valid MatzipDto matzipDto,
                                       Errors errors) {

        Optional<Matzip> optionalMatzip = matzipService.findOne(id);
        if(optionalMatzip.isEmpty()) { // empty result
            return ResponseEntity.notFound().build();
        }
        if(errors.hasErrors()) { // annotation validating
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        /* Deprecated
        matzipDtoValidator.validate(matzipDto, errors);
        if(errors.hasErrors()) { // customized validating
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }
        */

        /* Update에 관한 간략한 note
         조회하는 로직을 여기서 썼기 때문에 Service 계층에 따로 update 로직을 짜지 않았음
         optionalMatzip에 해당하는 id의 엔티티가 이미 Persistence Context에 올라와 있고
         update 시킬 Matzip 정보를 그 id로 save 시키면
         Repository단에서 Transaction이 끝날 시에 DB에 반영될 것이기 때문
         */
        Matzip matzip = optionalMatzip.get();
        modelMapper.map(matzipDto, matzip);
        Matzip updated = matzipService.save(matzip);

        MatzipResource matzipResource = new MatzipResource(updated);
        matzipResource.add(linkTo(MatzipController.class).withRel("Matzip-All"));
        matzipResource.add(linkTo(MatzipController.class).slash("page").withRel("Matzip-Bingo"));
        matzipResource.add(linkTo(MatzipController.class).slash("conditions").withRel("Matzip-Conditions"));
        matzipResource.add(new Link("/docs/index.html#resources-matzip-update").withRel("profile"));
        return ResponseEntity.ok(matzipResource);
    }

}
