package uz.optimit.taxi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.optimit.taxi.entity.Attachment;
import uz.optimit.taxi.entity.api.ApiResponse;
import uz.optimit.taxi.exception.FileUploadException;
import uz.optimit.taxi.exception.OriginalFileNameNullException;
import uz.optimit.taxi.exception.RecordNotFoundException;
import uz.optimit.taxi.repository.AttachmentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static uz.optimit.taxi.entity.Enum.Constants.*;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Value("${attach.upload.folder}")
    public String attachUploadFolder;

    public String getYearMonthDay() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        return year + "/" + month; // 2022/03
    }


    public String getExtension(String fileName) {
        if (fileName == null) {
            throw new OriginalFileNameNullException(FILE_NAME_NULL);
        }
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    //    Bitta fileni sestamaga saqlab beradi
    public Attachment saveToSystem(MultipartFile file) {
        try {
            String pathFolder = getYearMonthDay();
            File folder = new File(attachUploadFolder + pathFolder);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename());

            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachUploadFolder + pathFolder + "/" + fileName + "." + extension);
            Files.write(path, bytes).toFile();

            Attachment entity = new Attachment();
            entity.setNewName(fileName);
            entity.setOriginName(file.getOriginalFilename());
            entity.setType(extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            entity.setContentType(file.getContentType());

            return attachmentRepository.save(entity);
        } catch (IOException e) {
            throw new FileUploadException(FILE_COULD_NOT_UPLOADED);
        }

    }

    //    Ko'plab filelar kelsa saqlab beradi
    public List<Attachment> saveToSystemListFile(List<MultipartFile> fileList) {
        List<Attachment> attachments = new ArrayList<>();
        fileList.forEach((file) -> {
            attachments.add(saveToSystem(file));
        });
        return attachments;
    }

    //    Rasmni fileda joylashgan joyini linkini beradi
    public String getUrl(UUID imageId) {
        Attachment attachment = attachmentRepository.findById(imageId).orElseThrow(() -> new RecordNotFoundException(FILE_NOT_FOUND));
        return attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType();
    }

    public String getUrl(Attachment attachment) {
        if (attachment!=null){
            return attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType();
        }else {
            return attachUploadFolder+"avatar.png";
        }
    }

    // Rasmni byte qilib beradi
    public byte[] open(String fileName) {
        try {
            Attachment attachment = getAttachment(fileName);
            Path file = Paths.get(attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Attachment getAttachment(String fileName) {
        String newName = fileName.split("\\.")[0];
        return attachmentRepository.findByNewName(newName).orElseThrow(() -> new RecordNotFoundException(FILE_NOT_FOUND));
    }

//    File systendan ochirib tashlaydi
    public ApiResponse deleteNewNameId(String fileName) {
        try {
            Attachment entity = getAttachment(fileName);
            Path file = Paths.get(attachUploadFolder + entity.getPath() + "/" + fileName);
            Files.delete(file);
            attachmentRepository.deleteById(entity.getId());
            return new ApiResponse(DELETED, true);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
}

