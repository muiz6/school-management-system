package com.project.schoolsystem;


import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// singleton pattern
public class PreferenceProvider {
    private static final String _PREF_ROOT = "data/";
    private static final String _PREF_PATH = _PREF_ROOT + "preferences.json";
    private static PreferenceProvider _instance;
    private final Gson _gson = new Gson();
    private final BehaviorSubject<PreferenceModel> _subject = BehaviorSubject.createDefault(load());

    private PreferenceProvider() {
        new File(_PREF_ROOT).mkdir();
    }

    public static PreferenceProvider getInstance() {
        if (_instance == null) {
            _instance = new PreferenceProvider();
        }
        return _instance;
    }

    public boolean save(PreferenceModel model) {
        final String prefs = _gson.toJson(model);
        try (final FileWriter writer = new FileWriter(_PREF_PATH)) {
            writer.write(prefs);
            _subject.onNext(model);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nonnull
    public PreferenceModel load() {
        try (final FileReader reader = new FileReader("data/preferences.json")) {
            final PreferenceModel model = _gson.fromJson(reader, PreferenceModel.class);
            if (model != null) {
                return model;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PreferenceModel();
    }

    public Observable<PreferenceModel> observePreference() {
        return _subject;
    }
}
