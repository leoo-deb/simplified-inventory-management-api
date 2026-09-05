package com.leo.estoque_api.controller;

import com.amazonaws.Response;
import com.leo.estoque_api.dto.photovariant.PhotoVariantRequestDTO;
import com.leo.estoque_api.dto.photovariant.PhotoVariantResponseDTO;
import com.leo.estoque_api.exceptions.EntityNotFoundException;
import com.leo.estoque_api.service.PhotoVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products/{productId}/variants/{variantId}/photo")
public class PhotoVariantController {

    @Autowired
    private PhotoVariantService photoService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoVariantResponseDTO> updatePhoto(@PathVariable UUID productId,
                                                               @PathVariable UUID variantId,
                                                               @RequestParam MultipartFile file)
            throws Exception {
        PhotoVariantRequestDTO photoRequest = new PhotoVariantRequestDTO(
                UUID.randomUUID() + "_" + file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                file.getInputStream()
        );

        PhotoVariantResponseDTO photoResponse = photoService.update(productId, variantId, photoRequest);

        return ResponseEntity.ok(photoResponse);
    }

    @GetMapping
    public ResponseEntity<Void> servingPhoto(@PathVariable UUID productId,
                                             @PathVariable UUID variantId,
                                             @RequestHeader("accept") String acceptsMediaTypes)
            throws HttpMediaTypeNotAcceptableException {
        try {
            List<MediaType> mediaTypes = MediaType.parseMediaTypes(acceptsMediaTypes);
            String url = photoService.getUrl(productId, variantId, mediaTypes);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, url)
                    .build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoVariantResponseDTO> findPhotoById(@PathVariable UUID productId,
                                                                 @PathVariable UUID variantId) {
        PhotoVariantResponseDTO photoResponse = photoService.findById(productId, variantId);
        return ResponseEntity.ok(photoResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> removePhoto(@PathVariable UUID productId,
                                            @PathVariable UUID variantId) {
        photoService.delete(productId, variantId);
        return ResponseEntity.noContent().build();
    }

}
