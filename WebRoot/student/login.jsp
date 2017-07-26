<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="bean.Student"%>
<%@page import="dao.student.StudentDAO"%>
<%
	Student stu=new Student();
   	stu.setSid(request.getParameter("sid"));
   	stu.setSpassword(request.getParameter("spasswd"));

    System.out.println(stu.getSid());
   	StudentDAO studentDAO=new StudentDAO();
   if(studentDAO.login(stu).equals(stu.getSpassword())){
     out.print("true");  
   }
   else{
     out.print("false");
   }
%>