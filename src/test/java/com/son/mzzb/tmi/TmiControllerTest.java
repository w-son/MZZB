package com.son.mzzb.tmi;

import com.son.mzzb.common.BaseControllerTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TmiControllerTest extends BaseControllerTest {

    @Before
    public void setUp() {
        matzipRepository.deleteAll();
        tmiRepository.deleteAll();
        IntStream.range(0, 10).forEach(this::insertTmi);
    }

    @Test
    @TestDescription("Tmi 정보 생성하기")
    public void createTmi() throws Exception {
        // given
        TmiDto tmiDto = TmiDto.builder().content("some Tmi content").build();

        // when & then
        mockMvc.perform(post("/api/v1/tmi")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(tmiDto)))
                .andDo(print())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.Tmi-All").exists())
                .andExpect(jsonPath("_links.Tmi-Random").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(status().isCreated())
                .andDo(document("Create-Tmi",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("content").description("Tmi 정보")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Tmi 데이터 id"),
                                fieldWithPath("content").description("Tmi 정보"),
                                fieldWithPath("_links.self.href").description("생성한 Tmi 조회 링크"),
                                fieldWithPath("_links.Tmi-All.href").description("Tmi 정보 리스트 조회 링크"),
                                fieldWithPath("_links.Tmi-Random.href").description("무작위 Tmi 데이터 조회 링크"),
                                fieldWithPath("_links.profile.href").description("Tmi API 문서 링크")
                        )
                ));
    }

    @Test
    @TestDescription("비어 있는 Tmi 정보 생성하기")
    public void createEmptyTmi() throws Exception {
        // given
        TmiDto tmiDto = new TmiDto();

        // when & then
        mockMvc.perform(post("/api/v1/tmi")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(tmiDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("Tmi 정보 조회")
    public void getTmis() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/tmi")
                        .param("page", "1")
                        .param("size", "2")
                        .param("sort", "content,ASC"))
                .andDo(print())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.first").exists())
                .andExpect(jsonPath("_links.last").exists())
                .andExpect(status().isOk())
                .andDo(document("Get-Tmi",
                        requestParameters(
                                parameterWithName("page").description("Tmi 데이터의 페이지 인덱스"),
                                parameterWithName("size").description("Tmi 데이터 페이지의 크기"),
                                parameterWithName("sort").description("필요에 따라 정렬 조건을 적용")
                        )
                ));
    }

    @Test
    @TestDescription("Tmi 단건 랜덤 조회")
    public void getRandomTmi() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/tmi/random"))
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.Tmi-All").exists())
                .andExpect(jsonPath("_links.Tmi-Random").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(status().isOk())
                .andDo(document("Get-Tmi-Random",
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("무작위 Tmi 데이터 id"),
                                fieldWithPath("content").description("무작위 Tmi 정보"),
                                fieldWithPath("_links.self.href").description("현재 Tmi 조회 링크"),
                                fieldWithPath("_links.Tmi-All.href").description("Tmi 정보 리스트 조회 링크"),
                                fieldWithPath("_links.Tmi-Random.href").description("무작위 Tmi 데이터 조회 링크"),
                                fieldWithPath("_links.profile.href").description("Tmi API 문서 링크")
                        )
                ));
    }

    private Tmi insertTmi(int i) {
        Tmi tmi = generateTmi(i);
        return tmiService.save(tmi);
    }

    private Tmi generateTmi(int i) {
        return Tmi.builder()
                .content(i + 1 + "번째 Tmi")
                .build();
    }

}