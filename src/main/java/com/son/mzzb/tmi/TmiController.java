package com.son.mzzb.tmi;

import com.son.mzzb.common.ErrorsResource;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/v1/tmi", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class TmiController {
    /*
       관련 내용은 모두 MatzipController에 주석으로 설명
       TODO Rest Docs 적용

       링크들
       1) create : self, profile, tmi-all, tmi-random
       2) 랜덤 read : self, profile, tmi-all, tmi-random
          리스트 read : 엔티티 각각 self, 페이지 관련 링크
     */
    private final TmiService tmiService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createTmi(@RequestBody @Valid TmiDto tmiDto, Errors errors) {
        // Validation 처리
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        Tmi tmi = modelMapper.map(tmiDto, Tmi.class);
        tmiService.save(tmi);
        ControllerLinkBuilder self = linkTo(TmiController.class).slash(tmi.getId());
        URI uri = self.toUri();

        TmiResource tmiResource = new TmiResource(tmi);
        tmiResource.add(linkTo(TmiController.class).withRel("Tmi-All"));
        tmiResource.add(linkTo(TmiController.class).slash("random").withRel("Tmi-Random"));
        tmiResource.add(new Link("/docs/index.html#resources-tmi-create").withRel("profile"));
        return ResponseEntity.created(uri).body(tmiResource);
    }

    @GetMapping
    public ResponseEntity getTmis(Pageable pageable,
                                  PagedResourcesAssembler<Tmi> assembler) {
        Page<Tmi> tmis = tmiService.findAllByPage(pageable);
        PagedResources<TmiResource> pagedResources = assembler.toResource(tmis, t -> new TmiResource(t));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTmiById(@PathVariable("id") Integer id) {
        Optional<Tmi> optionalTmi = tmiService.findById(id);
        if(optionalTmi.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TmiResource tmiResource = new TmiResource(optionalTmi.get());
        tmiResource.add(linkTo(TmiController.class).withRel("Tmi-All"));
        tmiResource.add(linkTo(TmiController.class).slash("random").withRel("Tmi-Random"));
        return ResponseEntity.ok(tmiResource);
    }

    @GetMapping("/random")
    public ResponseEntity getRandomTmi() {
        Tmi randomTmi = tmiService.findOneRandom();
        if(randomTmi == null) {
            return ResponseEntity.notFound().build();
        }

        TmiResource tmiResource = new TmiResource(randomTmi);
        tmiResource.add(linkTo(TmiController.class).withRel("Tmi-All"));
        tmiResource.add(linkTo(TmiController.class).slash("random").withRel("Tmi-Random"));
        tmiResource.add(new Link("/docs/index.html#resources-tmi-get").withRel("profile"));
        return ResponseEntity.ok(tmiResource);
    }

}
