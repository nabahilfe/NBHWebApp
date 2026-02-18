package eu.nabahilfe.webapp.registration;

import jakarta.validation.constraints.NotBlank;

public class RegisterConfirmForm {

    @NotBlank
    private String code;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
