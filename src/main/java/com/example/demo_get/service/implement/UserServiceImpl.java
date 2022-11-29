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
import java.sql.Timestamp;
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
//        userRepository.insertCommand(UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(),UserIn.getStockPrice(), UserIn.getStockNumber());
        List<UserEntity>list = userRepository.getCommand();
        List<UserDto>listDto=list.stream().map(MapperUser::mapEntity).collect(Collectors.toList());

        if (UserIn.getIsSale() == true){        //Bán
            List<UserDto> matchingObjects = listDto.stream()
                    .filter(p-> p.getNameStock().startsWith(UserIn.getNameStock())
                    && p.getIsSale() != UserIn.getIsSale()
                    && p.getStockPrice() >= UserIn.getStockPrice())
                    .collect(Collectors.toList());

            if (matchingObjects.size() == 0){
                userRepository.insertCommand(UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(),UserIn.getStockPrice(), UserIn.getStockNumber());
                return new UserRespond( "Đặt lệnh bán thành công");
            }

            UserDto maxObject1  =  matchingObjects.stream()
                    .max(Comparator.comparing(UserDto::getStockPrice )
                    .thenComparing(UserDto::getTimeCreate))
                    .get();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Integer realStockNumberSale;
            if(UserIn.getStockNumber() > maxObject1.getStockNumber()){
                realStockNumberSale = UserIn.getStockNumber() - maxObject1.getStockNumber();
                userRepository.deleteTable(maxObject1.getId());
                userRepository.insertCommand(UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(),UserIn.getStockPrice(), realStockNumberSale);
                userRepository.insertCommandComplete( maxObject1.getNameUser(),maxObject1.getNameStock(),maxObject1.getIsSale(), maxObject1.getStockPrice(),UserIn.getStockPrice() ,  maxObject1.getStockNumber(),   maxObject1.getStockNumber(), maxObject1.getTimeCreate());
                userRepository.insertCommandComplete( UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(), UserIn.getStockPrice(),maxObject1.getStockPrice() ,  UserIn.getStockNumber() , realStockNumberSale , timestamp);
            }
            else if(UserIn.getStockNumber() < maxObject1.getStockNumber()) {
                realStockNumberSale = maxObject1.getStockNumber() - UserIn.getStockNumber();
//              UserEntity userEntity = userRepository.getById(maxObject1.getId());
//              userEntity.setStockNumber(realStockNumberSale);
//              userRepository.save(userEntity);
                userRepository.updateTable(35, realStockNumberSale);
                userRepository.insertCommandComplete( maxObject1.getNameUser(),maxObject1.getNameStock(),maxObject1.getIsSale(), maxObject1.getStockPrice(),UserIn.getStockPrice() ,  maxObject1.getStockNumber(),   UserIn.getStockNumber(), maxObject1.getTimeCreate());
                userRepository.insertCommandComplete( UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(), UserIn.getStockPrice(),maxObject1.getStockPrice() ,  UserIn.getStockNumber() ,  UserIn.getStockNumber() , timestamp);
            }else {
//                userRepository.deleteById(maxObject1.getId());
                userRepository.insertCommandComplete( maxObject1.getNameUser(),maxObject1.getNameStock(),maxObject1.getIsSale(), maxObject1.getStockPrice(),UserIn.getStockPrice() ,  maxObject1.getStockNumber(),   maxObject1.getStockNumber(), maxObject1.getTimeCreate());
                userRepository.insertCommandComplete( UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(), UserIn.getStockPrice(),maxObject1.getStockPrice() ,  UserIn.getStockNumber() ,  UserIn.getStockNumber() , timestamp);
            }
            return new UserRespond(maxObject1, "Khớp lệnh bán");
        }else {                                 //Mua
            List<UserDto> matchingObjects = listDto.stream()
                    .filter(p-> p.getNameStock().startsWith(UserIn.getNameStock())
                    && p.getIsSale() != UserIn.getIsSale()
                    && p.getStockPrice() <= UserIn.getStockPrice())
                    .collect(Collectors.toList());

            if (matchingObjects.size() == 0){
                userRepository.insertCommand(UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(),UserIn.getStockPrice(), UserIn.getStockNumber());
                return new UserRespond( "Đặt lệnh mua thành công");
            }

            UserDto minObject1  =  matchingObjects.stream()
                    .min(Comparator.comparing(UserDto::getStockPrice )
                            .thenComparing(UserDto::getTimeCreate))
                    .get();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Integer realStockNumber;
            if(UserIn.getStockNumber() > minObject1.getStockNumber()){
                realStockNumber = UserIn.getStockNumber() - minObject1.getStockNumber();
                userRepository.deleteTable(minObject1.getId());
                userRepository.insertCommand(UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(),UserIn.getStockPrice(), realStockNumber);
                userRepository.insertCommandComplete( minObject1.getNameUser(),minObject1.getNameStock(),minObject1.getIsSale(), minObject1.getStockPrice(),UserIn.getStockPrice() ,  minObject1.getStockNumber(),   minObject1.getStockNumber(), minObject1.getTimeCreate());
                userRepository.insertCommandComplete( UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(), UserIn.getStockPrice(),minObject1.getStockPrice() ,  UserIn.getStockNumber() , realStockNumber , timestamp);
            }
            else if(UserIn.getStockNumber() < minObject1.getStockNumber()) {
                realStockNumber = minObject1.getStockNumber() - UserIn.getStockNumber();
                UserEntity userEntity = userRepository.getById(minObject1.getId());
                userEntity.setStockNumber(realStockNumber);
                userRepository.save(userEntity);
//                userRepository.updateById(maxObject1.getId(), realStockNumber);
                userRepository.insertCommandComplete( minObject1.getNameUser(),minObject1.getNameStock(),minObject1.getIsSale(), minObject1.getStockPrice(),UserIn.getStockPrice() ,  minObject1.getStockNumber(),   UserIn.getStockNumber(), minObject1.getTimeCreate());
                userRepository.insertCommandComplete( UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(), UserIn.getStockPrice(),minObject1.getStockPrice() ,  UserIn.getStockNumber() ,  UserIn.getStockNumber() , timestamp);
            }else {
//                userRepository.deleteById(minObject1.getId());
                userRepository.insertCommandComplete( minObject1.getNameUser(),minObject1.getNameStock(),minObject1.getIsSale(), minObject1.getStockPrice(),UserIn.getStockPrice() ,  minObject1.getStockNumber(),   minObject1.getStockNumber(), minObject1.getTimeCreate());
                userRepository.insertCommandComplete( UserIn.getNameUser(),UserIn.getNameStock(),UserIn.getIsSale(), UserIn.getStockPrice(),minObject1.getStockPrice() ,  UserIn.getStockNumber() ,  UserIn.getStockNumber() , timestamp);
            }


            return new UserRespond(minObject1, "Khớp lệnh mua");
        }
    }

    @Override
    public UserRespond delete(Integer Id) {
        userRepository.deleteTable(Id);
        return new UserRespond(0, "delete lệnh mua");
    }

    @Override
    public UserRespond update(Integer Id, UserIn userIn) {
        userRepository.updateTable(Id, userIn.getStockNumber());
        return new UserRespond(0, "update lệnh mua");
    }

    @Override
    @Transactional
    public UserRespond getById(Integer id) {
        return null;
    }
}



