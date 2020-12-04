package com.project.schoolsystem;

public interface R {
    String SQL_USER_NAME = "SA";
    String SQL_PASSW = "Student@123";

    interface Fxml {
        String ADMIN_PAGE = "/fxml/admin_page.fxml";
        String TILE_DRAWER = "/fxml/tile_drawer.fxml";
        String LOGIN_PAGE = "/fxml/login_page.fxml";
        String SNACKBAR = "/fxml/snackbar.fxml";
        String TEACHER_PAGE = "/fxml/teacher_page.fxml";
        String DIALOG_SIGN_OUT = "/fxml/dialog_sign_out.fxml";
        String TAB = "/fxml/tab.fxml";
        String ADMIN_SETTINGS = "/fxml/admin_settings.fxml";
        String TEACHER_SETTINGS = "/fxml/teacher_settings.fxml";
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
        String MANAGE_STUDENTS = "/json/tab_nav_mng_students.json";
    }

    interface Id {
        String DEST_EDIT_TEACHER = "dest_edit_teacher";
        String DEST_EDIT_STUDENT = "dest_edit_student";
        String DEST_EDIT_SESSION = "dest_edit_session";
    }

    interface NavArgs {
        /**
         * key for {@link com.project.schoolsystem.data.models.UserModel} argument
         */
        String USER_MODEL = "user_model";
        String STUDENT_MODEL = "student_model";
        String SESSION_MODEL = "session_model";
    }
}
