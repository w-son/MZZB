package com.son.mzzb.tmi;

import com.son.mzzb.common.BaseControllerTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TmiControllerTest extends BaseControllerTest {

    @Before
    public void setUp() {
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
                .andExpect(status().isCreated());
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
                .andExpect(status().isOk());
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
                .andExpect(status().isOk());
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