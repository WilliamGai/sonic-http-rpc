package com.sonic.http.rpc.provider;

import org.springframework.stereotype.Component;

import com.sonic.http.rpc.api.People;
import com.sonic.http.rpc.api.SpeakInterface;

@Component("speakInterface")
public class SpeakInterfaceImpl implements SpeakInterface {
    public String speak(People people) {
	if (people.getAge() > 18) {
	    if (people.getSex() == 0) {
		return "female" + people.getAge() + "岁";
	    } else {
		return "male" + people.getAge() + "岁";
	    }
	} else {
	    return "小朋友";
	}
    }
}
