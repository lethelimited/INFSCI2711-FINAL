package edu.pitt.api.Mongo.controllers;

import edu.pitt.api.Mongo.config.AppKeys;
import edu.pitt.api.Mongo.models.Accidents;
import edu.pitt.api.Mongo.repository.Custom;
import edu.pitt.api.Mongo.repository.AccidentsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping(AppKeys.Mongo_API_PATH+"/accidents")
public class AccidentsController
{
    @Autowired
    private AccidentsRepository accidentsRepository;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Accidents> getAllAccidents() {
        return accidentsRepository.findAll();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Accidents getAccidentsById(@PathVariable("id") String id)
    {
        return accidentsRepository.findByid(id);
    }

    @RequestMapping(value = "/numbersByState", method = RequestMethod.GET)
    public List<Custom.CountState> getNumbersByState()
    {
        return accidentsRepository.getNumbersByState();
    }

    @RequestMapping(value = "/numbersByCounty/{state}", method = RequestMethod.GET)
    public List<Custom.CountCounty> getNumbersByCounty(@PathVariable("state") String state)
    {
        return accidentsRepository.getNumbersByCounty(state);
    }

    @RequestMapping(value="/accidentsByRoad/{state}/{city}/{street}", method = RequestMethod.GET)
    public List<Accidents> getAccidentsByRoad(@PathVariable String state, @PathVariable String city, @PathVariable String street)
    {
        return accidentsRepository.getAccidentsByRoad(state, city, street);
    }

    @RequestMapping(value="/numbersByVisibility", method = RequestMethod.GET)
    public List<Custom.CountVis>getNumbersByVisibility()
    {
        return accidentsRepository.getNumbersByVisibility();
    }

    @RequestMapping(value="/numbersByHumidity", method = RequestMethod.GET)
    public List<Custom.CountHumid>getNumbersByHumidity()
    {
        return accidentsRepository.getNumbersByHumidity();
    }

    @RequestMapping(value="/numbersByWeatherCondition", method = RequestMethod.GET)
    public List<Custom.CountWeather>getNumbersByWeatherCondition()
    {
        return accidentsRepository.getNumbersByWeatherCondition();
    }



}


