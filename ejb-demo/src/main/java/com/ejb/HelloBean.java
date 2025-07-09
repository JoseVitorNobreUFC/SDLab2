package com.ejb;

import jakarta.ejb.Stateless;

@Stateless(name = "HelloBean")
public class HelloBean {
    public String sayHello(String name) {
        return "Ola " + name + ", este é um EJB!";
    }
}
