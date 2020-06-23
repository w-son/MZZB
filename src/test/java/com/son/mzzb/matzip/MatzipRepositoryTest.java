package com.son.mzzb.matzip;

import com.son.mzzb.common.BaseTest;
import com.son.mzzb.common.TestDescription;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class MatzipRepositoryTest extends BaseTest {

    @Before
    public void setup() {
        // matzipRepository.deleteAll();
        List<Matzip> check = matzipRepository.findAll();
        if(check.isEmpty()) {
            IntStream.range(0, 49).forEach(this::insertMatzip);
        }
    }

    @Test
    @TestDescription("findOptionalMatzip 테스트")
    public void findOptionalMatzip() throws Exception {
        // when
        List<Matzip> list1 = matzipRepository.findOptionsMatzip("foodType", 1000);
        List<Matzip> list2 = matzipRepository.findOptionsMatzip("중식", 1000);

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
        List<Matzip> list1 = matzipRepository.findOptionsMatzip("foodType", null);
        List<Matzip> list2 = matzipRepository.findOptionsMatzip(null, 1000);

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

    @Test
    @TestDescription("fMBFTAPLTE 테스트")
    public void fMBFTAPLTE() throws Exception {
        // when
        List<Matzip> list1 = matzipRepository.findConditionsMatzip("foodType", 1001);
        List<Matzip> list2 = matzipRepository.findConditionsMatzip("일식", 1004);
        List<Matzip> list3 = matzipRepository.findConditionsMatzip("foodType", 100);

        // then
        for(Matzip matzip : list1) {
            System.out.println(matzip.getName());
        }

        for(Matzip matzip : list2) {
            System.out.println(matzip.getName());
        }

        for(Matzip matzip : list2) {
            System.out.println(matzip.getName());
        }

        assertEquals(2, list1.size());
        assertEquals(0, list2.size());
        assertEquals(0, list3.size());
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