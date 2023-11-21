package com.gonet.study001.service;

import com.gonet.study001.domain.RecruitVO;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThymeleafParser {


    @Value("${file-upload-dir}")
    private String fileUploadDir;

    private final SpringTemplateEngine templateEngine;

    public String parseThymeleafTemplate(RecruitVO dataVO, String type) {
        return setContext(dataVO, type);
    }

    public void generatePdfFromHtml(String html, String fileName) throws IOException, DocumentException {

        String outputFolder = fileUploadDir + File.separator + "gonet";
        String filePath = outputFolder + File.separator + fileName;

        log.info("pdf 생성 위치   = {}", filePath);

        File dir = new File(outputFolder);
        if (!dir.exists()) {
            dir.mkdirs();   // 디렉토리 생성
        }

        OutputStream outputStream = new FileOutputStream(filePath);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver()
                .addFont(
                        new ClassPathResource("/templates/font/NanumBarunGothic/NanumBarunGothic.ttf")
                                .getURL()
                                .toString(),
                        BaseFont.IDENTITY_H,
                        BaseFont.EMBEDDED);
        renderer.setDocumentFromString(html);
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }


    // thymeleaf를 통한 html 적용
    public String setContext(RecruitVO dataBean, String type) {
        Context context = new Context();
        context.setVariable("resultBean", dataBean);
        return templateEngine.process("email/" + type, context);
    }

}
