package edu.pitt.api.Mongo.controllers;

import edu.pitt.api.Mongo.config.AppKeys;
import edu.pitt.api.Mongo.models.Accidents;
import edu.pitt.api.Mongo.models.Users;
import edu.pitt.api.Mongo.repository.AccidentsRepository;
import edu.pitt.api.Mongo.repository.UsersRepository;
import edu.pitt.api.Mongo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(AppKeys.Mongo_API_PATH + "/user")
public class UsersController {
    @Autowired
    private UsersRepository userRepo;
    @Autowired
    private AccidentsRepository acidRepo;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /*User registration*/
    @PostMapping(value = "/signup")
    public Object signup(@RequestBody Users user) {
        Users u = userRepo.findUsersByUsrnameIs(user.getUsrname());
        if (u != null) {
            return ResponseEntity.badRequest().body("username already exists");
//            throw new RuntimeException("username already exists");
        }else if(user.getPwd().length()<4||user.getUsrname().length()<4){
            return ResponseEntity.badRequest().body("wrong Users format");
        }
        else {
            String token = jwtTokenProvider.createToken(user);

            HashMap<String, Object> result = new HashMap<>();
            result.put("user", userRepo.save(user));
            result.put("token", token);
            return result;
        }
    }

    /*Check info is matched or not*/
    @GetMapping(value = "/infoCheck")
    public Users checkInfoBy5Fields(@RequestBody Users user) {

        List<Users> u= userRepo.checkInfo(user.getUsrname(), user.getCity(),
                user.getState(), user.getEmail(), user.getPhone());
        if(u.size()==0){
            throw new RuntimeException("user doesn't exist");
        }else if(u.size()>1){
            throw new RuntimeException("user duplicated");
        }else return u.get(0);

    }

    /*Reset password for user*/
    @PutMapping(value = "/updatePassword/{username}")
    public Users restPassword(@PathVariable String username, @RequestBody LoginBody body) {
        Users u = userRepo.findUsersByUsrnameIs(username);
        if (u != null) {
            u.setPwd(body.password);
            return userRepo.save(u);
        } else throw new RuntimeException("username doesn't exist (Mongodb)");

    }

    /*user login*/
    @PostMapping(value = "/login")
    public Object login(@RequestBody LoginBody body) {
        Users u = userRepo.findUsersByUsrnameIsAndPwdIs(body.username, body.password);
        if (u == null) {
            return ResponseEntity.badRequest().body("User username and password mismatch");
        }
        return AdminController.getObject(Optional.of(u), jwtTokenProvider);
    }


    /*Get all user info*/
    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Users getAllInfo(@PathVariable String username) {
        Users user = userRepo.findUsersByUsrnameIs(username);
        if (user == null) {
            throw new RuntimeException("username doesn't exist");
        } else {
            return user;
        }
    }


    /*update new info for user*/
    @PutMapping(value = "/updateAllInfo/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Object updateAllInfo(@PathVariable String username, @RequestBody Users user) {
        return updateUser(username, user, userRepo);
    }

    /*Update report settings*/
    @PutMapping(value = "/updateSettings/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Users updateSettings(@PathVariable String username) {
        Users u = userRepo.findUsersByUsrnameIs(username);
        if (u == null) {
            throw new RuntimeException("username doesn't exist");
        } else {
            u.setAnonymous(!u.isAnonymous());
        }
        return userRepo.save(u);
    }

    /*Self-report accidents*/
    @PostMapping(value = "/self-report/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Accidents self_report(@PathVariable String username, @RequestBody Accidents accidents) {
        Users user = userRepo.findUsersByUsrnameIs(username);
        if (user == null) {
            throw new RuntimeException("username doesn't exist");
        } else {
            if (!user.isAnonymous()) {
                accidents.setSource(username);
            }
        }
        return acidRepo.save(accidents);
    }

    /*View user's all history reports*/
    @GetMapping(value = "/reports/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public List<Accidents> reportsByUsername(@PathVariable String username) {
        return acidRepo.findAllBySource(username);
    }


    /* Delete pending reports */
//    @DeleteMapping(value = "/{username}/{reportId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
//    public void deleteByReportId (@PathVariable String username, @PathVariable Long reportId) {
//        try{
//            acidRepo.deleteById(reportId);
//        } catch (NullPointerException er) {
//            throw new RuntimeException("cannot find report");
//        }
//
//    }

    public static class LoginBody {
        public String username;
        public String password;
    }

    @GetMapping(value = "/All/{username}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public List<Users> getAllUser(@PathVariable String username) {
//        return userRepo.findAll();
        if(username.equals("All")) return userRepo.findAll();
        return userRepo.findUsersByUsrname(username);
    }

    @GetMapping(value = "/One/{id}")
    public List<Users> getOneUser(@PathVariable String id) {
        return userRepo.findByThePersonsid(id);
    }

    static Object updateUser(@PathVariable String username, @RequestBody Users user, UsersRepository userRepository) {
        Users u = userRepository.findUsersByUsrnameIs(username);
        if (u == null) {
            return ResponseEntity.badRequest().body("username doesn't exist");
        } else {

            //check email eligibility
            String newEmail = user.getEmail();
            if(newEmail.length()<4){
                return ResponseEntity.badRequest().body("No valid email found in requestBody");
            }
            try{
            if (!u.getEmail().equalsIgnoreCase(newEmail)) {
                if (userRepository.findUsersByEmailIs(newEmail) != null) {
                    return ResponseEntity.badRequest().body("email already exist");
                } else {
                    u.setEmail(newEmail);
                }
            }
            }catch(Exception e){
                return ResponseEntity.badRequest().body("users email doesn't exist");
            }
            u.setCity(user.getCity());
            u.setState(user.getState());
            u.setPhone(user.getPhone());
            return userRepository.save(u);

        }
    }

}
