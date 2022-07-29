package com.warehousekeeper.root.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AuthenticationDTO {
    @NotEmpty(message = "Login should not be empty")
    @Size(min = 3, max = 100, message = "Login should be between 2 and 100 characters")
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
