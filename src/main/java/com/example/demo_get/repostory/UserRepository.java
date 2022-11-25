package com.example.demo_get.repostory;

import com.example.demo_get.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <UserEntity,Integer>{

    @Procedure(procedureName = "PKG_COMMAND.GET_COMMAND",refCursor = true)
    List<UserEntity> getCommand();

//    @Procedure(procedureName = "PKG_COMMAND.INSERT_COMMAND(?1,?2,?3,?4)", refCursor = true)
    @Modifying
    @Query(value = "{call PKG_COMMAND.INSERT_COMMAND(?1,?2,?3,?4,?5) }",nativeQuery = true)
    void insertCommand( String nameUser,String nameStock,Boolean isSale, Integer stockPrice, Integer stockNumber);

    @Modifying
    @Transactional
    @Procedure(procedureName = "command.getId")
    List<UserEntity> getbyId(@Param("Id") Integer Id);
}

