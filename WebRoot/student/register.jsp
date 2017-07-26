<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="bean.Student"%>
<%@page import="dao.student.StudentDAO"%>
<%
	Student stu=new Student();
    /*System.out.println(request.getParameter("sid"));
    System.out.println(request.getParameter("sname"));
    System.out.println(request.getParameter("spasswd"));
    System.out.println(request.getParameter("scollege"));
    System.out.println(request.getParameter("sprofession"));
    System.out.println(request.getParameter("sclass"));
    System.out.println(request.getParameter("sphone"));*/



    stu.setSid(request.getParameter("sid"));
    stu.setSname(request.getParameter("sname"));
    stu.setSpassword(request.getParameter("spasswd"));
    stu.setScollege(request.getParameter("scollege"));
    stu.setSprofession(request.getParameter("sprofession"));
    stu.setSclass(request.getParameter("sclass"));
    stu.setSphone(request.getParameter("sphone"));

    String ids=request.getParameter("ids");
    String courses=request.getParameter("courses");

    Scanner scanner=new Scanner(ids);
    Scanner scanner1=new Scanner(courses);
    List<String> courseids=new ArrayList<String>();
    List<String> coursenames=new ArrayList<String>();
    
    while (scanner.hasNext()){   //解析提交的课程
        courseids.add(scanner.next());
    }

    while (scanner1.hasNext()){
        coursenames.add(scanner1.next());
    }

    StudentDAO studentDAO=new StudentDAO();



    //System.out.println(studentDAO.checkStu(stu));
    if(studentDAO.checkStu(stu)){  //检查是否注册
        out.print("false");
    }
    else{
       	if(studentDAO.register(stu)){ //注册

            for (int i=0;i<courseids.size();i++){  //添加课程信息
                studentDAO.addCourseinCourse(courseids.get(i),coursenames.get(i));
            }

            for (String id:     //添加选课信息
                    courseids) {
                studentDAO.addCourse(stu,id);
            }

       	out.print("true");
    }
      	else{
    	out.print("false");
     }
    }
%>


