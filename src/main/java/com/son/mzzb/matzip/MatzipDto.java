package com.son.mzzb.matzip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatzipDto {

    @NotNull
    private String name;
    @NotNull
    private String foodType;
    @NotNull
    private String price;
    @NotNull
    private String infoLink;

    private String imgLink;

}
