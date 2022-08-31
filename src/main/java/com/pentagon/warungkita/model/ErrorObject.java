package com.pentagon.warungkita.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ErrorObject {

    private List<String> errorMessage;

    private Integer statusCode;

    private List<String> payload;

    private Date timestamp;
}