package com.example.demo.mappers;


import com.example.demo.dto.RegistrationDto;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;

/**
 * Интерфейс для преобразования между сущностями {@link User} и
 * объектами передачи данных {@link RegistrationDto}.
 * Использует MapStruct для автоматической генерации реализации маппинга.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  User toEntity(RegistrationDto userRegistrationDto);

}