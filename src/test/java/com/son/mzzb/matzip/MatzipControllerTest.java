package com.son.mzzb.matzip;

import com.son.mzzb.common.BaseControllerTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.swing.*;
import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class MatzipControllerTest extends BaseControllerTest {

    @Before
    public void setup() {
        /*
         matzipRepository.deleteAll();
         tmiRepository.deleteAll();

         Before가 메서드가 시작하기 전에 실행되는 거라서
         100개를 생성하는 로직이 메서드 10번에 모두 적용되면
         마지막에는 1000개가 생긴다

         + 모든 테스트를 같이 돌리면 이게 전부 공유가 된다
         따라서 MatzipRepositoryTest에서 Before로 5개를 생성해놓으면 여기 코드는 실행이 안된다
         */
        List<Matzip> check = matzipRepository.findAll();
        if(check.isEmpty()) {
            IntStream.range(0, 49).forEach(this::insertMatzip);
        }
    }

    @Test
    @TestDescription("맛집 정보 생성하기")
    public void createMatzip() throws Exception {
        // given
        MatzipDto matzipDto = MatzipDto.builder()
                .name("가미우동")
                .foodType("일식")
                .price(10000)
                .infoLink("some info link")
                .imgLink("some image link")
                .build();
        // when & then
        mockMvc.perform(post("/api/v1/matzip")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(matzipDto)))
                .andDo(print())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.Matzip-All").exists())
                .andExpect(jsonPath("_links.Matzip-Bingo").exists())
                .andExpect(jsonPath("_links.Matzip-Conditions").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(status().isCreated())
                .andDo(document("Create-Matzip",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("name").description("맛집 이름"),
                                fieldWithPath("foodType").description("음식 분류"),
                                fieldWithPath("price").description("가격대"),
                                fieldWithPath("infoLink").description("맛집 정보 링크"),
                                fieldWithPath("imgLink").description("맛집 이미지 링크")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("맛집 데이터 id"),
                                fieldWithPath("name").description("맛집 이름"),
                                fieldWithPath("foodType").description("음식 분류"),
                                fieldWithPath("price").description("가격대"),
                                fieldWithPath("infoLink").description("맛집 정보 링크"),
                                fieldWithPath("imgLink").description("맛집 이미지 링크"),
                                fieldWithPath("_links.self.href").description("맛집 단건 조회 및 수정이 가능한 링크"),
                                fieldWithPath("_links.Matzip-All.href").description("맛집 전체 조회 링크"),
                                fieldWithPath("_links.Matzip-Bingo.href").description("맛집 빙고 데이터 조회 링크"),
                                fieldWithPath("_links.Matzip-Conditions.href").description("조건을 포함하는 맛집 데이터 조회"),
                                fieldWithPath("_links.profile.href").description("Matzip API 문서 링크")
                        )
                ));
    }

    @Test
    @TestDescription("필수 입력 필드가 없는 경우")
    public void createEmptyMatzip() throws Exception {
        // given
        MatzipDto matzipDto = MatzipDto.builder()
                .name("가미우동")
                .foodType("일식")
                .build();
        // when & then
        mockMvc.perform(post("/api/v1/matzip")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(matzipDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 필드가 잘못된 경우")
    public void createWrongMatzip() throws Exception {
        // given
        MatzipDto matzipDto = MatzipDto.builder()
                .name("가미우동")
                .foodType("일식")
                .price(-1)
                .infoLink("some info link")
                .imgLink("some image link")
                .build();
        // when & then
        mockMvc.perform(post("/api/v1/matzip")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(matzipDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].object").exists())
                .andExpect(jsonPath("content[0].message").exists())
                .andExpect(jsonPath("content[0].code").exists());
    }

    @Test
    @TestDescription("맛집 정보 전체 조회")
    public void getMatzips() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.matzipList").exists())
                .andExpect(jsonPath("_links.Matzip-All.href").exists())
                .andExpect(jsonPath("_links.Matzip-Bingo.href").exists())
                .andExpect(jsonPath("_links.Matzip-Conditions.href").exists());
    }

    @Test
    @TestDescription("맛집 정보 페이지 단위로 조회")
    public void getMatzipsByPage() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/page")
                            .param("page", "1")
                            .param("size", "25")
                            .param("sort", "name,ASC"))
                .andDo(print())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.first").exists())
                .andExpect(jsonPath("_links.last").exists())
                .andExpect(status().isOk())
                .andDo(document("Get-Matzip-Bingo",
                            requestParameters(
                                    parameterWithName("page")
                                            .description("원하는 빙고 데이터 셋의 인덱스를 담는 파라미터입니다 [예시] 10개의 데이터에 size를 5으로 설정하면 2개의 page 인덱스(0, 1) 이 존재합니다"),
                                    parameterWithName("size")
                                            .description("맛집 빙고에 들어가는 데이터의 개수"),
                                    parameterWithName("sort")
                                            .description("필요에 따라 빙고 데이터 조회에 정렬 조건을 적용")
                            )
                ));
    }

    @Test
    @TestDescription("조건을 포함한 맛집 조회")
    public void getMatzipsByCondition() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/conditions")
                            .param("foodType", "foodType")
                            .param("price", "1004"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.matzipList").exists())
                .andExpect(jsonPath("_links.Matzip-All.href").exists())
                .andExpect(jsonPath("_links.Matzip-Bingo.href").exists())
                .andExpect(jsonPath("_links.Matzip-Conditions.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())
                .andDo(document("Get-Matzip-By-Conditions",
                        requestParameters(
                                parameterWithName("foodType").description("음식 종류에 대한 파라미터입니다"),
                                parameterWithName("price").description("가격에 대한 파라미터입니다")
                        )
                ));
    }

    @Test
    @TestDescription("비어있는 조건을 파라미터로 요청하는 경우")
    public void emptyCondition() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/conditions")
                .param("foodType", "foodType"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("잘못된 조건을 파라미터로 요청하는 경우")
    public void wrongCondition() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/conditions")
                .param("foodType", "foodType")
                .param("price", "-1"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("맛집 정보 단건 조회")
    public void getMatzip() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/{id}", 10))
                .andDo(print())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.Matzip-All").exists())
                .andExpect(jsonPath("_links.Matzip-Bingo").exists())
                .andExpect(jsonPath("_links.Matzip-Conditions").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(status().isOk())
                .andDo(document("Get-Matzip",
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("맛집 데이터 id"),
                                fieldWithPath("name").description("맛집 이름"),
                                fieldWithPath("foodType").description("음식 분류"),
                                fieldWithPath("price").description("가격대"),
                                fieldWithPath("infoLink").description("맛집 정보 링크"),
                                fieldWithPath("imgLink").description("맛집 이미지 링크"),
                                fieldWithPath("_links.self.href").description("맛집 단건 조회 및 수정이 가능한 링크"),
                                fieldWithPath("_links.Matzip-All.href").description("맛집 전체 조회 링크"),
                                fieldWithPath("_links.Matzip-Bingo.href").description("맛집 빙고 데이터 조회 링크"),
                                fieldWithPath("_links.Matzip-Conditions.href").description("조건을 포함하는 맛집 데이터 조회"),
                                fieldWithPath("_links.profile.href").description("Matzip API 문서 링크")
                        )
                ));
    }

    @Test
    @TestDescription("존재하지 않는 맛집 id")
    public void getMatzip404() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/{id}", 1000))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("맛집 정보 정상 업데이트")
    public void updateMatzip() throws Exception {
        // given
        Matzip matzip = matzipService.findOne(10).get();
        String name = "Updated Matzip Name";
        String foodType = "Update food type";
        MatzipDto matzipDto = modelMapper.map(matzip, MatzipDto.class);
        matzipDto.setName(name);
        matzipDto.setFoodType(foodType);

        // when & then
        this.mockMvc.perform(put("/api/v1/matzip/{id}", matzip.getId())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(matzipDto)))
                .andDo(print())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.Matzip-All").exists())
                .andExpect(jsonPath("_links.Matzip-Bingo").exists())
                .andExpect(jsonPath("_links.Matzip-Conditions").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(status().isOk())
                .andDo(document("Update-Matzip",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 헤더")
                        ),
                        requestFields(
                                fieldWithPath("name").description("수정할 맛집 이름"),
                                fieldWithPath("foodType").description("수정할 음식 분류"),
                                fieldWithPath("price").description("수정할 가격대"),
                                fieldWithPath("infoLink").description("맛집 정보 링크"),
                                fieldWithPath("imgLink").description("맛집 이미지 링크")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type 헤더")
                        ),
                        responseFields(
                                fieldWithPath("id").description("맛집 데이터 id"),
                                fieldWithPath("name").description("수전된 맛집 이름"),
                                fieldWithPath("foodType").description("수전된 음식 분류"),
                                fieldWithPath("price").description("수정된 가격대"),
                                fieldWithPath("infoLink").description("맛집 정보 링크"),
                                fieldWithPath("imgLink").description("맛집 이미지 링크"),
                                fieldWithPath("_links.self.href").description("맛집 단건 조회 및 수정이 가능한 링크"),
                                fieldWithPath("_links.Matzip-All.href").description("맛집 전체 조회 링크"),
                                fieldWithPath("_links.Matzip-Bingo.href").description("맛집 빙고 데이터 조회 링크"),
                                fieldWithPath("_links.Matzip-Conditions.href").description("조건을 포함하는 맛집 데이터 조회"),
                                fieldWithPath("_links.profile.href").description("Matzip API 문서 링크")
                        )
                ));

    }

    @Test
    @TestDescription("존재하지 않는 맛집 정보 업데이트")
    public void unknownMatzipUpdate() throws Exception {
        // given
        Matzip matzip = Matzip.builder()
                .id(1000)
                .name("Random name")
                .foodType("Random type")
                .price(1234)
                .infoLink("Random link")
                .imgLink("Random link")
                .build();
        MatzipDto unknownDto = modelMapper.map(matzip, MatzipDto.class);
        // when & then
        this.mockMvc.perform(put("/api/v1/matzip/{id}", matzip.getId())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(unknownDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("비어있는 업데이트 정보가 들어있는 경우")
    public void emptyMatzipUpdate() throws Exception {
        // given
        Matzip matzip = matzipService.findOne(10).get();
        MatzipDto matzipDto = modelMapper.map(matzip, MatzipDto.class);
        matzipDto.setName(null);
        // when & then
        this.mockMvc.perform(put("/api/v1/matzip/{id}", matzip.getId())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(matzipDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("잘못된 업데이트 정보가 들어있는 경우")
    public void wrongMatzipUpdate() throws Exception {
        // given
        Matzip matzip = matzipService.findOne(10).get();
        MatzipDto matzipDto = modelMapper.map(matzip, MatzipDto.class);
        matzipDto.setPrice(-1);
        // when & then
        this.mockMvc.perform(put("/api/v1/matzip/{id}", matzip.getId())
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(matzipDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private Matzip insertMatzip(int i) {
        Matzip matzip = buildMatzip(i);
        return matzipService.save(matzip);
    }

    private Matzip buildMatzip(int i) {
        return Matzip.builder()
                .name(i + "번째 맛집")
                .foodType("foodType")
                .price(1000 + i)
                .infoLink("some info link")
                .imgLink("some image link")
                .build();
    }

}