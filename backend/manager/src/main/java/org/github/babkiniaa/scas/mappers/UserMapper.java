package org.github.babkiniaa.scas.mappers;


import org.github.babkiniaa.scas.dto.ProfileDto;
import org.github.babkiniaa.scas.dto.RegistrationDto;
import org.github.babkiniaa.scas.entity.User;
import org.mapstruct.Mapper;

/**
 * Интерфейс для преобразования между сущностями {@link User} и
 * объектами передачи данных {@link RegistrationDto}.
 * Использует MapStruct для автоматической генерации реализации маппинга.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegistrationDto userRegistrationDto);
    ProfileDto toProfile(User user);

}