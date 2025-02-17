package com.enatbank.file_filtering;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileDetails {
    private String fileName;
    private LocalDate cdate;
    private String fileType;


}
