package com.gonet.study001.util;

import com.lowagie.text.BadElementException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Image;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class B64ImgReplacedElementFactory implements ReplacedElementFactory {

    @Override
    public ReplacedElement createReplacedElement(LayoutContext layoutContext, BlockBox blockBox, UserAgentCallback userAgentCallback, int cssWidth, int cssHeight) {

        Element htmlTag = blockBox.getElement();
        if (htmlTag == null) {
            return null;
        }
        String nodeName = htmlTag.getNodeName();

        if (nodeName.equals("img")) {

            log.info("===== nodeName  = {} ", nodeName);

            String srcAttr = htmlTag.getAttribute("src");

            String idAttr = htmlTag.getAttribute("id");
            /**
             * 이미지 태그에 있는 이미지를 변경하는 함수 이다.
             * 모든 이미지 태그가 변경 됩니다. 만약에 특정 id 만 타게 하는 경우 나머지 이미지는 모두 안 보인다.
             * 따라서 이미지 태그가 여러개 있는 경우 모두 함수를 타게 하고 원하는 이미지만 변경할 수 있게 처리한다.
             */
            FSImage fsImage = buildImage(srcAttr, idAttr, userAgentCallback);

            if (fsImage != null) {
                if (cssWidth != -1 || cssHeight != -1) {
                    fsImage.scale(cssWidth, cssHeight);
                }
                return new ITextImageElement(fsImage);
            }

        }
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void remove(Element element) {

    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener formSubmissionListener) {

    }


    private FSImage buildImage(String srcAttr, String idAttr, UserAgentCallback userAgentCallback) {

        FSImage fsImage;
        InputStream is;

        log.info("===== srcAttr {}", srcAttr);
        if (srcAttr.contains("uloads") && idAttr.contains("photo")) {
            File file = new File(srcAttr);

            log.info("===== srcAttr {}", srcAttr);
            if (!file.isFile()) return null;

            try {
                is = new FileInputStream(file);

                BufferedImage bufferedImage = ImageIO.read(is);
                is.close();
                fsImage = new ITextFSImage(Image.getInstance(bufferedImage, null, false));
            } catch (IOException | BadElementException e) {
                throw new ExceptionConverter(e);
            }
        } else {
            fsImage = userAgentCallback.getImageResource(srcAttr).getImage();
        }
        return fsImage;
    }

}