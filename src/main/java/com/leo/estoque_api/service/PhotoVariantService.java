package com.leo.estoque_api.service;

import com.leo.estoque_api.dto.photovariant.PhotoVariantRequestDTO;
import com.leo.estoque_api.dto.photovariant.PhotoVariantResponseDTO;
import com.leo.estoque_api.exceptions.PhotoVariantNotFoundException;
import com.leo.estoque_api.exceptions.ProductVariantNotFoundException;
import com.leo.estoque_api.infra.storage.StorageS3Service;
import com.leo.estoque_api.model.PhotoVariant;
import com.leo.estoque_api.model.ProductVariant;
import com.leo.estoque_api.repository.PhotoVariantRepository;
import com.leo.estoque_api.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import javax.print.attribute.standard.Media;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoVariantService {

    private final PhotoVariantRepository photoRepository;
    private final ProductVariantRepository variantRepository;
    private final StorageS3Service storageS3;

    @Transactional
    public PhotoVariantResponseDTO update(UUID productId, UUID variantId, PhotoVariantRequestDTO dto) {
        ProductVariant variant = variantRepository.findById(productId, variantId)
                .orElseThrow(() -> new ProductVariantNotFoundException(productId, variantId));

        Optional<PhotoVariant> photoVariantExist = photoRepository.findById(productId, variantId);
        String nameOldPhoto = null;

        if (photoVariantExist.isPresent()) {
            nameOldPhoto = photoVariantExist.get().getName();
            photoRepository.delete(photoVariantExist.get());
        }

        String photoUrl = storageS3.replaceFile(nameOldPhoto, dto);

        PhotoVariant photoVariant = PhotoVariant.builder()
                .productVariant(variant)
                .name(dto.name())
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

    @Transactional(readOnly = true)
    public String getUrl(UUID productId, UUID variantId, List<MediaType> acceptMediaTypes) throws HttpMediaTypeNotAcceptableException {
        PhotoVariant photoVariant = photoRepository.findById(productId, variantId)
                .orElseThrow(() -> new PhotoVariantNotFoundException(productId, variantId));

        extracted(acceptMediaTypes, photoVariant);

        return storageS3.getUrlFile(photoVariant.getName());
    }

    public PhotoVariantResponseDTO findById(UUID productId, UUID variantId) {
        PhotoVariant photoVariant = photoRepository.findById(productId, variantId)
                .orElseThrow(() -> new PhotoVariantNotFoundException(productId, variantId));

        return new PhotoVariantResponseDTO(
                photoVariant.getId(),
                photoVariant.getName(),
                photoVariant.getContentType(),
                photoVariant.getUrl(),
                photoVariant.getSize());
    }

    @Transactional
    public void delete(UUID productId, UUID variantId) {
        PhotoVariant photoVariant = photoRepository.findById(productId, variantId)
                .orElseThrow(() -> new PhotoVariantNotFoundException(productId, variantId));

        photoRepository.delete(photoVariant);
        storageS3.removeFile(photoVariant.getName());
    }

    private void extracted(List<MediaType> acceptMediaTypes, PhotoVariant photoVariant) throws HttpMediaTypeNotAcceptableException {
        MediaType mediaTypePhoto = MediaType.parseMediaType(photoVariant.getContentType());

        boolean accept = acceptMediaTypes.stream()
                .anyMatch(mediaType -> mediaType.isCompatibleWith(mediaTypePhoto));

        if (!accept) {
            throw new HttpMediaTypeNotAcceptableException(acceptMediaTypes);
        }
    }

}
