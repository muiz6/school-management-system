package com.project.schoolsystem.data;

import com.project.schoolsystem.R;
import com.project.schoolsystem.data.model.ClassModel;
import com.project.schoolsystem.data.model.StudentModel;
import com.project.schoolsystem.data.model.TeacherModel;
import com.project.schoolsystem.data.model.UserModel;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// singleton pattern
public class SqlServer {
    private static final String _CONNECTION_URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=school_system;"
            + "user="+ R.SQL_USER_NAME +";"
            + "password=" + R.SQL_PASSW + ";";
    private static SqlServer _instance;
    private final BehaviorSubject<UserModel> _liveSignIn = BehaviorSubject.create();
    private UserModel _signedUserModel;

    private SqlServer() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static SqlServer getInstance() {
        if (_instance == null) {
            _instance = new SqlServer();
        }
        return  _instance;
    }

    public void connect(OnCompletionCallback<String> callback) {
        try {
            try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL)) {
                System.out.println("Connected Successfully!");
                callback.onResult(true, null);
            }
        } catch (SQLException e) {
            callback.onResult(false, null);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
            System.out.println("Connected Successfully!");
//                callback.onResult(true);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
//            callback.onResult(false);
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void insertStudent(StudentModel model, OnCompletionCallback<String> callback)
    {
        Connection con= getConnection();

        try{
        String query = "INSERT INTO student(name,father_name,mobile_no,emergency_contact,registeration_date,address,dob)values(?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, model.getName());
        ps.setString(2, model.getFather_name());
        ps.setString(3, model.getMobile_no());
        ps.setString(4, model.getEmergency_no());
        ps.setString(5, model.getRegistration_date());
        ps.setString(6, model.getAddress());
        ps.setString(7, model.getDob());


        ps.execute();
        System.out.println("Data Insert Successfully!");
        callback.onResult(true, null);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            callback.onResult(false, null);
        }
    }

    public void insertTeacher(TeacherModel model, OnCompletionCallback<String> callback)
    {
        Connection con= getConnection();

        try{
        String query = "INSERT INTO teacher(name,cnic,mobile_no,emergency_contact,address,registeration_date,dob,qualification)values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, model.getName());
        ps.setString(2, model.getCnic());
        ps.setString(3, model.getMobile_no());
        ps.setString(4, model.getEmergency_no());
        ps.setString(5, model.getAddress());
        ps.setString(6, model.getRegistration_date());
        ps.setString(7, model.getDob());
        ps.setString(8, model.getQualification());


        ps.execute();
        System.out.println("Data Insert Successfully!");
        callback.onResult(true, null);
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            callback.onResult(false, null);
        }
    }

    public void searchStudent(int id, OnCompletionCallback<StudentModel> callback)
    {
        Connection con = getConnection();

        try{
            String query = "select * from student where roll_no =?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();


            if (rs.next())
            {
                 StudentModel model = new StudentModel();
                  model.setName(rs.getString("name"));
                  model.setFather_name(rs.getString("father_name"));
                  model.setMobile_no(rs.getString("mobile_no"));
                  model.setEmergency_no(rs.getString("emergency_contact"));
                  model.setRegistration_date(rs.getString("registeration_date"));
                  model.setAddress(rs.getString("address"));
                  model.setDob(rs.getString("dob"));
//                jtxtprogram.setText(rs.getString("program"));
//                jtxtcell.setText(rs.getString("cellno"));
//
                model.setId(id);
                callback.onResult(true, model);
            } else {
                // this statement will run when data not found
                callback.onResult(false, null);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            callback.onResult(false, null);
        }

    }

    public void getClasses(OnCompletionCallback<List<ClassModel>> callback) {
        final String query = "SELECT id, name, term_id, (SELECT COUNT(*) FROM class_register WHERE class_id=id) AS count FROM class";
        boolean flagSuccess = false;
        try (final Connection conn = getConnection()) {
            final PreparedStatement ps = conn.prepareStatement(query);
            final List<ClassModel> classes = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final ClassModel model = new ClassModel();
                    model.setName(rs.getString("name"));
                    model.setId(rs.getInt("id"));
                    model.setTermId(rs.getInt("term_id"));
                    model.setStudentCount(rs.getInt("count"));
                    classes.add(model);
                }
                callback.onResult(true, classes);
                flagSuccess = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        if (!flagSuccess) {
            callback.onResult(false, null);
        }
    }

    public UserModel getUser(@Nonnull String userName, @Nonnull String password) {
        final String query = "EXEC sp_get_user @user_name=?, @password=?;";
        try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
            final PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userName);
            ps.setString(2, password);
            try (final ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    final UserModel model = new UserModel();
                    model.setUserName(rs.getString("user_name"));
                    model.setAddress(rs.getString("address"));
                    model.setCnic(rs.getString("cnic"));
                    model.setPassword(rs.getString("password"));
                    model.setPhoneNumber(rs.getString("mobile_no"));
                    model.setDisplayName(rs.getString("display_name"));
                    model.setActive(rs.getBoolean("active"));
                    model.setGender(rs.getString("gender"));
                    model.setQualification(rs.getString("qualification"));
                    model.setRegistrationDate(rs.getDate("registration_date"));
                    model.setRole(rs.getString("user_role"));
                    model.setDob(rs.getDate("dob"));
                    model.setEmergencyContact(rs.getString("emergency_contact"));
                    if (model.getUserName() != null) {
                        setLastSignIn(model);
                        return model;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        _signedUserModel = null;
        return null;
    }

    @Nullable
    public UserModel getLastSignIn() {
        return _signedUserModel;
    }

    public boolean patchSignedUser(@Nonnull UserModel model) {
        boolean result = patchUser(model);
        if (result) {
            _liveSignIn.onNext(model);
        }
        return result;
    }

    public boolean patchUser(@Nonnull UserModel model) {
        final String query = "EXEC sp_patch_user "
                + "@user_name=?,"
                + "@password=?,"
                + "@display_name=?,"
                + "@dob=?,"
                + "@gender=?,"
                + "@cnic=?,"
                + "@mobile_no=?,"
                + "@emergency_contact=?,"
                + "@qualification=?,"
                + "@address=?,"
                + "@active=?;";
        try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL)) {
            try (final PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, model.getUserName());
                ps.setString(2, model.getPassword());
                ps.setString(3, model.getDisplayName());
                ps.setDate(4, model.getDob());
                ps.setString(5, model.getGender());
                ps.setString(6, model.getCnic());
                ps.setString(7, model.getPhoneNumber());
                ps.setString(8, model.getEmergencyContact());
                ps.setString(9, model.getQualification());
                ps.setString(10, model.getAddress());
                ps.setBoolean(11, model.isActive());
                ps.execute();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setLastSignIn(@Nullable UserModel model) {
        _signedUserModel = model;
        if (model != null) {
            _liveSignIn.onNext(model);
        }
    }

    @Nonnull
    public Observable<UserModel> observeLastSignIn() {
        return _liveSignIn;
    }

    public interface OnCompletionCallback<T> {
        void onResult(boolean success, T result);
    }
}
