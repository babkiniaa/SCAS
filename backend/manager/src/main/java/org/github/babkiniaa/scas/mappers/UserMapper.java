package org.github.babkiniaa.scas.mappers;

import org.github.babkiniaa.scas.dto.forUserDto.ProfileDto;
import org.github.babkiniaa.scas.dto.forUserDto.RegistrationDto;
import org.github.babkiniaa.scas.dto.forUserDto.ViewUserForAdmin;
import org.github.babkiniaa.scas.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;


/**
 * Интерфейс для преобразования между сущностями {@link User} и
 * объектами передачи данных {@link RegistrationDto}.
 * Использует MapStruct для автоматической генерации реализации маппинга.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegistrationDto userRegistrationDto);

    @Mapping(source = "avatarUrl", target = "avatar")
    ProfileDto toProfile(User user);

    User updateUserFromDto(ProfileDto userDto, @MappingTarget User user);

    List<ViewUserForAdmin> toAdmin(List<User> user);

}