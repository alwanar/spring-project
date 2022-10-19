package com.example.jdbc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProgramDetailDTO {
    private String RequestId;
    private String FIDProgram;

    public String getRequestId() {
        return RequestId;
    }

    @JsonProperty("RequestId")
    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getFIDProgram() {
        return FIDProgram;
    }

    @JsonProperty("FIDProgram")
    public void setFIDProgram(String FIDProgram) {
        this.FIDProgram = FIDProgram;
    }
}
