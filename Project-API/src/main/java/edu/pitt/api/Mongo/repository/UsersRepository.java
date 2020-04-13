package edu.pitt.api.Mongo.repository;

import edu.pitt.api.Mongo.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends MongoRepository<Users,String > {

    @Query(value="{ '_id' : ?0 }", fields="{ 'name' : 1, 'email' : 1}")
    List<Users> findByThePersonsid(String id);


    @Query(value = "{'usrname':?0,'city':?1,'state':?2,'email':?3,'phone':?4}")
    List<Users> checkInfo(String usrname, String city, String state, String email, String phone);


    List<Users> findOneByUsrname(String usrname);

    List<Users> findUsersByUsrname(String usrname);

    Users findUsersByUsrnameIs(String usrname);

    Users findUsersByEmailIs(String email);

    Users findUsersByUsrnameIsAndPwdIs(String usrname, String pwd);

    Users deleteByUsrname(String usrname);

    Users deleteUsersByUsrnameExists(String usrname);

    boolean existsUsersByUsrname(String usrname);



}
