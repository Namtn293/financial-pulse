package com.vcc.bigdata.stock.infrastructure.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@UtilityClass
public class StockHashUtils {
    public static String hashStock(String symbol, String companyName, String exchange) {
        String raw = String.format("%s|%s|%s",
                Objects.toString(symbol,""),
                Objects.toString(companyName,""),
                Objects.toString(exchange,""));
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static String hashVietcadStock(String industry, String overviewSummary, Long sharesOutstanding, Integer establishedYear) {
        String raw = String.format("%s|%s|%s|%s",
                Objects.toString(industry, ""),
                Objects.toString(overviewSummary, ""),
                Objects.toString(sharesOutstanding, ""),
                Objects.toString(establishedYear, ""));
        return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
    }
}
