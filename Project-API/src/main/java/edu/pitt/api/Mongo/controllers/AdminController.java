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

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;



import edu.pitt.api.Mongo.repository.AdminRepository;


@CrossOrigin
@RestController
@RequestMapping(AppKeys.Mongo_API_PATH + "/admin")
public class AdminController {
    @Autowired
    UsersRepository userRepo;
    @Autowired
    AdminRepository adminRepo;
    @Autowired
    AccidentsRepository accidentRepo;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /*delete reports by reports' Id*/
    @DeleteMapping("/report/{reportId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object deleteAccidentsById(@PathVariable String reportId) {
        try {
            Accidents acc = accidentRepo.findByid(reportId);
            if (acc == null) {
                return ResponseEntity.badRequest().body("query report null");
            } else {
                accidentRepo.deleteById(reportId);
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
            boolean exists = 
              userRepo.existsUsersByUsrname(username);
            if (exists) {
                Users u=userRepo.findUsersByUsrnameIs(username);
               userRepo.delete(u);
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
        return UsersController.updateUser(username, user, userRepo);
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


    // admin login
    @PostMapping(value = "/login")
    public Object login(@RequestBody UsersController.LoginBody body) {
        Users u = userRepo.findUsersByUsrnameIsAndPwdIs(body.username, body.password);
        if (u == null) {
            return ResponseEntity.badRequest().body("User username and password mismatch");
        }
        if (!u.isAdmin()) {
            return ResponseEntity.badRequest().body("You are not an administrator, please log-in as a user");
        }
        return AdminController.getObject(Optional.of(u), jwtTokenProvider);
    }

    // get all users
    @GetMapping("/allUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    // get all accidents
    @GetMapping("/allAccidents")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Accidents> getReceent100Reports() {
        return accidentRepo.getRecent100Reports();
    }

    // change role of a user , assign admin
    @PutMapping("/changeRole/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object changeRole(@PathVariable String username) {
        Users user = userRepo.findUsersByUsrnameIs(username);
        user.setAdmin(!user.isAdmin());
        userRepo.save(user);
        return user;
    }

    // handel reports by commments
    @PostMapping("/{reportId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Accidents updateByReportId(@PathVariable String reportId, @RequestBody Accidents accidents) {
        Accidents oldAccident = accidentRepo.findByid(reportId);
        if (oldAccident == null) {
            throw new RuntimeException("No report is found");
        } else {
            oldAccident.setCity(accidents.getCity());
            oldAccident.setHumidity(accidents.getHumidity());
            oldAccident.setLat(accidents.getLat());
            oldAccident.setLng(accidents.getLng());
            oldAccident.setState(accidents.getState());
            oldAccident.setZipcode(accidents.getZipcode());
            oldAccident.setStreet(accidents.getStreet());
            oldAccident.setVisibility(accidents.getVisibility());
            return accidentRepo.save(oldAccident);
        }
    }


//    check info match on admin users
//    @GetMapping(value = "/infoCheck")
//    public Users checkInfoBy5Fields(@RequestBody Users user) {
//        List<Users> u= userRepo.checkInfo(user.getUsrname(), user.getCity(),
//                user.getState(), user.getEmail(), user.getPhone());
//        if(u.size()==0){
//            throw new RuntimeException("user doesn't exist");
//        }else if(u.size()>1){
//            throw new RuntimeException("user duplicated");
//        }else return u.get(0);
//
//    }


}