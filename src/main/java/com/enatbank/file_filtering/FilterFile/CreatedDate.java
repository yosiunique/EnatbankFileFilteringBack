package com.enatbank.file_filtering.FilterFile;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatedDate {

    private LocalDate startDate;

    private LocalDate endDate;
}
