package edu.pitt.api.Mongo.repository;
import com.mongodb.Mongo;
import edu.pitt.api.Mongo.models.Accidents;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.util.List;
public class CustomRepositoryImpl implements CustomRepository
{
//    private final MongoOperations operations;
    private final MongoTemplate mongoTemplate;

    @Autowired
//    public CustomRepositoryImpl(MongoOperations operations) {this.operations = operations; }
    public CustomRepositoryImpl(MongoTemplate mongoTemplate){this.mongoTemplate = mongoTemplate;}

    @Override
    public List<Custom.CountState> getNumbersByState()
    {
        GroupOperation groupOperation = groupByState();
        ProjectionOperation projectionOperation = projectGroupByState();
        return mongoTemplate.aggregate(Aggregation.newAggregation(
                groupOperation,
                projectionOperation), Accidents.class, Custom.CountState.class).getMappedResults();
    }

    private GroupOperation groupByState(){
        return group("state")
                .addToSet("state").as("state")
                .count().as("total");
    }

    private ProjectionOperation projectGroupByState()
    {
        return project("state","total")
                .and("state").previousOperation();
    }

    @Override
    public List<Custom.CountCounty> getNumbersByCounty(String state){
        Aggregation agg = newAggregation(
                match(Criteria.where("state").is(state)),
                group("county").count().as("total"),
                project("total").and("county").previousOperation());
        return mongoTemplate.aggregate(agg, Accidents.class, Custom.CountCounty.class).getMappedResults();
    }

    @Override
    public List<Accidents> getAccidentsByRoad(String state, String city, String street)
    {
        Query locateByRoad = new Query();
        locateByRoad.addCriteria(Criteria.where("state").is(state));
        locateByRoad.addCriteria(Criteria.where("city").is(city));
        locateByRoad.addCriteria(Criteria.where("street").regex(street));
        List<Accidents> acc = mongoTemplate.find(locateByRoad,Accidents.class);
        return acc;
    }

    @Override
    public List<Custom.CountVis>getNumbersByVisibility(){
        Aggregation agg = newAggregation(
                group("visibility")
                        .addToSet("visibility").as("visibility")
                        .count().as("total"),
                project("visibility","total").and("visibility").previousOperation());
        return mongoTemplate.aggregate(agg, Accidents.class, Custom.CountVis.class).getMappedResults();
    }

    @Override
    public List<Custom.CountHumid>getNumbersByHumidity(){
        Aggregation agg = newAggregation(
                group("humidity")
                        .addToSet("humidity").as("humidity")
                        .count().as("total"),
                project("humidity","total").and("humidity").previousOperation());
        return mongoTemplate.aggregate(agg, Accidents.class, Custom.CountHumid.class).getMappedResults();
    }

    @Override
    public List<Custom.CountWeather>getNumbersByWeatherCondition(){
        Aggregation agg = newAggregation(
                group("weather_condition")
                        .addToSet("weather_condition").as("weather_condition")
                        .count().as("total"),
                project("weather_condition", "total").and("weather_condition").previousOperation());
        return mongoTemplate.aggregate(agg,Accidents.class, Custom.CountWeather.class).getMappedResults();
    }

    @Override
    public List<Accidents>getRecent100Reports(){
        Query loadTop100 = new Query();
        loadTop100.limit(100);
        loadTop100.with(Sort.by(Sort.Direction.DESC, "startTime"));
        return mongoTemplate.find(loadTop100, Accidents.class);
    }

}
