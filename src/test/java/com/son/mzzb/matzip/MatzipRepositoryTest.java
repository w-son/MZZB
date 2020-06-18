package com.son.mzzb.matzip;

import com.son.mzzb.common.BaseControllerTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class MatzipRepositoryTest extends BaseControllerTest {

    @Before
    public void setup() {
        matzipRepository.deleteAll();
        tmiRepository.deleteAll();
        IntStream.range(0, 5).forEach(this::insertMatzip);
    }

    @Test
    @TestDescription("정상적으로 음식 종류와 가격을 인자를 입력으로 넣은 경우")
    public void findConditionalMatzip() throws Exception {
        // when
        List<Matzip> list1 = matzipRepository.findConditionalMatzip("한식", "6000");
        List<Matzip> list2 = matzipRepository.findConditionalMatzip("중식", "6000");

        // then
        for(Matzip matzip : list1) {
            System.out.println(matzip.getName());
        }
        for(Matzip matzip : list2) {
            System.out.println(matzip.getName());
        }
        assertEquals(1, list1.size());
        assertEquals(0, list2.size());
    }

    @Test
    @TestDescription("인자 하나가 없는 경우")
    public void emptyParameters() throws Exception {
        // when
        List<Matzip> list1 = matzipRepository.findConditionalMatzip("한식", null);
        List<Matzip> list2 = matzipRepository.findConditionalMatzip(null, "6000");

        // then
        for (Matzip matzip : list1) {
            System.out.println(matzip.getName());
        }
        for (Matzip matzip : list2) {
            System.out.println(matzip.getName());
        }
        assertEquals(0, list1.size());
        assertEquals(0, list2.size());
    }

    private Matzip insertMatzip(int i) {
        Matzip matzip = buildMatzip(i);
        return matzipService.save(matzip);
    }

    private Matzip buildMatzip(int i) {
        return Matzip.builder()
                .name(i + "번째 맛집")
                .foodType("한식")
                .price("600" + i)
                .infoLink("some info link")
                .imgLink("some image link")
                .build();
    }

}