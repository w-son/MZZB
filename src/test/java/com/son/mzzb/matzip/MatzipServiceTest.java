package com.son.mzzb.matzip;

import com.son.mzzb.common.BaseTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.cache.Cache;

import java.util.ArrayList;
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
    @TestDescription("findAll 캐시 테스트 스택 오버플로우 참고 자료")
    public void cacheTest() throws Exception {

        // TODO 캐시 매니저를 받아오는 과정에서 이 객체를 validate 하는 것이 의미가 있는지 모르겠음

        List<Matzip> matzips = new ArrayList<>();
        matzips.add(Matzip.builder().name("A").build());
        matzips.add(Matzip.builder().name("B").build());

        // DB를 사용하지 않기 때문에 MatzipService에 대한 Mock 설정을 해준다
        MatzipService mockService = Mockito.mock(MatzipService.class);
        Mockito.when(mockService.findAll()).thenReturn(matzips);

        List<Matzip> testMatzips = mockService.findAll();
        assertThat(matzips).isEqualTo(testMatzips);

        testMatzips = mockService.findAll();
        assertThat(matzips).isEqualTo(testMatzips);

        // Mockito.verify(mockService, Mockito.times(이 부분이 조금 이상함)).findAll();
        Cache cache = cacheManager.getCache("findAllMatzip");
        System.out.println("캐시의 내용" + cache.getName());
        assertThat(cache).isNotNull();

    }

    @Test
    @TestDescription("Service 캐시 생성 확인용")
    public void createCacheTest() throws Exception {
        matzipService.findAll();
        matzipService.findAll();
    }

    @Test
    @TestDescription("Service 캐시 삭제 확인용")
    public void deleteCacheTest() throws Exception {
        matzipService.findAll();
        matzipService.save(Matzip.builder().name("A").build());
        matzipService.findAll();
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