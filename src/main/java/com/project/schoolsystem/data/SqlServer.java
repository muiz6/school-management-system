package com.project.schoolsystem.data;

import com.project.schoolsystem.R;
import com.project.schoolsystem.data.models.*;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
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
            + "user=" + R.SQL_USER_NAME + ";"
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
                // System.out.println("Connected Successfully!");
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
            // System.out.println("Connected Successfully!");
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

    public UserModel getSignedUser(@Nonnull String userName, @Nonnull String password) {
        final String query = "EXEC sp_get_user @user_name=?, @password=?;";
        try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
             final PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, userName);
            ps.setString(2, password);
            try (final ResultSet rs = ps.executeQuery()) {
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
        try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
             final PreparedStatement ps = conn.prepareStatement(query)) {
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

    @Nonnull
    public Single<List<UserModel>> getTeachers() {
        return Single.create(new SingleOnSubscribe<List<UserModel>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<UserModel>> emitter) throws Exception {
                try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL)) {
                    final String query = "EXEC sp_get_teachers;";
                    try (final PreparedStatement ps = conn.prepareStatement(query)) {
                        try (final ResultSet rs = ps.executeQuery()) {
                            final List<UserModel> teacherList = new ArrayList<>();
                            while (rs.next()) {
                                final UserModel teacher = new UserModel();
                                teacher.setUserName(rs.getString("user_name"));
                                teacher.setPassword(rs.getString("password"));
                                teacher.setRegistrationDate(rs.getDate("registration_date"));
                                teacher.setDisplayName(rs.getString("display_name"));
                                teacher.setDob(rs.getDate("dob"));
                                teacher.setGender(rs.getString("gender"));
                                teacher.setPhoneNumber(rs.getString("mobile_no"));
                                teacher.setEmergencyContact(rs.getString("emergency_contact"));
                                teacher.setAddress(rs.getString("address"));
                                teacher.setCnic(rs.getString("cnic"));
                                teacher.setRole(rs.getString("user_role"));
                                teacher.setActive(rs.getBoolean("active"));
                                teacher.setQualification(rs.getString("qualification"));
                                teacherList.add(teacher);
                            }
                            emitter.onSuccess(teacherList);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @NonNull
    public Single<List<UserModel>> getTeacherBySearch(@Nonnull String searchTerm) {
        return Single.create(new SingleOnSubscribe<List<UserModel>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<UserModel>> emitter) throws Exception {
                try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL)) {
                    final String query = "EXEC sp_get_teachers_by_search @query=?;";
                    try (final PreparedStatement cs = conn.prepareStatement(query)) {
                        cs.setString(1, searchTerm);
                        try (final ResultSet rs = cs.executeQuery()) {
                            final List<UserModel> teacherList = new ArrayList<>();
                            while (rs.next()) {
                                final UserModel teacher = new UserModel();
                                teacher.setUserName(rs.getString("user_name"));
                                teacher.setPassword(rs.getString("password"));
                                teacher.setRegistrationDate(rs.getDate("registration_date"));
                                teacher.setDisplayName(rs.getString("display_name"));
                                teacher.setDob(rs.getDate("dob"));
                                teacher.setGender(rs.getString("gender"));
                                teacher.setPhoneNumber(rs.getString("mobile_no"));
                                teacher.setEmergencyContact(rs.getString("emergency_contact"));
                                teacher.setAddress(rs.getString("address"));
                                teacher.setCnic(rs.getString("cnic"));
                                teacher.setRole(rs.getString("user_role"));
                                teacher.setActive(rs.getBoolean("active"));
                                teacher.setQualification(rs.getString("qualification"));
                                teacherList.add(teacher);
                            }
                            emitter.onSuccess(teacherList);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Single<Boolean> postUser(UserModel model) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                final String query = "EXEC sp_post_user "
                        + "@user_name=?,"
                        + "@password=?,"
                        + "@auth_role=?,"
                        + "@display_name=?,"
                        + "@dob=?,"
                        + "@gender=?,"
                        + "@cnic=?,"
                        + "@mobile_no=?,"
                        + "@emergency_contact=?,"
                        + "@qualification=?,"
                        + "@address=?;";
                try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
                     final PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, model.getUserName());
                    ps.setString(2, model.getPassword());
                    ps.setString(3, model.getRole());
                    ps.setString(4, model.getDisplayName());
                    ps.setDate(5, model.getDob());
                    ps.setString(6, model.getGender());
                    ps.setString(7, model.getCnic());
                    ps.setString(8, model.getPhoneNumber());
                    ps.setString(9, model.getEmergencyContact());
                    ps.setString(10, model.getQualification());
                    ps.setString(11, model.getAddress());
                    ps.execute();
                    emitter.onSuccess(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        });
    }

    public Single<List<SessionModel>> getSessions() {
        return Single.create(new SingleOnSubscribe<List<SessionModel>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<SessionModel>> emitter) throws Exception {
                final String query = "EXEC sp_get_sessions;";
                try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
                     final PreparedStatement ps = conn.prepareStatement(query);
                     final ResultSet rs = ps.executeQuery()) {
                    final List<SessionModel> sessionList = new ArrayList<>();
                    while (rs.next()) {
                        final SessionModel model = new SessionModel();
                        model.setSessionCode(rs.getString("code"));
                        model.setSessionTitle(rs.getString("title"));
                        model.setStartDate(rs.getDate("start_date"));
                        model.setEndDate(rs.getDate("end_date"));
                        sessionList.add(model);
                    }
                    emitter.onSuccess(sessionList);
                } catch (SQLException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        });
    }

    public Single<Boolean> postSession(SessionModel session) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                final String query = "EXEC sp_post_session "
                        + "@code=?,"
                        + "@title=?,"
                        + "@start_date=?,"
                        + "@end_date=?;";
                try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
                     final PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, session.getSessionCode());
                    ps.setString(2, session.getSessionTitle());
                    ps.setDate(3, session.getStartDate());
                    ps.setDate(4, session.getEndDate());
                    ps.execute();
                    emitter.onSuccess(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        });
    }

    public Single<List<DepartmentModel>> getDepartments() {
        return Single.create(new SingleOnSubscribe<List<DepartmentModel>>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<List<DepartmentModel>> emitter) throws Exception {
                final String query = "EXEC sp_get_departments;";
                try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
                     final PreparedStatement ps = conn.prepareStatement(query);
                     final ResultSet rs = ps.executeQuery()) {
                    final List<DepartmentModel> departments = new ArrayList<>();
                    while (rs.next()) {
                        final DepartmentModel model = new DepartmentModel();
                        model.setTitle(rs.getString("title"));
                        model.setDepartmentCode(rs.getString("code"));
                        model.setActive(rs.getBoolean("active"));
                        departments.add(model);
                    }
                    emitter.onSuccess(departments);
                } catch (SQLException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        });
    }

    public Single<Boolean> postDepartment(DepartmentModel department) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                final String query = "EXEC sp_post_department "
                        + "@code=?,"
                        + "@title=?;";
                try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
                     final PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, department.getDepartmentCode());
                    ps.setString(2, department.getTitle());
                    ps.execute();
                    emitter.onSuccess(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        });
    }

    public Single<Boolean> postClass(ClassModel classModel) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Boolean> emitter) throws Exception {
                final String query = "EXEC sp_post_class "
                        + "@code=?,"
                        + "@department_code=?,"
                        + "@session_code=?;";
                try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL);
                     final PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, classModel.getClassCode());
                    ps.setString(2, classModel.getDepartmentCode());
                    ps.setString(3, classModel.getSessionCode());
                    ps.execute();
                    emitter.onSuccess(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        });
    }

    // public Single<UserModel> getUser(String userName) {
    //     return Single.create(new SingleOnSubscribe<UserModel>() {
    //         @Override
    //         public void subscribe(@NonNull SingleEmitter<UserModel> emitter) throws Exception {
    //             try (final Connection conn = DriverManager.getConnection(_CONNECTION_URL)) {
    //                 final String query = "EXEC sp_get_user_by_user_name ?;";
    //                 try (final PreparedStatement ps = conn.prepareStatement(query)) {
    //                     ps.setString(1, userName);
    //                     try (final ResultSet rs = ps.executeQuery()) {
    //                         if (rs.next()) {
    //                             final UserModel user = new UserModel();
    //                             user.setUserName(rs.getString("user_name"));
    //                             user.setPassword(rs.getString("password"));
    //                             user.setRegistrationDate(rs.getDate("registration_date"));
    //                             user.setDisplayName(rs.getString("display_name"));
    //                             user.setDob(rs.getDate("dob"));
    //                             user.setGender(rs.getString("gender"));
    //                             user.setPhoneNumber(rs.getString("mobile_no"));
    //                             user.setEmergencyContact(rs.getString("emergency_contact"));
    //                             user.setAddress(rs.getString("address"));
    //                             user.setCnic(rs.getString("cnic"));
    //                             user.setRole(rs.getString("user_role"));
    //                             user.setActive(rs.getBoolean("active"));
    //                             user.setQualification(rs.getString("qualification"));
    //                             emitter.onSuccess(user);
    //                         }
    //                     }
    //                 }
    //             } catch (SQLException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     });
    // }

    public interface OnCompletionCallback<T> {
        void onResult(boolean success, T result);
    }
}
