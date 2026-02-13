package eu.nabahilfe.webapp.registration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterConfirmForm {
    @NotBlank
    private String code;

    @NotBlank
    @Size(min = 12, message = "Das Passwort muss mindestens 12 Zeichen lang sein.")
    private String password;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
