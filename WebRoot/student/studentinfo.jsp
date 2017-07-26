<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="bean.Student"%>
<%@page import="dao.student.StudentDAO"%>
<%@page import="com.google.gson.Gson"%>
<%
    Student stu=new Student();
    stu.setSid(request.getParameter("sid"));
    
   StudentDAO studentDAO=new StudentDAO();
   
   stu=studentDAO.studentInfo(stu);
   
   Gson gson=new Gson();
   
   String stuInfoJSon=gson.toJson(stu);
   
   out.print(stuInfoJSon);
   
   System.out.println(stuInfoJSon);
%>
