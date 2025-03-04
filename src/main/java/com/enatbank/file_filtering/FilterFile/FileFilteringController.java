package com.enatbank.file_filtering.FilterFile;

import com.enatbank.file_filtering.Employee.Employee;
import com.enatbank.file_filtering.Employee.EmployeeClient;
import com.enatbank.file_filtering.FileDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("filter-file")
public class FileFilteringController {
    @Autowired
    private final  SMB smbClient;
    private final  EmployeeClient employeeClient;
    @Description("search file by created date")
    @PostMapping("search-by-created-date")
    public List<FileDetails> getFile(@RequestBody CreatedDate createdDate) throws IOException, InterruptedException {

        return smbClient.listFile(createdDate);
    }

    @Description("view files")
    @GetMapping("view/{fileName}")
    public ResponseEntity<InputStreamResource> getFilteredFile(@PathVariable("fileName") String fileName) {


        return smbClient.openFile(fileName);

    }

    @Description("download files ")
    @GetMapping("download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileName") String fileName) {
        return smbClient.download(fileName);
    }


    @Description("getting employe id from hr using FignClient")
    @GetMapping("/employeeId")
    public Employee getEmployeeFromToken(JwtAuthenticationToken jwtAuthentication) {
        return employeeClient.getEmployeesByEmployeeId((String) jwtAuthentication.getTokenAttributes().get("employeeID"));


}





}

