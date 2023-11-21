package com.gonet.study001.service;

import com.gonet.study001.domain.RecruitVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${file-upload-dir}")
    private String fileUploadDir;

    public String sendMail(RecruitVO recruitVO, String htmlName, String fileName, String pdfFileName) throws Exception {
        String toMail = recruitVO.getEmail();
        String title = recruitVO.getName() + " 님의 이력서";

        String outputFolder = fileUploadDir + File.separator + "gonet";


        recruitVO.setTitle(title);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setSubject(title); // 메일 제목
            mimeMessageHelper.setTo(toMail); // 메일 수신자
            mimeMessageHelper.setText(setContext(recruitVO, htmlName), true); // 메일 본문 내용, HTML 여부


            if (fileName != null && !"".equals(fileName)) {
                String filePath = outputFolder + File.separator + fileName;

                File file = new File(filePath);

                log.info("첨부파일 filePath = {}", filePath);

                log.info("첨부파일 file.isFile() = {}", file.isFile());

                if(file.isFile()) {
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath));
                    log.info("첨부파일 fileSystemResource.isFile() = {}", fileSystemResource.isFile());
                    mimeMessageHelper.addAttachment(MimeUtility.encodeText(fileName, "UTF-8", "B"), fileSystemResource);
                }
            }

            if(pdfFileName != null && !"".equals(pdfFileName)) {
                String filePath = outputFolder + File.separator + pdfFileName;
                File file = new File(filePath);

                log.info("첨부파일 pdfFileName = {}", pdfFileName);

                log.info("첨부파일 file.isFile() = {}", file.isFile());

                if(file.isFile()) {
                    FileSystemResource fileSystemResource = new FileSystemResource(new File(filePath));
                    mimeMessageHelper.addAttachment(MimeUtility.encodeText(pdfFileName, "UTF-8", "B"), fileSystemResource);
                }
            }

            javaMailSender.send(mimeMessage);
            return "Success";

        } catch (MessagingException e) {
            log.info("Fail");
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String setContext(RecruitVO recruitVO, String htmlName) { // 타임리프 설정하는 코드
        Context context = new Context();
        context.setVariable("resultBean", recruitVO); // Template에 전달할 데이터 설정
        return templateEngine.process(htmlName, context); // mail.html
    }
}