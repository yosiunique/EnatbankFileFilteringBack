//package com.enatbank.file_filtering.FilterFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.net.URLConnection;
//import java.nio.file.*;
//import java.nio.file.attribute.FileTime;
//import java.text.ParseException;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.enatbank.file_filtering.Config.AuthenticatServer;
//import com.enatbank.file_filtering.Config.Location;
//import com.enatbank.file_filtering.FileDetails;
//import com.hierynomus.msdtyp.AccessMask;
//import com.hierynomus.mssmb2.SMB2CreateDisposition;
//import com.hierynomus.mssmb2.SMB2ShareAccess;
//import com.hierynomus.smbj.SMBClient;
//import com.hierynomus.smbj.auth.AuthenticationContext;
//import com.hierynomus.smbj.connection.Connection;
//import com.hierynomus.smbj.session.Session;
//import com.hierynomus.smbj.share.DiskShare;
//import jcifs.internal.fscc.FileInformation;
//import lombok.AllArgsConstructor;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import java.io.File;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.*;
//@Service
//@AllArgsConstructor
//public class FileService {
//
//
//    private final Location location;
//    private final AuthenticatServer authenticatServer;
//
//
//    /**********
//     * view file from specified location
//     *
//     *********/
//    public ResponseEntity<Resource> openFile(String fileName) {
//        System.out.println("Opening file " + fileName);
//        File file = new File(location.getLocation()+fileName);
//        if (!file.exists()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+fileName+"\"");
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType(detectFileType(file)))
//                .headers(headers)
//                .body(new FileSystemResource(file));
//
//    }
//
///****
// *
// * download file by file name filename is unique
// *
// */
//
//public ResponseEntity<byte[]> download(String fileName) {
//
//
//
//        Path filePath = Path.of(location.getLocation()+fileName);
//    System.out.println("Downloading "+filePath);
//        try {
//            byte[] fileBytes = Files.readAllBytes(filePath);
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                    .body(fileBytes);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//
//
//
//    }
//
//    /*****
//     *
//     * detecting what is type of file
//     *
//      */
//
//    private String detectFileType(File file) {
//        try {
//            Path path = file.toPath();
//            String mimeType = Files.probeContentType(path);
//
//            // Method 2: If Files.probeContentType() fails, use URLConnection
//            if (mimeType == null) {
//                mimeType = URLConnection.guessContentTypeFromName(file.getName());
//            }
//
//            // Default if unknown
//            return mimeType != null ? mimeType : "Unknown file type";
//        } catch (IOException e) {
//            return "Error detecting file type: " + e.getMessage();
//        }
//    }
//
//
//    /**
//     *
//     *  finding file using created date of file between start and end date.
//      */
//
//    public List<FileDetails> listFile(CreatedDate createdDate) throws IOException, ParseException {
//        File folder = new File(location.getLocation());
//        File[] listOfFiles = folder.listFiles();
//        List<FileDetails> fileDetailsList = new ArrayList<>();
//        for (File file : listOfFiles) {
//            Path path= Paths.get(location.getLocation(),file.getName());
//            FileTime fileTime = Files.getLastModifiedTime(path);
//            LocalDate dateOfCreated=fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
//            /********
//             * if created date is before start date or the same with start date and
//             * end date is after or the same with created date
//             */
//            if ((dateOfCreated.equals(createdDate.getStartDate())||dateOfCreated.isAfter(createdDate.getStartDate()))&& (dateOfCreated.equals(createdDate.getEndDate())||dateOfCreated.isBefore(createdDate.getEndDate()))) {
//
//                FileDetails fileDetails = new FileDetails();
//                // set file properties
//                fileDetails.setFileName(file.getName());
//                fileDetails.setCdate(dateOfCreated);
//                fileDetails.setFileType(detectFileType(new File(location.getLocation(),file.getName())));
//                fileDetailsList.add(fileDetails);
//            }
//
//        }
//        return  fileDetailsList;
//    }
//
//
//
//
//
//
//
//

//import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
//import com.hierynomus.smbj.SMBClient;
//import com.hierynomus.smbj.auth.AuthenticationContext;
//import com.hierynomus.smbj.connection.Connection;
//import com.hierynomus.smbj.session.Session;
//import com.hierynomus.smbj.share.DiskShare;

////        public static void listFiles() throws IOException {
////
////            String smbServer = "192.168.1.100";  // SMB Server (replace with actual IP)
////            String shareName = "shared_folder";  // Shared folder name
////            String filePath = "path/to/file.txt"; // Path inside SMB share
////            String username = "your-username";
////            String password = "your-password";
////            String domain = "";  // Leave empty if not using a domain
////
////            SMBClient client = new SMBClient();
////
////            try (Connection connection = client.connect(smbServer)) {
////                AuthenticationContext auth = new AuthenticationContext(username, password.toCharArray(), domain);
////                Session session = connection.authenticate(auth);
////                DiskShare share = (DiskShare) session.connectShare(shareName);
////
////                if (share.fileExists(filePath)) {
////                    try (File smbFile = share.openFile(filePath,
////                            EnumSet.of(AccessMask.GENERIC_READ),
////                            null,
////                            SMB2ShareAccess.ALL,
////                            SMB2CreateDisposition.FILE_OPEN,
////                            null);
////                         InputStream inputStream = smbFile.getInputStream()) {
////
////                        System.out.println("Successfully opened: " + filePath);
////                        // TODO: Process the input stream (e.g., read file content)
////
////                    }
////                } else {
////                    System.out.println("File not found: " + filePath);
////                }
////            } catch (Exception e) {
////                System.err.println("SMB Error: " + e.getMessage());
////            }
////        }
////
////
////        }
////
////
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//public void main() {
//
//
//
//    SMBClient client = new SMBClient();
//
//    try (Connection connection = client.connect(location.getSmbServer())) {
//        AuthenticationContext auth = new AuthenticationContext(location.getUsername(),location.getPassword().toCharArray(), location.getDomain());
//        Session session = connection.authenticate(auth);
//        DiskShare share = (DiskShare) session.connectShare(location.getShareName());
//
//        // List all files in the given folder
//        System.out.println("Files in folder: " + location.getFolderPath());
//        for (FileIdBothDirectoryInformation fileInfo : share.list(location.getFolderPath()) ){
//            System.out.println(" - " + fileInfo.getFileName());
//        }
//    } catch (Exception e) {
//        System.err.println("SMB Error: " + e.getMessage());
//    }
//
//}



//    }
//
//
//
//
//
//
//
//
//
//
