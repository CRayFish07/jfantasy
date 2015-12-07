package com.fantasy.file.bean.databind;


import com.fantasy.file.bean.FileDetail;
import com.fantasy.file.bean.Image;
import com.fantasy.file.service.FileService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class ImageDeserializer extends JsonDeserializer<Image> {

    private static FileService fileService;

    @Override
    public Image deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String value = jp.getValueAsString();
        if (StringUtil.isBlank(value)) {
            return null;
        }
        String[] arry = value.split(":");
        return new Image(getFileService().getFileDetail(arry[1], arry[0]));
    }

    public FileService getFileService() {
        return fileService == null ? fileService = SpringContextUtil.getBeanByType(FileService.class) : fileService;
    }

}