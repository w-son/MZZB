package com.son.mzzb.matzip;

import com.son.mzzb.common.BaseTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class MatzipServiceTest extends BaseTest {

    @Before
    public void setup() {

        List<Matzip> check = matzipService.findAll();
        if(check.isEmpty()) {
            IntStream.range(0, 100).forEach(this::insertMatzip);
        }
    }

    @Test
    @TestDescription("findAll 캐시 생성 및 삭제 테스트")
    public void cacheTest() throws Exception {

        // TODO 캐시 관련 테스트 작성

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