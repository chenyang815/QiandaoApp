package dao.student;

import bean.Student;

public interface IStudentDAO {
     boolean register(Student student); //学生注册
     String login(Student student);     //学生登录验证
     boolean checkStu(Student student);  //检查学生是否注册
     Student studentInfo(Student student);//查询学生信息
     boolean addCourse(Student student,String courseid);//添加学生选课信息
     boolean addCourseinCourse(String cid,String name);//添加课程（测试）
}
