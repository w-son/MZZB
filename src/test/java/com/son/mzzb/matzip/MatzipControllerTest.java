package com.son.mzzb.matzip;

import com.son.mzzb.common.BaseControllerTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Test;

import java.io.InputStream;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


public class MatzipControllerTest extends BaseControllerTest {

    @Test
    @TestDescription("맛집 정보 전체 조회")
    public void getMatzips() throws Exception {
        // given
        IntStream.range(0, 50).forEach(this::insertMatzip);
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @TestDescription("맛집 정보 단건 조회")
    public void getMatzip() throws Exception {
        // given
        IntStream.range(0, 50).forEach(this::insertMatzip);
        // when & then
        this.mockMvc.perform(get("/api/v1/matzip/{id}", 25))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @TestDescription("존재하지 않는 맛집 id")
    public void getMatzip404() throws Exception {
        // given
        IntStream.range(0, 10).forEach(this::insertMatzip);
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