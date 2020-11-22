package com.project.schoolsystem.ui.alert;

import com.jfoenix.controls.JFXAlert;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Window;

public class ConfirmationAlert extends JFXAlert<Integer> {
    public static final int RESULT_YES = 0;
    public static final int RESULT_NO = 1;
    private final AlertContent _content = AlertContent.inflate();

    {
        setContent(_content.getRoot());
    }

    public ConfirmationAlert(Window window) {
        super(window);
    }

    public Single<Integer> showAsModal() {
        _content.setAlert(this);
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Integer> emitter) throws Exception {
                show();
                showingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable,
                            Boolean oldValue,
                            Boolean newValue) {
                        if (!newValue) {
                            Object result = getResult();
                            if (result instanceof Integer) {
                                emitter.onSuccess((Integer) result);
                            } else {
                                emitter.onError(new Throwable("Closed without result!"));
                            }
                        }
                    }
                });
            }
        });
    }

    public void setMessage(String message) {
        _content.setMessage(message);
    }
}
