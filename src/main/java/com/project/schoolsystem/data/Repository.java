package com.project.schoolsystem.data;

// singleton pattern
public class Repository {
    private static Repository _instance;

    private Repository() {}

    public Repository getInstance() {
        if (_instance == null) {
            _instance = new Repository();
        }
        return _instance;
    }
}
