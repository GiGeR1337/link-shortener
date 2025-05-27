package com.example.tpo10.Models;

import com.example.tpo10.Constraints.UniqueUrl;
import com.example.tpo10.Constraints.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateLinkDTO {
    @Size(min = 5, max = 20, message = "{name.size}")
    private String name;
    @Pattern(regexp = "^https://.*", message = "{url.https}")
    @UniqueUrl
    private String targetUrl;

//    @ValidPassword
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}