package com.gonet.study001.controller;

import com.gonet.study001.domain.Recruit;
import com.gonet.study001.domain.RecruitVO;
import com.gonet.study001.service.jpa.RecruitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/mng/recruit")
public class RecruitEnvController {
    private final RecruitService recruitService;

    @GetMapping("/list")
    public String list(RecruitVO recruitVO, ModelMap model) {
        Page<Recruit> resultList = recruitService.list(recruitVO);
        model.addAttribute("resultList",resultList);
        return "recruit/mng/list";
    }

    @GetMapping("/view")
    public String view(@RequestParam("id") Long id, RecruitVO recruitVO, ModelMap model) {

        Recruit  resultBean =  recruitService.view(id);
        model.addAttribute("resultBean", resultBean);
        model.addAttribute("id", id);

        return "recruit/mng/view";
    }

    @GetMapping("/modify")
    public String modify(@RequestParam("id") Long id, RecruitVO recruitVO, ModelMap model) {

        Recruit  resultBean =  recruitService.view(id);
        model.addAttribute("resultBean", resultBean);
        model.addAttribute("id", id);

        return "recruit/mng/modify";
    }
    @GetMapping("/update")
    public String update(@RequestParam("id") Long id, RecruitVO recruitVO, ModelMap model) {
        recruitVO.setId(id);
        recruitService.update(recruitVO);
        return "redirect:/mng/recruit/list";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id, RecruitVO recruitVO, ModelMap model) {

        recruitService.delete(id);

        return "redirect:/mng/recruit/list";
    }
}
