package com.example.demo;

import com.example.demo.common.annotaion.BigDecimailScale;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;

import static java.math.RoundingMode.HALF_UP;

/**
 * Created by sam on 2019/4/21.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AnnotationTest {

    @Autowired
    private UserService userService;



    @Test
    public void testUserService(){

        User user = userService.updatePoints(new BigDecimal(120.34555));

        Assert.assertEquals(new BigDecimal(120.3456).setScale(4,HALF_UP),user.getPoints());
    }


    @Test
    public void testUserDomain() throws IllegalAccessException {

        User user = new User();
        user.setId(1);
        user.setName("小黑");
        user.setNick("小黑爱大长腿");
        user.setPoints(new BigDecimal(120.34555));

        //如果是非spring管理的对象，就必须要手动写解析器了,这个问题也困扰了我好久，暂时没有很好的方式解决
        filedProcess(user);

        methodsProcess(user);

        Assert.assertEquals(new BigDecimal(120.3456).setScale(4,HALF_UP),user.getPoints());

    }

    private void filedProcess(User user) throws IllegalAccessException {
        Field[] fields = User.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(BigDecimailScale.class)){
                BigDecimailScale bigDecimailScale = field.getAnnotation(BigDecimailScale.class);
                field.setAccessible(true);
                if (field.getType() == BigDecimal.class){
                   BigDecimal val = (BigDecimal) field.get(user);
                   val = val.setScale(bigDecimailScale.scale(),bigDecimailScale.roundMode());
                   field.set(user,val);
                }
                System.out.println(field.getName());
            }
        }
    }

    private void methodsProcess(User user) {
        Method[] methods = User.class.getDeclaredMethods();

        Arrays.stream(methods).filter(method -> method.getName().contains("get")).forEach(method -> {
            if (method.isAnnotationPresent(BigDecimailScale.class)){
                BigDecimailScale bigDecimailScale = method.getAnnotation(BigDecimailScale.class);
                String filedName =  method.getName().substring(3,method.getName().length()).toLowerCase();
                try {
                    Field field = user.getClass().getDeclaredField(filedName);
                    field.setAccessible(true);
                    if (field.getType() == BigDecimal.class){
                        BigDecimal val = (BigDecimal) field.get(user);
                        val = val.setScale(bigDecimailScale.scale(),bigDecimailScale.roundMode());
                        field.set(user,val);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
