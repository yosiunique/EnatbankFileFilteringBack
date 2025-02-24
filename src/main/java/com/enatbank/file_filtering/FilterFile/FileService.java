package com.enatbank.file_filtering.FilterFile;


import com.enatbank.file_filtering.Config.Location;
import com.enatbank.file_filtering.FileDetails;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
@Service
@AllArgsConstructor
public class FileService {


    private final Location location;


    /**********
     * view file from specified location
     *
     *********/
    public ResponseEntity<Resource> openFile(String fileName) {

        File file = new File(location.getLocation()+fileName);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+fileName+"\"");
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(detectFileType(file)))
                .headers(headers)
                .body(new FileSystemResource(file));

    }

/****
 *
 * download file by file name filename is unique
 *
 */

public ResponseEntity<byte[]> download(String fileName) {



        Path filePath = Path.of(location.getLocation()+fileName);
    System.out.println("Downloading "+filePath);
        try {
            byte[] fileBytes = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(fileBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }



    }

    /*****
     *
     * detecting what is type of file
     *
      */

    private String detectFileType(File file) {
        try {
            Path path = file.toPath();
            String mimeType = Files.probeContentType(path);

            // Method 2: If Files.probeContentType() fails, use URLConnection
            if (mimeType == null) {
                mimeType = URLConnection.guessContentTypeFromName(file.getName());
            }

            // Default if unknown
            return mimeType != null ? mimeType : "Unknown file type";
        } catch (IOException e) {
            return "Error detecting file type: " + e.getMessage();
        }
    }


    /**
     *
     *  finding file using created date of file between start and end date.
      */

    public List<FileDetails> listFile(CreatedDate createdDate) throws IOException, ParseException {
        File folder = new File(location.getLocation());
        File[] listOfFiles = folder.listFiles();
        List<FileDetails> fileDetailsList = new ArrayList<>();
        List<String> fileList = new ArrayList<>();
        for (File file : listOfFiles) {
            Path path=Paths.get(location.getLocation(),file.getName());
            FileTime fileTime = Files.getLastModifiedTime(path);
            LocalDate dateOfCreated=fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            /********
             * if created date is before start date or the same with start date and
             * end date is after or the same with created date
             */
            if ((dateOfCreated.equals(createdDate.getStartDate())||dateOfCreated.isAfter(createdDate.getStartDate()))&& (dateOfCreated.equals(createdDate.getEndDate())||dateOfCreated.isBefore(createdDate.getEndDate()))) {

                FileDetails fileDetails = new FileDetails();
                // set file properties
                fileDetails.setFileName(file.getName());
                fileDetails.setCdate(dateOfCreated);
                fileDetails.setFileType(detectFileType(new File(location.getLocation(),file.getName())));
                fileDetailsList.add(fileDetails);
            }

        }
        return  fileDetailsList;
    }








}
