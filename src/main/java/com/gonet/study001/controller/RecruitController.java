package com.gonet.study001.controller;

import com.gonet.study001.domain.Recruit;
import com.gonet.study001.domain.RecruitVO;
import com.gonet.study001.repository.jpa.RecruitRepository;
import com.gonet.study001.service.EmailService;
import com.gonet.study001.service.ThymeleafParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RecruitController {

    private final EmailService emailService;
    private final ThymeleafParser thymeleafParser;
    private final RecruitRepository recruitRepository;

    @GetMapping("/send/recruitForm")
    public String recruitForm() {
        return "recruitForm";
    }


    @Value("${file-upload-dir}")
    private String fileUploadDir;

    /**
     * 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음
     *
     * @param file
     * @param recruitVO
     * @param model
     * @return
     * @throws Exception
     */
    @PostMapping("/send/recruitInsert")
    public String recruitInsert(MultipartFile file, RecruitVO recruitVO, ModelMap model) throws Exception {

        recruitVO.setTitle("이력서");
        String outputFolder = fileUploadDir + File.separator + "gonet";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();

        recruitVO.setTitle("이력서");
        String name = recruitVO.getName();

        String fileName = "";
        String htmlName = "email";

        if (file != null) {

            fileName = "R" + date.getTime() + "_" + file.getOriginalFilename();

            log.info("첨부파일 file.getOriginalFilename() = {}", file.getOriginalFilename());
            log.info("첨부파일 file.getName() = {}", file.getName());
            log.info("첨부파일 fileName = {}", fileName);

            File fDir = new File(outputFolder);
            if (!fDir.exists()) {
                fDir.mkdirs();
            }

            file.transferTo(new File(outputFolder + File.separator + fileName));

            // 파일을 절대 경로를 보낸다. FileInputStream 으로 보낸다.
            recruitVO.setPhoto(outputFolder + File.separator + fileName);
            log.info("===== recruitVO.getPhoto() = {}", recruitVO.getPhoto());
        }

        // pdf 만들기
        String pdfFileName = "R" + date.getTime() + "_" + name + ".pdf";
        String html = thymeleafParser.parseThymeleafTemplate(recruitVO, "email");
        thymeleafParser.generatePdfFromHtml(html, pdfFileName);


        // 테이블 저장
        recruitVO.setAtchFile1(fileName);
        recruitVO.setAtchFile2(pdfFileName);
        Recruit recruit = new Recruit(recruitVO.getName(), recruitVO.getEmail(), recruitVO.getAge(), recruitVO.getAddr(), recruitVO.getSchool(), recruitVO.getPhoto(), recruitVO.getAtchFile1(), recruitVO.getAtchFile2());
        recruitRepository.save(recruit);


        // return ResponseEntity.ok(code);
        return "redirect:/mng/recruit/list";
    }
}