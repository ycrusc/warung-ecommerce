package com.pentagon.warungkita.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommonResponse {
    private String responseCode;
    private String responseMessage;
    private Object data;

    public CommonResponse() {
        this.responseCode = "200";
        this.responseMessage = "Success";
    }

    public CommonResponse(String responseCode, String responseMessage, Object data) {
        this.responseCode = responseCode == null ? "200" : responseCode;
        this.responseMessage = responseMessage == null ? "Success" : responseMessage;
        this.data = data;
    }
}
