package com.son.mzzb.matzip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatzipParams {

    @NotNull
    private String foodType;

    @NotNull @Min(0)
    private Integer price;

}
