package com.z.merchantsettle.modules.upm.service;

import com.z.merchantsettle.modules.upm.domain.bo.User;

public interface SystemService {

    User login(String userId, String password);
}
