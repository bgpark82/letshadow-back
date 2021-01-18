package com.bgpark.letshadow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class LetshadowApplicationTests {


    @Test
    void contextLoads() {
    }

    @Test
    void passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String test = passwordEncoder.encode("123");
        System.out.println(test);
    }
}
