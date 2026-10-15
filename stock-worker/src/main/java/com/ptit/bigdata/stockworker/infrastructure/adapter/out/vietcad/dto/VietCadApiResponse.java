package com.ptit.bigdata.stockworker.infrastructure.adapter.out.vietcad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class VietCadApiResponse<T> {
    private int status;
    private int code;
    private String msg;
    private boolean successful;
    private T data;
}
