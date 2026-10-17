package com.alihassan.ssimp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleCheckController {

    @GetMapping("/user/check")
    public String userCheck() {
        return "User access confirmed.";
    }

    @GetMapping("/agent/check")
    public String agentCheck() {
        return "Support Agent access confirmed.";
    }

    @GetMapping("/admin/check")
    public String adminCheck() {
        return "Administrator access confirmed.";
    }
}