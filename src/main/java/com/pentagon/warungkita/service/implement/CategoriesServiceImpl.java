package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.dto.CategoriesRequestDTO;
import com.pentagon.warungkita.dto.CategoriesResponseDTO;
import com.pentagon.warungkita.dto.CategoriesResponsePOST;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.Categories;
import com.pentagon.warungkita.repository.CategoriesRepo;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.service.CategoriesService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepo categoriesRepo;
    private static final Logger logger = LogManager.getLogger(CategoriesServiceImpl.class);

    /**
     * Get All Categories
     * @return categoriesRepo.findAll()
     */
    @Override
    public ResponseEntity<Object> getAll() {
        try {
            List<Categories> categories = categoriesRepo.findAll();
            List<CategoriesResponseDTO> categoriesMaps = new ArrayList<>();
            logger.info("==================== Logger Start Get All Categories     ====================");
            for (Categories dataResult : categories) {
                CategoriesResponseDTO categoriesResponseDTO = dataResult.convertToResponse();
                categoriesMaps.add(categoriesResponseDTO);
                logger.info("Kategori ID       : " + dataResult.getCategoriesId());
                logger.info("Nama Kategori     : " + dataResult.getName());
            }
            logger.info("==================== Logger Start Get All Categories     ====================");
            return ResponseHandler.generateResponse("Successfully Get All Categories", HttpStatus.OK, categoriesMaps);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Table has no value");
        }
    }

    @Override
    public ResponseEntity<Object> getCategoriesById(Long categoriesId) {
        try {
            Optional<Categories> categories = categoriesRepo.findById(categoriesId);
            Categories categoriesGet = categories.get();
            CategoriesResponseDTO result = categoriesGet.convertToResponse();
            logger.info("==================== Logger Start Get By ID Categories     ====================");
            logger.info("Kategori ID       : " + categoriesGet.getCategoriesId());
            logger.info("Nama Kategori     : " + categoriesGet.getName());
            logger.info("==================== Logger Start Get By ID Categories     ====================");
            return ResponseHandler.generateResponse("Successfully Get Categories Id",HttpStatus.OK,result);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }

    @Override
    public ResponseEntity<Object> createCategories(CategoriesRequestDTO categoriesRequestDTO) {
        try{
            Categories categories = categoriesRequestDTO.convertToEntity();
            if(categoriesRequestDTO.getName().isEmpty()) {
                throw new ResourceNotFoundException("Please Add Categories Name");
            }
            categoriesRepo.save(categories);
            CategoriesResponsePOST result = categories.convertToResponsePost();
            logger.info("==================== Logger Start Add New Categories     ====================");
            logger.info("Kategori ID       : " + categories.getCategoriesId());
            logger.info("Nama Kategori     : " + categories.getName());
            logger.info("==================== Logger Start Add New Categories Product     ====================");
            return ResponseHandler.generateResponse("Successfully Add Categories",HttpStatus.CREATED,result);
        }catch (Exception e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST,"Failed Create Categories");
        }
    }

    @Override
    public ResponseEntity<Object> updateCategories(Long categoriesId, CategoriesRequestDTO categoriesRequestDTO) {
        try {
            Categories categories = categoriesRequestDTO.convertToEntity();
            if(categoriesRequestDTO.getName().isEmpty()) {
                throw new ResourceNotFoundException("Please Add Categories Name");
            }
            categories.setCategoriesId(categoriesId);
            Categories responseUpdate = categoriesRepo.save(categories);
            CategoriesResponseDTO responseDTO = responseUpdate.convertToResponse();
            logger.info("==================== Logger Start Get Updated Categories     ====================");
            logger.info("Kategori ID       : " + categories.getCategoriesId());
            logger.info("Nama Kategori     : " + categories.getName());
            logger.info("==================== Logger Start Get Updated Categories     ====================");
            return ResponseHandler.generateResponse("Successfully Update Categories",HttpStatus.CREATED,responseDTO);
        }catch (Exception e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST,"Bad Request");
        }
    }

    @Override
    public ResponseEntity<Object> deleteCategories(Long categoriesId) {
        try {
            categoriesRepo.deleteById(categoriesId);
            Boolean result = Boolean.TRUE;
            logger.info("==================== Logger Start Delete Categories     ====================");
            logger.info(result);
            logger.info("==================== Logger Start Delete Categories     ====================");
            return ResponseHandler.generateResponse("Successfully Delete Categories",HttpStatus.OK,result);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }
}
