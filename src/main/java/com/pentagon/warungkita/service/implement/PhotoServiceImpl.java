package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.controller.PhotoController;
import com.pentagon.warungkita.dto.*;
import com.pentagon.warungkita.dto.PhotoRequestDTO;
import com.pentagon.warungkita.dto.FileUploadResponse;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.Photo;
import com.pentagon.warungkita.repository.PhotoRepo;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.service.PhotoService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.*;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private static final Logger logger = LogManager.getLogger(PhotoController.class);
    private final PhotoRepo photoRepo;

    @Override
    public ResponseEntity<Object> getAll() {
        try {
            List<Photo> photo = photoRepo.findAll();
            if(photo.isEmpty()){
                throw new ResourceNotFoundException("Photo Not Exist");
            }
            List<PhotoResponseDTO> photoMaps = new ArrayList<>();
            logger.info("==================== Logger Start Get All Photos     ====================");
            for (Photo dataResult : photo) {
                PhotoResponseDTO photoResponseDTO = dataResult.convertToResponse();
                photoMaps.add(photoResponseDTO);
                logger.info("Foto ID           : " + dataResult.getPhotoId());
                logger.info("Nama Foto         : " + dataResult.getPhotoName());
                logger.info("-----------------------------------------------");
            }
            logger.info("==================== Logger Start Get All Categories     ====================");
            return ResponseHandler.generateResponse("Successfully Get All Photos", HttpStatus.OK, photoMaps);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table has no value");
        }
    }

    @Override
    public  ResponseEntity<Object> getPhotoById(Long photoId) {
        try {
            Optional<Photo> photo = photoRepo.findById(photoId);
            if(photo.isEmpty()){
                throw new ResourceNotFoundException("Photo not exist with id " + photoId);
            }
            Photo photoGet = photo.get();
            PhotoResponseDTO result = photoGet.convertToResponse();
            logger.info("==================== Logger Start Get By ID Photo     ====================");
            logger.info("Foto ID           : " + result.getKodeFoto());
            logger.info("Nama Foto         : " + result.getNamaFoto());
            logger.info("==================== Logger Start Get By ID Photo     ====================");
            return ResponseHandler.generateResponse("Successfully Get Photo Id",HttpStatus.OK,result);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }

    @Override
    public ResponseEntity<Object> createPhoto(PhotoRequestDTO photoRequestDTO, MultipartFile multipartFile ) {

        try{
            if(photoRequestDTO.getPhotoName().isEmpty()) {
                throw new ResourceNotFoundException("Please Add Photo Name");
            }
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            long size = multipartFile.getSize();

            String filecode = FileUploadUtil.saveFile(fileName, multipartFile);

            FileUploadResponse response = new FileUploadResponse();
            response.setFileName(fileName);
            response.setSize(size);
            response.setDownloadUri("/downloadFile/" + filecode);

            Photo photo = Photo.builder().photoName(filecode).build();
            photoRepo.save(photo);
            PhotoResponseDTO result = photo.convertToResponse();
            logger.info("==================== Logger Start Add New Photo     ====================");
            logger.info("Foto ID           : " + result.getKodeFoto());
            logger.info("Nama Foto         : " + result.getNamaFoto());
            logger.info("==================== Logger Start Add New Photo     ====================");
            return ResponseHandler.generateResponse("Successfully Add Photo", HttpStatus.CREATED,result);
        }catch (Exception e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST,"Failed Create Photo");
        }
    }

    @Override
    public ResponseEntity<Object> updatePhoto(Long photoId, PhotoRequestDTO photoRequestDTO) {
        try {
            Optional<Photo> optionalPhoto = photoRepo.findById(photoId);
            if(optionalPhoto.isEmpty()){
                throw new ResourceNotFoundException("Photo not exist with id " + photoId);
            }
            Photo photo = photoRequestDTO.convertToEntity();
            photo.setPhotoId(photoId);
            Photo responseUpdate = photoRepo.save(photo);
            PhotoResponseDTO responseDTO = responseUpdate.convertToResponse();
            logger.info("==================== Logger Start Get Updated Photo     ====================");
            logger.info("Foto ID           : " + responseDTO.getKodeFoto());
            logger.info("Nama Foto         : " + responseDTO.getNamaFoto());
            logger.info("==================== Logger Start Get Updated Photo     ====================");
            return ResponseHandler.generateResponse("Successfully Update Photo",HttpStatus.CREATED,responseDTO);
        }catch (Exception e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST,"Bad Request");
        }
    }

    @Override
    public  ResponseEntity<Object> deletePhoto(Long photoId) {
       try {
            Optional<Photo> optionalPhoto = photoRepo.findById(photoId);
            if(optionalPhoto.isEmpty()){
                throw new ResourceNotFoundException("Photo not exist with id " + photoId);
            }
            Photo photo = photoRepo.getReferenceById(photoId);
            photoRepo.delete(photo);
            Boolean result = Boolean.TRUE;
            logger.info("==================== Logger Start Delete Photo     ====================");
            logger.info("Deleted : " + result);
            logger.info("==================== Logger Start Delete Photo     ====================");
            return ResponseHandler.generateResponse("Successfully Delete Photo",HttpStatus.OK,result);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }

    @Override
    public ResponseEntity<?> downloadFile(String fileCode) {
        FileDownloadUtil downloadUtil = new FileDownloadUtil();

        Resource resource = null;
        try {
            resource = downloadUtil.getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
}
