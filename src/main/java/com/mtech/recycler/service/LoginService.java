package com.mtech.recycler.service;


import com.mtech.recycler.model.LoginResponse;

import java.util.Optional;

public interface LoginService {

    Optional<LoginResponse> authenticate(String userName, String rawInputPassword);

    Optional<LoginResponse> refreshAccessToken(String refreshToken);
}
