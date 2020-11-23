package com.project.schoolsystem;

public interface R {
    String SQL_USER_NAME = "SA";
    String SQL_PASSW = "Student@123";

    interface Fxml {
        String HOME = "/fxml/Home.fxml";
        String DASHBOARD = "/fxml/Dashboard.fxml";
        String STUDENTS = "/fxml/Students.fxml";
        String TIME_TABLE = "/fxml/Timetable.fxml";
        String ATTENDANCE_MANAGEMENT_SYSTEM = "/fxml/AttendenceManagementSystem.fxml";
        String DELETE_AND_EDIT_SYSTEM = "/fxml/DeleteAndEditSystem.fxml";
        String STUDENT_FORM = "/fxml/StudentRegistrationForm.fxml";
        String TEACHER_FORM = "/fxml/TeacherRegistrationForm.fxml";
        String MANAGE_CLASS = "/fxml/ManageClass.fxml";
        String TILE_CLASS = "/fxml/tile_class.fxml";
        String ADMIN_PAGE = "/fxml/admin_page.fxml";
        String TILE_DRAWER = "/fxml/tile_drawer.fxml";
        String LOGIN_PAGE = "/fxml/login_page.fxml";
        String SNACKBAR = "/fxml/snackbar.fxml";
        String TEACHER_PAGE = "/fxml/teacher_page.fxml";
        String DIALOG_SIGN_OUT = "/fxml/dialog_sign_out.fxml";
        String TAB = "/fxml/tab.fxml";
        String ADMIN_SETTINGS = "/fxml/admin_settings.fxml";
        String TEACHER_SETTINGS = "/fxml/teacher_settings.fxml";
        String MANAGE_TEACHERS = "/fxml/manage_teachers.fxml";
        String PERSON_TILE = "/fxml/tile_person.fxml";
        String ALERT = "/fxml/alert.fxml";
        String SESSION_TILE = "/fxml/tile_session.fxml";
        String DEPARTMENT_TILE = "/fxml/tile_department.fxml";
    }

    interface Image {
        String ICON = "/icons/icon.png";
    }

    interface Navigation {
        String ADMIN = "/json/navigation_admin.json";
        String TEACHER = "/json/navigation_teacher.json";
        String SESSION = "/json/tab_nav_session.json";
        String MANAGE_TEACHERS = "/json/tab_nav_mng_teachers.json";
    }

    interface Id {
        String DEST_EDIT_TEACHER = "dest_edit_teacher";
    }

    interface NavArgs {
        /**
         * key for {@link com.project.schoolsystem.data.model.UserModel} argument
         */
        String USER_MODEL = "user_model";
    }
}
