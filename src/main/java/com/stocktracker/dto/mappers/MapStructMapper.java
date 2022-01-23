package com.stocktracker.dto.mappers;

import com.stocktracker.dto.UserGetDto;
import com.stocktracker.dto.UserPostDto;
import com.stocktracker.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") //will produce a singleton Spring Bean mapper injectable wherever you need
public interface MapStructMapper {

    UserGetDto userToUserGetDto(User user);
    User userPostDtoToUser(UserPostDto userPostDto);
}
