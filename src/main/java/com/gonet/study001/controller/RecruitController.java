package com.gonet.study001.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class RecruitController {
    @GetMapping("/send/recruitForm")
    public String recruitForm() {
        return "recruitForm";
    }

}
