package edu.pitt.api.Mongo.repository;

import edu.pitt.api.Mongo.models.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface AdminRepository extends MongoRepository<Users, String>
{


}
