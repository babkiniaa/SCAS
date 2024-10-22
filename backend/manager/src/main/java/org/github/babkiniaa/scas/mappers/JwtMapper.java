package org.github.babkiniaa.scas.mappers;


import org.github.babkiniaa.scas.entity.User;
import org.github.babkiniaa.scas.security.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JwtMapper {

    UserDetailsImpl toJwt(User user);
}
