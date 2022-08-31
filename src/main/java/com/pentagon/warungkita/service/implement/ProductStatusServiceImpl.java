package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.ProductStatus;
import com.pentagon.warungkita.repository.ProductStatusRepo;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.service.ProductStatusService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductStatusServiceImpl implements ProductStatusService {
    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    ProductStatusRepo productStatusRepo;

    @Override
    public ResponseEntity<Object> getAll(){
    List<ProductStatus> result = productStatusRepo.findAll();
    if(result.isEmpty()){
                throw new ResourceNotFoundException("Product Status not exist with id :");
            }
    try {
        List<Map<String, Object>> maps = new ArrayList<>();
        logger.info("==================== Logger Start Get All Product Status ====================");
        for(ProductStatus productStatus : result){
            Map<String, Object> productStatusMaps = new HashMap<>();
            productStatusMaps.put("ID            ", productStatus.getProductStatusId());
            productStatusMaps.put("Name          ", productStatus.getName());
            maps.add(productStatusMaps);
            logger.info("Code   :"+productStatus.getProductStatusId() );
            logger.info("Status :"+productStatus.getName() );
            logger.info("------------------------------------");
            }
            logger.info("==================== Logger End  ====================");
            return ResponseHandler.generateResponse("Successfully Get All ", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, "Table Has No Value!");
        }
    }

        @Override
    public  ResponseEntity<Object> createProductStatus(ProductStatus productStatusDetails){
            try {
                productStatusRepo.save(productStatusDetails);
                ProductStatus productStatus = productStatusRepo.save(productStatusDetails);
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> maps = new ArrayList<>();
                map.put("ID             ", productStatus.getProductStatusId());
                map.put("Name       ", productStatus.getName());
                maps.add(map);
                logger.info("==================== Logger Start ====================");
                logger.info("Code   :"+productStatus.getProductStatusId() );
                logger.info("Status :"+productStatus.getName() );
                logger.info("==================== Logger End =================");
                return ResponseHandler.generateResponse("Successfully Created Product Status!", HttpStatus.CREATED, maps);
            } catch (Exception e) {
                logger.error("------------------------------------");
                logger.error(e.getMessage());
                logger.error("------------------------------------");
                return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Product Status Already Exist!");
            }
        }

    @Override
    public ResponseEntity<Object>getProductStatusById(Long Id) {

           try {
                ProductStatus productStatus = productStatusRepo.findById(Id)
                        .orElseThrow(() -> new ResourceNotFoundException("Product Status not exist with Id :" + Id));
                Map<String, Object> data = new HashMap<>();
                List<Map<String, Object>> maps = new ArrayList<>();
                data.put("ID            ", productStatus.getProductStatusId());
                data.put("Name          ", productStatus.getName());
                maps.add(data);
                logger.info("==================== Logger Start ====================");
                logger.info("Code   :"+productStatus.getProductStatusId() );
                logger.info("Status :"+productStatus.getName() );
                logger.info("==================== Logger End =================");
                return ResponseHandler.generateResponse("Successfully Get Product Status By ID!", HttpStatus.OK, maps);
            } catch (Exception e) {
                logger.error("------------------------------------");
                logger.error(e.getMessage());
                logger.error("------------------------------------");
                return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!" );
            }
    }
            @Override
            public  ResponseEntity<Object> deleteProductStatusById (Long Id) throws ResourceNotFoundException{
                Optional<ProductStatus> optionalProductStatus = productStatusRepo.findById(Id);
                if (optionalProductStatus.isEmpty()) {
                    throw new ResourceNotFoundException("Product Status not exist with id :" + Id);
                }
                ProductStatus productStatus = productStatusRepo.getReferenceById(Id);
                try {
                    productStatusRepo.delete(productStatus);
                    Map<String, Boolean> response = new HashMap<>();
                    response.put("deleted", Boolean.TRUE);
                    logger.info("======== Logger Start   ========");
                    logger.info("Payment deleted " + response);
                    logger.info("======== Logger End   ==========");
                    return ResponseHandler.generateResponse("Successfully Delete Produk Status! ", HttpStatus.OK, response);
                } catch (ResourceNotFoundException e){
                    logger.error("------------------------------------");
                    logger.error(e.getMessage());
                    logger.error("------------------------------------");
                    return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!" );
                }
            }

            @Override
            public ResponseEntity<Object> updateProductStatus (Long Id, ProductStatus productStatus) throws ResourceNotFoundException {
               try {
                    ProductStatus productStatuses = productStatusRepo.findById(Id)
                            .orElseThrow(() -> new ResourceNotFoundException("Product Status not exist withId :" + Id));
                    productStatuses.setProductStatusId(Id);
                    productStatuses.setName(productStatus.getName());
                    ProductStatus result = productStatusRepo.save(productStatuses);
                    logger.info("==================== Logger Start Get All Product Status ====================");
                    logger.info("Code   :"+result.getProductStatusId() );
                    logger.info("Status :"+result.getName() );
                    logger.info("==================== Logger End =================");
                    return ResponseHandler.generateResponse("Successfully Updated Product Status!",HttpStatus.OK,result);
                }catch(Exception e){
                    logger.error("------------------------------------");
                    logger.error(e.getMessage());
                    logger.error("------------------------------------");
                    return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data Not Found!");
                }
            }

        @Override
        public Optional<ProductStatus> getProductStatusBy(Long Id) {
        Optional<ProductStatus> optionalUser = productStatusRepo.findById(Id);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Product Status not exist with id :" + Id);
        }
        return this.productStatusRepo.findById(Id);
    }

}
