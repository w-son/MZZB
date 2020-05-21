package com.son.mzzb.matzip;

import com.son.mzzb.common.BaseControllerTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.InputStream;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class MatzipControllerTest extends BaseControllerTest {

    @Before
    public void setup() {
        // given
        IntStream.range(0, 50).forEach(this::insertMatzip);
    }

    @Test
    @TestDescription("맛집 정보 생성하기")
    public void createMatzip() throws Exception {
        // given
        MatzipDto matzipDto = MatzipDto.builder()
                .name("가미우동")
                .foodType("일식")
                .price("10000")
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
                .andExpect(status().isCreated());
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
                .price("가격")
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
                .andExpect(status().isOk());
    }

    @Test
    @TestDescription("맛집 정보 페이지 단위로 조회")
    public void getMatzipsByPage() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/page")
                            .param("page", "1")
                            .param("size", "15")
                            .param("sort", "name,ASC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.first").exists())
                .andExpect(jsonPath("_links.last").exists())
                .andExpect(jsonPath("_links.prev").exists())
                .andExpect(jsonPath("_links.next").exists());
    }

    @Test
    @TestDescription("맛집 정보 단건 조회")
    public void getMatzip() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/{id}", 25))
                .andDo(print())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(status().isOk());
    }

    @Test
    @TestDescription("존재하지 않는 맛집 id")
    public void getMatzip404() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/{id}", 1000))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Matzip insertMatzip(int i) {
        Matzip matzip = buildMatzip(i);
        return matzipService.save(matzip);
    }

    private Matzip buildMatzip(int i) {
        return Matzip.builder()
                .name(i + "번쨰 맛집")
                .foodtype("Guitar")
                .price("1234")
                .imgLink("some image link")
                .build();
    }

}