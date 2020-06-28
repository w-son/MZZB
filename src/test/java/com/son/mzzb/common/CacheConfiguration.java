package com.son.mzzb.common;

import com.son.mzzb.matzip.MatzipRepository;
import com.son.mzzb.matzip.MatzipService;
import org.mockito.Mockito;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    /*
     캐시를 테스트 하기 위해서 설정한 Configuration 파일
     캐시 이름에 해당하는 캐시 매니저를 리턴하는 팩토리 메서드를 정의한다
     캐시를 테스트 할 때는 DB의 개입이 없어야 한다고 한다
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("findAllMatzip");
    }

}
