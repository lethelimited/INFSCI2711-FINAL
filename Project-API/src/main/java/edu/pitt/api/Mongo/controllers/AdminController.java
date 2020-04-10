package edu.pitt.api.Mongo.controllers;

import edu.pitt.api.Mongo.config.AppKeys;
import edu.pitt.api.Mongo.models.Users;
import edu.pitt.api.Mongo.repository.AccidentsRepository;
import edu.pitt.api.Mongo.repository.UsersRepository;
import edu.pitt.api.Mongo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

//import edu.pitt.api.Mongo.repository.AdminRepository;


@CrossOrigin
@RestController
@RequestMapping(AppKeys.Mongo_API_PATH + "/admin")
public class AdminController {
    @Autowired
    UsersRepository userRepository;
//    @Autowired
//    AdminRepository adminRepository;
    @Autowired
AccidentsRepository accidentRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    static Object getObject(Optional<Users> admin, JwtTokenProvider jwtTokenProvider) {
        try {
            String token = jwtTokenProvider.createToken(admin.get());
            HashMap<String, Object> result = new HashMap<>();
            result.put("User", admin);
            result.put("token", token);
            System.out.println(token);
            return result;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username/password supplied");
        }
    }


}