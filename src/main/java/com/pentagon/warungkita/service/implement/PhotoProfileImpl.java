package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.controller.PhotoController;
import com.pentagon.warungkita.dto.FileUploadResponse;
import com.pentagon.warungkita.dto.PhotoRequestDTO;
import com.pentagon.warungkita.dto.PhotoResponseDTO;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.PhotoProfile;
import com.pentagon.warungkita.repository.PhotoProfileRepo;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.service.PhotoProfileService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class PhotoProfileImpl implements PhotoProfileService {
    private PhotoProfileRepo photoProfileRepo;
    private static final Logger logger = LogManager.getLogger(PhotoController.class);

    @Override
    public ResponseEntity<Object> createPhoto(PhotoRequestDTO photoRequestDTO, MultipartFile multipartFile) {
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

            PhotoProfile photo = PhotoProfile.builder().photoName(filecode).build();
            photoProfileRepo.save(photo);
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
}
