package com.mtech.recycler.service;


import com.mtech.recycler.dto.LoginResponseDto;

import java.util.Optional;

public interface LoginService {

    Optional<LoginResponseDto> authenticate(String email, String rawInputPassword);

}
