/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

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

    // username for registration and login is the email adress, so we use the email as username

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
