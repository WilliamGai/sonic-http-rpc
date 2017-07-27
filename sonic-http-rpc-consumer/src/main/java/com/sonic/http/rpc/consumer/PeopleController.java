package com.sonic.http.rpc.consumer;


import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sonic.http.rpc.api.People;
import com.sonic.http.rpc.api.SpeakInterface;

@Component("peopleController")
public class PeopleController
{
    @Resource
    private SpeakInterface speakInterface;

    public String getSpeak(Integer age,Integer sex)
    {
        People people = new People();
        people.setAge(age);
        people.setSex(sex);
        return speakInterface.speak(people);
    }
}
