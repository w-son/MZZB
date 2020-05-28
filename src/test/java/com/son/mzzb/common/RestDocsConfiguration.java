package com.son.mzzb.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration
public class RestDocsConfiguration {

    /*
     적용 시키고 싶은 테스트 클래스에
     @AutoConfigureRestDocs
     @Import(RestDocsConfiguration.class)

     을 포함 시킨 다음 andDo(document(...));
     를 통해서 문서화 시키고 싶은 요청에 대한 snippet을 생성한다
     정상적으로 생성된 경우에 빌드 경로에 생성한 snippet들을 확인할 수 있다
     */
    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return new RestDocsMockMvcConfigurationCustomizer() {
            @Override
            public void customize(MockMvcRestDocumentationConfigurer configurer) {
                configurer.operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint());
            }
        };
    }

}
