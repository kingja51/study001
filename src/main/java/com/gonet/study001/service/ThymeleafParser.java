package com.gonet.erp.service;


import com.acego.domain.ExcelDataVO;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThymeleafParser {


    private final SpringTemplateEngine templateEngine;

    public String parseThymeleafTemplate(ExcelDataVO dataVO, String type) {

        return setContext(dataVO, type);
    }

    public void generatePdfFromHtml(String html, String fileName, String password) throws IOException, DocumentException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        String yearMon = dateFormat.format(date);

        String outputFolder = System.getProperty("user.home") + File.separator + "gonet" + File.separator + yearMon;
        String filePath = outputFolder + File.separator +fileName;

        log.info("pdf 생성 위치   = {}", filePath);

        File dir = new File(outputFolder);
        if(!dir.exists()) {
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


        if(password == null || "".equals(password)) {
            password = "acego.net";
        }
        else {
            password = password.replaceAll("@agencygo.net", "").replaceAll("@naver.com", "").replaceAll("@gmail.com", "");
        }


        //pdf파일 암호설정
        File file = new File(filePath);
        PDDocument pdDocument = PDDocument.load(file); // 파일에 암호가 있으면 두번째 인자에 입력
        AccessPermission accessPermission = new AccessPermission();
        StandardProtectionPolicy standardProtectionPolicy = new StandardProtectionPolicy(password, password, accessPermission); // param : ownerpassword, userpassword, AccessPermission
        standardProtectionPolicy.setEncryptionKeyLength(128); // 암호화 키길이(40, 128, 256) 이외 값 exception
        standardProtectionPolicy.setPermissions(accessPermission);
        pdDocument.protect(standardProtectionPolicy);
        pdDocument.save(filePath);
        pdDocument.close();

    }


    // thymeleaf를 통한 html 적용
    public String setContext(ExcelDataVO dataBean, String type) {
        Context context = new Context();
        context.setVariable("dataBean", dataBean);
        return templateEngine.process("email/" + type, context);
    }

}
