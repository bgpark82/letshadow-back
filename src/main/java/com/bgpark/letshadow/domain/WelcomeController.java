package com.bgpark.letshadow.domain;

import com.bgpark.letshadow.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class WelcomeController {

    @GetMapping("/hello")
    public String welcome(Principal principal) {


        String credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        System.out.println("credentials: "+credentials);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        System.out.println("name: "+name);
        System.out.println("principal: "+principal);

        SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        return credentials;
    }
}
