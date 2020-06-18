package com.son.mzzb.matzip;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class MatzipDtoValidator {

    public void validate(MatzipDto matzipDto, Errors errors) {

//        String price = matzipDto.getPrice();
//        for(char c : price.toCharArray()) {
//            /*
//             errors의 field, code, message, rejectedValue 설정
//             reject -> Global Errors
//             rejectValue -> Field Errors
//             */
//            if(!Character.isDigit(c)) {
//                errors.rejectValue("price", "가격 오류", "숫자로 구성되어 있는 가격을 입력하세요");
//                break;
//            }
//        }

    }

}
