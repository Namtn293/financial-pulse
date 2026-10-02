package com.vcc.bigdata.stock.infrastructure.adapter.out.ssi.exception;

import lombok.Getter;

@Getter
public class SsiApiException extends Exception{
    private final int errorCode;
    private final String rawResponseBody;

    public SsiApiException(int errorCode, String message, String rawResponseBody) {
        super(message);
        this.errorCode = errorCode;
        this.rawResponseBody = rawResponseBody;
    }
}
