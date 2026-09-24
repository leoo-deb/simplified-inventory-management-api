package com.leo.estoque_api.infra.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.leo.estoque_api.exceptions.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageS3Service {

    private static final CharSequence MAX_SIZE_PHOTO_MB = "3MB";
    private static final List<String> ALLOWED_CONTENT_TYPE = List.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE
    );

    @Value("${storage.bucket-name}")
    private String bucketName;

    @Value("${storage.public-url}")
    private String publicUrl;

    private final AmazonS3 s3Client;

    public String replaceFile(String oldImageUrl, String folder, MultipartFile file)
            throws HttpMediaTypeNotAcceptableException {
        String url = this.uploadFile(file, folder);

        if (oldImageUrl != null) {
            this.removeFile(oldImageUrl);
        }

        return url;
    }

    public String uploadFile(MultipartFile file, String folder) throws HttpMediaTypeNotAcceptableException {
            validateFile(file);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String key = folder + "/" + fileName;

        try {
            var objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(file.getContentType());
            objectMetaData.setContentLength(file.getSize());

            var putObject = new PutObjectRequest(
                    bucketName,
                    key,
                    file.getInputStream(),
                    objectMetaData
            );

            s3Client.putObject(putObject);
            return s3Client.getUrl(bucketName, key).toString();
        } catch (Exception e) {
            throw new StorageException("Cannot did possible to upload file from Amazon S3.");
        }
    }

    public void removeFile(String fileUrl) {
        try {
            String key = fileUrl.replace(publicUrl + "/", "");

            var deleteObject = new DeleteObjectRequest(bucketName, key);
            s3Client.deleteObject(deleteObject);
        } catch (Exception e) {
            throw new StorageException("Cannot did possible to delete file in Amazon S3.");
        }
    }

    private void validateFile(MultipartFile file) throws HttpMediaTypeNotAcceptableException {
        DataSize dataSize = DataSize.parse(MAX_SIZE_PHOTO_MB);

        if (file.getSize() > dataSize.toBytes()) {
            throw new StorageException(
                    String.format("Cannot possible to upload files larger than %s.", MAX_SIZE_PHOTO_MB));
        }

        if (!ALLOWED_CONTENT_TYPE.contains(file.getContentType())) {
            throw new HttpMediaTypeNotAcceptableException(
                    String.format("Request does not accept the ContentType of file '%s'.", file.getContentType()));
        }
    }

}
