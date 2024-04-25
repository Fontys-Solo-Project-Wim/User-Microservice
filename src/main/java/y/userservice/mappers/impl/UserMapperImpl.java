package y.userservice.mappers.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import y.userservice.domain.dto.UserDto;
import y.userservice.domain.entities.UserEntity;
import y.userservice.mappers.Mapper;

@Component
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {
    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
