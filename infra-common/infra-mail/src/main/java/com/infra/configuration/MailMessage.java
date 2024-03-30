package com.infra.configuration;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

/**
 * 邮箱消息构建实体
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MailMessage {

    /**
     * 收件人邮箱地址
     */
    private String receiver;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 附件信息,不可以传null,只可以传空集合
     */
    @Builder.Default
    private List<AnnexFileInfo> annexFiles = Collections.emptyList();

    public String getReceiver() {
        Assert.hasText(this.receiver, "收件人信息不能为空");
        return receiver;
    }

    public String getSubject() {
        Assert.hasText(this.subject, "主题信息不能为空");
        return subject;
    }

    public String getContent() {
        Assert.hasText(this.content, "内容信息不能为空");
        return content;
    }

    public List<AnnexFileInfo> getAnnexFiles() {
        Assert.notNull(this.content, "内容信息不能为空");
        return annexFiles;
    }

    /**
     * 附件信息
     */
    @Data
    public static class AnnexFileInfo {

        private String fileName;

        private byte[] fileContent;

        public AnnexFileInfo(String fileName, byte[] fileContent) {
            this.fileName = fileName;
            this.fileContent = fileContent;
        }
    }
}
