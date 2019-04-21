package com.example.demo.domain;

import com.example.demo.common.annotaion.BigDecimailScale;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by sam on 2019/4/21.
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String nick;
    private Integer age;

    //保留4位小数
    @BigDecimailScale(scale = 4)
    private BigDecimal points;

//    @BigDecimailScale(scale = 4)
    public BigDecimal getPoints() {
        return points;
    }
}
