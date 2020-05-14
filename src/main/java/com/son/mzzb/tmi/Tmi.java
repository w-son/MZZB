package com.son.mzzb.tmi;

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
public class Tmi {

    @Column(name = "tmi_id")
    @Id @GeneratedValue
    private Integer id;

    private String content;

}
