package com.leo.estoque_api.infra.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.leo.estoque_api.config.amazons3.S3;
import com.leo.estoque_api.dto.photovariant.PhotoVariantRequestDTO;
import com.leo.estoque_api.exceptions.PhotoStorageException;
import com.leo.estoque_api.exceptions.StorageContentTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageS3Service {

    private static final CharSequence MAX_SIZE_PHOTO_MB = "3MB";
    private static final List<String> ALLOWED_CONTENT_TYPE = List.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE
    );

    private final AmazonS3 amazonS3;
    private final S3 s3;

    public String replaceFile(String nameOldFile, PhotoVariantRequestDTO dto) {
        String url = this.uploadFile(dto);

        if (nameOldFile != null) {
            this.removeFile(nameOldFile);
        }

        return url;
    }

    public String uploadFile(PhotoVariantRequestDTO dto) {
        DataSize dataSize = DataSize.parse(MAX_SIZE_PHOTO_MB);
        String pathFile = getPathFile(dto.name());

        validateFile(dto, dataSize);

        try {
            var objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(dto.contentType());
            objectMetaData.setContentLength(dto.size());

            var putObject = new PutObjectRequest(
                    s3.getBucketName(),
                    pathFile,
                    dto.inputStream(),
                    objectMetaData
            );

            amazonS3.putObject(putObject);
            return this.getUrlFile(dto.name());
        } catch (Exception e) {
            throw new PhotoStorageException("Cannot did possible to upload file from Amazon S3.");
        }
    }

    public void removeFile(String nameFile) {
        try {
            String pathFile = getPathFile(nameFile);

            var deleteObject = new DeleteObjectRequest(s3.getBucketName(), pathFile);
            amazonS3.deleteObject(deleteObject);
        } catch (Exception e) {
            throw new PhotoStorageException("Cannot did possible to delete file in Amazon S3.");
        }
    }

    public String getUrlFile(String nameFile) {
        String pathFile = this.getPathFile(nameFile);
        return amazonS3.getUrl(s3.getBucketName(), pathFile).toString();
    }

    private void validateFile(PhotoVariantRequestDTO dto, DataSize dataSize) {
        if (dto.size() > dataSize.toBytes()) {
            throw new PhotoStorageException(
                    String.format("Cannot possible to upload files larger than %s.", MAX_SIZE_PHOTO_MB));
        }

        if (!ALLOWED_CONTENT_TYPE.contains(dto.contentType())) {
            throw new StorageContentTypeException(
                    String.format("Request does not accept the ContentType of file '%s'.", dto.contentType()));
        }
    }

    private String getPathFile(String nameFile) {
        return String.format("%s/%s", s3.getBucketDirectory(), nameFile);
    }

}
