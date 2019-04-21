package com.example.demo.service;

import com.example.demo.common.annotaion.BigDecimailScale;
import com.example.demo.domain.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by sam on 2019/4/21.
 */
@Service
public class UserService {



    @BigDecimailScale(scale = 4)
    public User updatePoints(BigDecimal points) {
        User user = new User();
        user.setId(1);
        user.setName("小黑");
        user.setPoints(points);
        return user;
    }
}
