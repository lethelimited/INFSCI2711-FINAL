package edu.pitt.api.Mongo.repository;

import edu.pitt.api.Mongo.models.Accidents;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

public interface AccidentsRepository extends MongoRepository<Accidents, String>, CustomRepository
{
    // test for connection
    Accidents findByid(String id);

    List<Accidents> findAllBySource(String username);
    //search accidents by numbersByState


    List<Accidents>getRecent100Reports();

}
