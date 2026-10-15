package com.ptit.bigdata.stockworker.infrastructure.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@UtilityClass
public class StockHashUtils {

    public static String hashVietcadStock(String industry, String overviewSummary, Long sharesOutstanding, Integer establishedYear) {
        String raw = String.format("%s|%s|%s|%s",
                Objects.toString(industry, ""),
                Objects.toString(overviewSummary, ""),
                Objects.toString(sharesOutstanding, ""),
                Objects.toString(establishedYear, ""));
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }
}
