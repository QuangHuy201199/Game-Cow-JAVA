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

@NamedStoredProcedureQuery(name = "command.addd",
        resultClasses = UserEntity.class,
        procedureName = "PKG_COMMAND.GET_ID",
        parameters={
                @StoredProcedureParameter(name="v_id", type=Integer.class, mode=ParameterMode.IN),
                @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "v_cursor", type = ResultSet.class)
        }
)

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
