package com.example.demo.mappers;


import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegistrationDto userRegistrationDto);

    RegistrationDto toDto(User user);

}