package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.photovariant.PhotoVariantRequestDTO;
import com.leo.estoque_api.dto.photovariant.PhotoVariantResponseDTO;
import com.leo.estoque_api.service.PhotoVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        String newNameFile = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        PhotoVariantRequestDTO photoRequest = new PhotoVariantRequestDTO(
                newNameFile,
                file.getContentType(),
                file.getSize(),
                file.getInputStream()
        );

        PhotoVariantResponseDTO photoResponse = photoService.update(productId, variantId, photoRequest);

        return ResponseEntity.ok(photoResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> removePhoto(@PathVariable UUID productId,
                                            @PathVariable UUID variantId) {
        photoService.delete(productId, variantId);
        return ResponseEntity.noContent().build();
    }

}
