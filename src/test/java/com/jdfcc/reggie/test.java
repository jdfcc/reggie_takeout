package com.jdfcc.reggie;

import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.Dish;
import com.jdfcc.reggie.entity.Employee;
import com.jdfcc.reggie.mapper.DishMapper;
import com.jdfcc.reggie.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;

@SpringBootTest(classes = ReggieApplication.class)
public class test {

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private EmployeeService service;
//
//    @Autowired
//    private DishMapper mapper;
//
//    @Test
//    public void TestSplit(){
//        String s="123456,6666666";
//        String as="123456";
//        String[] a = as.split(",");
//        for(String val:a){
//            System.out.println(val);
//        }
//        System.out.println(a.length);
//    }
//
//    @Test
//    public void TestSearch(){
//       Dish dish=mapper.selectById("1397849739276890114");
//        System.out.println(dish.toString());
//    }

//    @Test
//    public void TestRedis() {
//        //获取连接
//        Jedis jedis = new Jedis("localhost", 6379);
//
//        jedis.set("userName", "zhangsan");
//        System.out.println(jedis.get("userName"));
////        jedis.del("userName");
////        System.out.println(jedis.get("userName"));
//        jedis.hset("zhao","age","7");
//        System.out.println(jedis.hget("zhao","age"));
//        System.out.println(jedis.keys("*"));
//
//        jedis.close();
//
//    }

    @Test
    public void testRedis() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("jdfcc1", "hhh");
    }

    @Test
    public void testZSet() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("range", "a", 1);
        zSetOperations.add("range", "b", 2);
        zSetOperations.add("range", "c", 3);
        zSetOperations.add("range", "d", 4);
        zSetOperations.remove("range", "a", "b");
        System.out.println(zSetOperations.range("range", 0, -1));
    }

    @Test
    public void testCommon(){
        System.out.println( redisTemplate.keys("*"));

    }

}
