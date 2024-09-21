package com.enigmacamp.loanapp.entity;

import com.enigmacamp.loanapp.base.BaseEntity;
import com.enigmacamp.loanapp.constant.strings.PathDB;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = PathDB.PROFILE_PICTURE)
public class ProfilePicture extends BaseEntity {
    @Column
    private String name;

    @Column(name = "content_type")
    private String contentType;

    @Column
    private Long size;

    @Column
    private String url;

    @Column
    private String path;
}
