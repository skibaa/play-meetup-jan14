package models;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.LinkedList;
import java.util.List;

public class PlayerConnect {
    public static final int MAX_ID = 9999;

    public Integer playerId;
    public String user;
    public String password;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new LinkedList<>();
        if (playerId == null)
            errors.add(new ValidationError("playerId", "Player ID must not be empty"));
        else if (playerId <= 0)
            errors.add(new ValidationError("playerId", "Player ID must be positive"));
        else if (playerId > MAX_ID)
            errors.add(new ValidationError("playerId", "Player ID must be less or equal to "+MAX_ID));
        return errors.isEmpty() ? null : errors;
    }
}
