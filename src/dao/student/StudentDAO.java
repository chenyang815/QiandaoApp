package dao.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import bean.DbConnect;
import bean.Student;

public class StudentDAO implements IStudentDAO {

	String registerSqlString="insert into student values(?,?,?,?,?,?,?)";
	String checkSqlString="select * from student where sid=?";
	String loginSqlString="select spassword FROM student where sid=?";
	String stuInfoString="select * where sid=?";
	String addcourse="insert into studentcourse values(?,?)";
	
	public String login(Student student) {
		// TODO Auto-generated method stub
		Connection connection=null;
        PreparedStatement pstm=null;
        ResultSet rs=null;
        
        
        connection=DbConnect.getDBconnect();
        String passwdString="";
        
        try {
			pstm=connection.prepareStatement(loginSqlString);
			pstm.setString(1, student.getSid());
			rs=pstm.executeQuery();
			 
	            if (rs.next()){
	            	passwdString=rs.getString("spassword");
	            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            DbConnect.closeDb(connection,pstm,rs);
		}
        
		return passwdString;
	}

	public boolean register(Student student) {
		// TODO Auto-generated method stub
		Connection connection=null;
        PreparedStatement pstm=null;
        ResultSet rs=null;
        boolean check=false;
        
        connection=DbConnect.getDBconnect();
        
        
        try {
			pstm=connection.prepareStatement(registerSqlString);
			pstm.setString(1, student.getSid());
			pstm.setString(2, student.getSname());
			pstm.setString(3, student.getSpassword());
			pstm.setString(4, student.getScollege());
			pstm.setString(5, student.getSprofession());
			pstm.setString(6, student.getSclass());
			pstm.setString(7, student.getSphone());
			 int i=pstm.executeUpdate();
	            if (i==1){
	                check=true;
	            }else {
	                check=false;
	            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            DbConnect.closeDb(connection,pstm,rs);
		}
        
		return check;
	}

	public boolean checkStu(Student student) {
		// TODO Auto-generated method stub
		
		Connection connection=null;
        PreparedStatement pstm=null;
        ResultSet rs=null;
        boolean check=false;
        
        connection=DbConnect.getDBconnect();
        
        
        try {
			pstm=connection.prepareStatement(checkSqlString);
			pstm.setString(1, student.getSid());
			rs=pstm.executeQuery();
			//System.out.println("run here");
			if (rs.next()){
					//System.out.println("false");
					check=true;
	            }else {
				//System.out.println("true");
	                check=false;
	            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            DbConnect.closeDb(connection,pstm,rs);
		}
        
		return check;
	}

	public Student studentInfo(Student s) {
		// TODO Auto-generated method stub
		
		Connection connection=null;
        PreparedStatement pstm=null;
        ResultSet rs=null;
        
        
        connection=DbConnect.getDBconnect();
        Student student=new Student();
        
        try {
			pstm=connection.prepareStatement(stuInfoString);
			pstm.setString(1, s.getSid());
			rs=pstm.executeQuery();
	        if (rs.next()){
	        	s.setSid(rs.getString(1));
	            s.setSname(rs.getString(2));
	            s.setSpassword(rs.getString(3));
	            s.setScollege(rs.getString(4));
	            s.setSprofession(rs.getString(5));
	            s.setSclass(rs.getString(6));
	            s.setSphone(rs.getString(7));
	            	
	      }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
            DbConnect.closeDb(connection,pstm,rs);
		}
        
		return student;
	}

	public boolean addCourse(Student student,String courseid) {

		Connection connection=null;
		PreparedStatement pstm=null;
		ResultSet rs=null;
		boolean check=false;

		connection=DbConnect.getDBconnect();


		try {
			pstm=connection.prepareStatement(addcourse);
			pstm.setString(1,courseid);
			pstm.setString(2, student.getSid());
			int i=pstm.executeUpdate();
			if (i==1){
				check=true;
			}else {
				check=false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DbConnect.closeDb(connection,pstm,rs);
		}

		return check;
	}

	public boolean addCourseinCourse(String cid, String name) {
		String sql="insert into course values(?,?)";
		Connection connection=null;
		PreparedStatement pstm=null;
		ResultSet rs=null;
		boolean check=false;

		connection=DbConnect.getDBconnect();


		try {
			pstm=connection.prepareStatement(sql);
			pstm.setString(1,cid);
			pstm.setString(2,name);
			int i=pstm.executeUpdate();
			if (i==1){
				check=true;
			}else {
				check=false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DbConnect.closeDb(connection,pstm,rs);
		}

		return check;

	}

}
