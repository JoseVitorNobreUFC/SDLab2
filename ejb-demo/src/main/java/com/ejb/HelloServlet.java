package com.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    HelloBean helloBean;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Context ctx = new InitialContext();
            helloBean = (HelloBean) ctx.lookup("java:global/ejb-demo-1.0-SNAPSHOT/HelloBean");


            response.setContentType("text/plain");
            response.getWriter().write(helloBean.sayHello("usuário"));
        } catch (NamingException e) {
            throw new ServletException("Erro ao localizar o EJB", e);
        }
    }
}
