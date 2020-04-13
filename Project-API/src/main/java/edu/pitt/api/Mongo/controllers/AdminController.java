package edu.pitt.api.Mongo.controllers;

import edu.pitt.api.Mongo.config.AppKeys;
import edu.pitt.api.Mongo.models.Accidents;
import edu.pitt.api.Mongo.models.Users;
import edu.pitt.api.Mongo.repository.AccidentsRepository;
import edu.pitt.api.Mongo.repository.AdminRepository;
import edu.pitt.api.Mongo.repository.UsersRepository;
import edu.pitt.api.Mongo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashMap;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping(AppKeys.Mongo_API_PATH + "/admin")
public class AdminController {
    @Autowired
    UsersRepository userRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AccidentsRepository accidentRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /*delete reports by reports' Id*/
    @DeleteMapping("/report/{reportId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object deleteAccidentsById(@PathVariable String reportId) {
        try {
            Accidents acc = accidentRepository.findByid(reportId);
            if (acc == null) {
                return ResponseEntity.badRequest().body("query report null");
            } else {
                accidentRepository.deleteById(reportId);
            }
            return acc;
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No report is found");
        }
    }

    /*delete users by username*/
    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object deleteUsersbyUsername(@PathVariable String username) {
        try {
            boolean exists = userRepository.existsUsersByUsrname(username);
            if (exists) {
                Users u=userRepository.findUsersByUsrnameIs(username);
               userRepository.delete(u);
               return u;

            }else{
                return ResponseEntity.badRequest().body("user is not found");
            }
        } catch (Exception er) {
            throw er;
        }
    }

    @PutMapping("updateUser/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object updateUser(@PathVariable String username, @RequestBody Users user) {
        return UsersController.updateUser(username, user, userRepository);
    }


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