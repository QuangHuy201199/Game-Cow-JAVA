package com.example.demo_get.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name="COMMAND")
public class UserEntity {
    @Id
    @Column(name = "ID")
    private  Integer Id;

    @Column(name = "ISSALE")
    private  Boolean isSale ;

    @Column(name="NAMEUSER")
    private String nameUser;

    @Column(name = "NAMESTOCK")
    private  String nameStock ;

    @Column(name = "STOCKPRICE")
    private  Integer stockPrice ;

    @Column(name= "STOCKNUMBER")
    private Integer stockNumber;

    @Column(name = "TIMECREATE")
    private Timestamp timeCreate;

}
