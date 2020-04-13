package edu.pitt.api.Mongo.repository;
import edu.pitt.api.Mongo.models.Accidents;
import java.util.List;
public interface CustomRepository
{
    List<Custom.CountState> getNumbersByState();
    List<Custom.CountCounty>getNumbersByCounty(String state);
    List<Accidents> getAccidentsByRoad(String state, String city, String street);
    List<Custom.CountVis>getNumbersByVisibility();
    List<Custom.CountHumid>getNumbersByHumidity();
    List<Custom.CountWeather>getNumbersByWeatherCondition();
    List<Accidents>getRecent100Reports();


}
