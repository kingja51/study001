package com.gonet.study001.controller;


import com.gonet.study001.domain.RecruitVO;
import com.gonet.study001.service.EmailService;
import com.gonet.study001.service.ThymeleafParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmailRestController {

    private final EmailService emailService;
    private final ThymeleafParser thymeleafParser;


    @Value("${file-upload-dir}")
    private String fileUploadDir;

    // 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환하도록 작성하였음
    @PostMapping("/send/email")
    public ResponseEntity sendJoinMail(MultipartFile file, RecruitVO recruitVO, ModelMap model) throws Exception {
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
        String html = thymeleafParser.parseThymeleafTemplate(recruitVO, "email");

        log.info("===== html = {}", html);

        String pdfFileName = "R" + date.getTime() + "_" + name + ".pdf";
        thymeleafParser.generatePdfFromHtml(html, pdfFileName);

        String code = emailService.sendMail(recruitVO, htmlName, fileName, pdfFileName);

        log.info("메일 발송 결과 코드 = {}", code);

        return ResponseEntity.ok(code);
    }

}