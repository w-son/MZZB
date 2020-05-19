package com.son.mzzb.config;

import com.son.mzzb.matzip.Matzip;
import com.son.mzzb.matzip.MatzipService;
import com.son.mzzb.tmi.Tmi;
import com.son.mzzb.tmi.TmiService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ApplicationRunner startApp() {
        return new ApplicationRunner() {

            @Autowired MatzipService matzipService;
            @Autowired TmiService tmiService;

            private void addMatzip(String name, String foodtype, String price, String imgLink) {
                Matzip matzip = Matzip.builder()
                        .name(name)
                        .foodtype(foodtype)
                        .price(price)
                        .imgLink(imgLink)
                        .build();
                matzipService.save(matzip);
            }

            private void addTmi(String content) {
                Tmi tmi = Tmi.builder()
                        .content(content)
                        .build();
                tmiService.save(tmi);
            }

            @Override
            public void run(ApplicationArguments args) throws Exception {
                /*
                String img = "http://t1.daumcdn.net/webtoon/op/f6df13cba138bce8e5526e71fd46ecb1d8433520";
                addMatzip("국시와가래떡", "한식", "6000", img);
                addMatzip("김피라", "한식", "8000", img);
                addMatzip("공복", "한식", "10000", img);
                addMatzip("또보겠지떡볶이", "한식", "12000", img);

                addTmi("과거 C동 8층에는 우동을 팔았다. 이를 C8 우동이라고 했다.");
                addTmi("장학금 수혜 요건은 봉사 시간 15시간인데 헌혈 1번이면 15시간 인정됩니다~");
                addTmi("R5층에 흡연장이 있는거 아시나요?");
                addTmi("Q동 7층은 시험기간에 자리가 남는거 아시나요?");
                addTmi("이 프로그램 제작자는 4학년인데 4학년이라고 다 나쁜 사람들 아니에요~");
                addTmi("홍대는 비가 와도 정문에서 후문까지 비 안맞고 갈 수 있는거 아시나요?");
                addTmi("프린트 충전 Q동 1층에서 가능한거 아시나요?");
                addTmi("여자 휴게실과 남자 휴게실이 존재하는거 아시나요?");
                addTmi( "신촌역에서 마포 8,9번을 타고 홍대 정문에서 내리면 등산 안 해도 괜찮아요~");
                */
            }

        };
    }

}
