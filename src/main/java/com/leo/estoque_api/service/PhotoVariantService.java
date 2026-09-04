package com.leo.estoque_api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.leo.estoque_api.config.amazons3.BucketS3;
import com.leo.estoque_api.dto.photovariant.PhotoVariantRequestDTO;
import com.leo.estoque_api.dto.photovariant.PhotoVariantResponseDTO;
import com.leo.estoque_api.exceptions.PhotoStorageException;
import com.leo.estoque_api.exceptions.ProductVariantNotFoundException;
import com.leo.estoque_api.model.PhotoVariant;
import com.leo.estoque_api.model.ProductVariant;
import com.leo.estoque_api.repository.PhotoVariantRepository;
import com.leo.estoque_api.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.unit.DataSize;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoVariantService {

    private final PhotoVariantRepository photoRepository;
    private final ProductVariantRepository variantRepository;
    private final BucketS3 bucket;
    private final AmazonS3 amazonS3;

    private static final String MAX_SIZE_PHOTO_MB = "3MB";
    private static final List<String> ALLOWED_CONTENT_TYPE = List.of(
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE
    );

    @Transactional
    public PhotoVariantResponseDTO updatePhoto(UUID productId, UUID variantId, PhotoVariantRequestDTO dto)  {
        ProductVariant variant = variantRepository.findById(productId, variantId)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, variantId));

        validatePhoto(dto);

        String newNameFile = UUID.randomUUID().toString() + "_" + dto.name();
        String photoUrl = uploadPhoto(bucket.getBucketName(), newNameFile, dto);

        PhotoVariant photoVariant = PhotoVariant.builder()
                .productVariant(variant)
                .name(newNameFile)
                .contentType(dto.contentType())
                .size(dto.size())
                .url(photoUrl)
                .build();

        photoVariant = photoRepository.save(photoVariant);

        return new PhotoVariantResponseDTO(
                photoVariant.getId(),
                photoVariant.getName(),
                photoVariant.getContentType(),
                photoVariant.getUrl(),
                photoVariant.getSize()
        );
    }

    private String uploadPhoto(String bucketName, String nameFile, PhotoVariantRequestDTO dto) {
        try {
            String pathFile = getPathFile(nameFile);

            var objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(dto.contentType());
            objectMetaData.setContentLength(dto.size());

            var putObject = new PutObjectRequest(
                    bucketName,
                    pathFile,
                    dto.inputStream(),
                    objectMetaData
            );

            amazonS3.putObject(putObject);

            return amazonS3.getUrl(bucketName, pathFile).toString();
        } catch (Exception e) {
            throw new PhotoStorageException("Cannot possible to upload file from Amazon S3.");
        }
    }

    private void validatePhoto(PhotoVariantRequestDTO dto) {
        DataSize dataSize = DataSize.parse(MAX_SIZE_PHOTO_MB);

        if (dto.size() > dataSize.toBytes()) {
            throw new PhotoStorageException(String.format("Cannot possible ot upload files larger than %s.", MAX_SIZE_PHOTO_MB));
        }

        if (!ALLOWED_CONTENT_TYPE.contains(dto.contentType())) {
            throw new PhotoStorageException("ContentType not allowed.");
        }
    }

    private String getPathFile(String nameFile) {
        return String.format("%s/%s", bucket.getBucketDirectory(), nameFile);
    }

}
