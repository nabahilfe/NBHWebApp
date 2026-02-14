package eu.nabahilfe.webapp.registration;

import jakarta.validation.constraints.NotBlank;

public class RegisterConfirmForm {

    @NotBlank
    private String code;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

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
        return username;
    }

    public void setEmail(String email) {
        this.username = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
