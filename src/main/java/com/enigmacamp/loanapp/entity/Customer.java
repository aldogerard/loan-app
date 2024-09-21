package com.enigmacamp.loanapp.entity;

import com.enigmacamp.loanapp.base.BaseEntity;
import com.enigmacamp.loanapp.constant.strings.PathDB;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = PathDB.CUSTOMER)
public class Customer extends BaseEntity {
    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column
    private String phone;

    @Column
    private String status;

    @OneToOne
    @JoinColumn(name = "profile_picture_id")
    private ProfilePicture profilePicture;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
