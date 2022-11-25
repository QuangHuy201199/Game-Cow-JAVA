package com.example.demo_get.service.implement;

import com.example.demo_get.mapper.MapperUser;
import com.example.demo_get.model.dto.UserDto;
import com.example.demo_get.model.entity.UserEntity;
import com.example.demo_get.model.in.UserIn;
import com.example.demo_get.model.respond.UserRespond;
import com.example.demo_get.repostory.UserRepository;
import com.example.demo_get.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
@Component
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserRespond getAll() {
        List<UserEntity>list = userRepository.getCommand();
        List<UserDto>listDto=list.stream().map(MapperUser::mapEntity).collect(Collectors.toList());
        return new UserRespond(listDto, "Success");
    }


    @Override
    public UserRespond create(UserIn UserIn) {
        userRepository.insertCommand(UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(),UserIn.getStockPrice(), UserIn.getStockNumber());
        List<UserEntity>list = userRepository.getCommand();
        List<UserDto>listDto=list.stream().map(MapperUser::mapEntity).collect(Collectors.toList());

        List<UserDto> matchingObjects = listDto.stream().filter(p-> p.getIsSale() != UserIn.getIsSale() && p.getNameStock().startsWith(UserIn.getNameStock())).collect(Collectors.toList());
        List<UserDto> users = new ArrayList<>();

        Comparator<UserDto> comparator = Comparator.comparing( UserDto::getStockPrice );
        List minObject = users.stream().min(comparator).get();
        List<UserDto> listData = users.stream().min(Comparator.comparing(UserIn::getStockPrice));

//        System.out.println("data" +matchingObjects);
        return new UserRespond(listData, "Success");
    }



    @Override
    public UserRespond delete(Integer Id) {
        return null;
    }

    @Override
    public UserRespond update(Integer Id, UserIn userIn) {
        return null;
    }

    @Override
    @Transactional
    public UserRespond getById(Integer id) {
        List<UserEntity>list = userRepository.getbyId(id);
        List<UserDto>listDto=list.stream().map(MapperUser::mapEntity).collect(Collectors.toList());
        return new UserRespond(listDto, "Success");
    }
}



