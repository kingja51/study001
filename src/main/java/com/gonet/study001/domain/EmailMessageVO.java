package com.gonet.study001.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailMessageVO {
    private String to;      // 수신자
    private String subject; // 제목
    private String message; // 메시지
}
