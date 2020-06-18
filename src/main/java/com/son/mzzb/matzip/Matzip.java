package com.son.mzzb.matzip;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity @Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Matzip {

    @Column(name = "matzip_id")
    @Id @GeneratedValue
    private Integer id;

    private String name;

    private String foodType;

    private Integer price;

    private String infoLink;

    private String imgLink;

}
