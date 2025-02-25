package com.enatbank.file_filtering.FilterFile;


import com.enatbank.file_filtering.Config.Location;
import com.enatbank.file_filtering.FileDetails;
import com.hierynomus.msdtyp.FileTime;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import lombok.AllArgsConstructor;
import com.hierynomus.smbj.share.File;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.InputStreamResource;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.time.LocalDate;
import java.time.ZoneId;
@Service
@AllArgsConstructor
public class SMB {

private final Location location;

/**********
 * Open and stream a file from SMB
 *********/

public ResponseEntity<InputStreamResource> openFile(String fileName) {

        SMBClient client = new SMBClient();
        try  {
            Connection connection = client.connect(location.getSmbServer());
            AuthenticationContext auth = new AuthenticationContext(location.getUsername(), location.getPassword().toCharArray(), location.getDomain());
            Session session = connection.authenticate(auth);
            DiskShare share = (DiskShare) session.connectShare(location.getShareName());

            String smbFilePath = location.getFolderPath() + "/" + fileName;
            com.hierynomus.smbj.share.File file = share.openFile(smbFilePath,
                    EnumSet.of(AccessMask.GENERIC_READ),
                    null,
                    SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OPEN,
                    null);

            InputStream inputStream = file.getInputStream();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(detectFileType(file)))
                    .headers(headers)
                    .body(new InputStreamResource(inputStream));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }


    /****
     * Download a file from SMB
     ****/

    public ResponseEntity<byte[]> download(String fileName) {
        SMBClient client = new SMBClient();
        try (Connection connection = client.connect(location.getSmbServer())) {
            AuthenticationContext  auth = new
                           AuthenticationContext(
                            location.getUsername(),
                            location.getPassword().toCharArray(),
                            location.getDomain());
            Session session = connection.authenticate(auth);
            DiskShare share = (DiskShare) session.connectShare(location.getShareName());

            String smbFilePath = location.getFolderPath() + "/" + fileName;
            com.hierynomus.smbj.share.File file = share.openFile(smbFilePath,
                    EnumSet.of(AccessMask.GENERIC_READ),
                    null,
                    SMB2ShareAccess.ALL,
                    SMB2CreateDisposition.FILE_OPEN,
                    null);

            InputStream inputStream = file.getInputStream();
            byte[] fileBytes = inputStream.readAllBytes();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(fileBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    /**
     * List files in the SMB share based on creation date range.
     */
    public List<FileDetails> listFile(CreatedDate createdDate)  {
        SMBClient client = new SMBClient();
        List<FileDetails> fileDetailsList = new ArrayList<>();

        try {
            Connection connection = client.connect(location.getSmbServer());
            AuthenticationContext auth = new AuthenticationContext(location.getUsername(), location.getPassword().toCharArray(), location.getDomain());
            Session session = connection.authenticate(auth);
            DiskShare share = (DiskShare) session.connectShare(location.getShareName());
            share.list(location.getFolderPath()).forEach(file->{
                FileTime creationTime = file.getCreationTime();
                LocalDate dateOfCreated = creationTime.toInstant()
                        .atZone(ZoneId.systemDefault())  // Converts to system default timezone
                        .toLocalDate();
if ((dateOfCreated.equals(createdDate.getStartDate()) || dateOfCreated.isAfter(createdDate.getStartDate())) &&
                        (dateOfCreated.equals(createdDate.getEndDate()) || dateOfCreated.isBefore(createdDate.getEndDate()))) {

                    FileDetails fileDetails = new FileDetails();
                    fileDetails.setFileName(file.getFileName());
                    fileDetails.setCdate(dateOfCreated);
                    fileDetails.setFileType("PDF");
                    fileDetailsList.add(fileDetails);
                }

            });

           connection.close();
        } catch (Exception e) {
            System.out.println("smb client error is"+e.getMessage());
        }

        return fileDetailsList;
    }


    /****
     **detecting file type
     */

    private String detectFileType(File file) {
        try {
            // Extract path from the SMBFile (using the file name)
            Path path = Paths.get(file.getPath());

            // Attempt to detect MIME type using Files.probeContentType()
            String mimeType = Files.probeContentType(path);

            // Method 2: If Files.probeContentType() fails, use URLConnection
            if (mimeType == null) {
                mimeType = URLConnection.guessContentTypeFromName(file.getFileName());
            }

            // Default if unknown
            return mimeType != null ? mimeType : "Unknown file type";
        } catch (IOException e) {
            return "Error detecting file type: " + e.getMessage();
        }
    }




}




