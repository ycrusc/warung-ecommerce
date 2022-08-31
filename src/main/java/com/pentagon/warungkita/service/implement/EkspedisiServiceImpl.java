package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.Ekspedisi;
import com.pentagon.warungkita.repository.EkspedisiRepo;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.service.EkspedisiService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class EkspedisiServiceImpl implements EkspedisiService {

    private static final Logger logger = LogManager.getLogger(EkspedisiServiceImpl.class);

    EkspedisiRepo ekspedisiRepo;

    @Override
    public ResponseEntity<Object> getAll() {
        List<Ekspedisi> result = ekspedisiRepo.findAll();
        if(result.isEmpty()){
          throw new ResourceNotFoundException("Ekspedisi not exist with id :");
            }
        try {
            List<Map<String, Object>> maps = new ArrayList<>();
            logger.info("==================== Logger Start Get All Ekspedisi ====================");
            for(Ekspedisi ekspedisi : result){
                Map<String, Object> ekspedisidata = new HashMap<>();
                ekspedisidata.put("ID            ", ekspedisi.getEkspedisiId());
                ekspedisidata.put("Name          ", ekspedisi.getName());
                maps.add(ekspedisidata);
                logger.info("Code   :"+ekspedisi.getEkspedisiId() );
                logger.info("Status :"+ekspedisi.getName() );
                logger.info("------------------------------------");
            }
            logger.info("==================== Logger End  ====================");
            return ResponseHandler.generateResponse("Successfully Get All Ekspedisi!", HttpStatus.OK, result);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, "Table Has No Value!");
        }
    }

    @Override
    public  ResponseEntity <Object> createEkspedisi(Ekspedisi ekspedisi) {
        try {
            ekspedisiRepo.save(ekspedisi);
            Ekspedisi ekspedisiresult = ekspedisiRepo.save(ekspedisi);
            Map<String, Object> ekspedisiMap = new HashMap<>();
            List<Map<String, Object>> maps = new ArrayList<>();
            ekspedisiMap.put("ID             ", ekspedisiresult.getEkspedisiId());
            ekspedisiMap.put("Username       ", ekspedisiresult.getName());
            maps.add(ekspedisiMap);
            logger.info("==================== Logger Start  ====================");
            logger.info("Code   :"+ekspedisi.getEkspedisiId() );
            logger.info("Status :"+ekspedisi.getName() );
            logger.info("==================== Logger End =================");
            return ResponseHandler.generateResponse("Successfully Created Ekspedisi!", HttpStatus.CREATED, maps);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, "Ekspedisi Already Exist!");
        }
    }

    @Override
    public ResponseEntity<Object> getEkspedisiById(Long Id) {
        try {
            Ekspedisi ekspedisi = ekspedisiRepo.findById(Id)
                    .orElseThrow(() -> new ResourceNotFoundException("Ekspedisi not exist with Id :" + Id));
            Map<String, Object> ekspedisidata = new HashMap<>();
            List<Map<String, Object>> maps = new ArrayList<>();
            ekspedisidata.put("ID            ", ekspedisi.getEkspedisiId());
            ekspedisidata.put("Name          ", ekspedisi.getName());
            maps.add(ekspedisidata);

            logger.info("==================== Logger Start  ====================");
            logger.info("Code   :"+ekspedisi.getEkspedisiId() );
            logger.info("Status :"+ekspedisi.getName() );
            logger.info("==================== Logger End =================");
            return ResponseHandler.generateResponse("Successfully Get Ekspedisi By ID!", HttpStatus.OK, maps);
        } catch (Exception e) {
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!" );
        }

    }

    @Override
    public ResponseEntity<Object> deleteEkspedisiById(Long Id) throws ResourceNotFoundException{
        Optional<Ekspedisi> optionalEkspedisi = ekspedisiRepo.findById(Id);
        if(optionalEkspedisi.isEmpty()){
            throw new ResourceNotFoundException("Ekspedisi not exist with id :" + Id);
        }
        try {
            Ekspedisi ekspedisi = ekspedisiRepo.getReferenceById(Id);
            this.ekspedisiRepo.delete(ekspedisi);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            logger.info("======== Logger Start   ========");
            logger.info("Payment deleted " + response);
            logger.info("======== Logger End   ==========");
            return ResponseHandler.generateResponse("Successfully Delete Ekspedisi! ", HttpStatus.OK, response);
        } catch (ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.NOT_FOUND, "Data Not Found!" );
        }
    }



    @Override
    public  ResponseEntity<Object> updateEkspedisi(Long Id,Ekspedisi ekspedisiDetails) {

        try {
            Ekspedisi ekspedisi = ekspedisiRepo.findById(Id)
             .orElseThrow(() -> new ResourceNotFoundException("Product Status not exist withId :" + Id));
            ekspedisi.setEkspedisiId(Id);
            ekspedisi.setName(ekspedisiDetails.getName());
            Ekspedisi updatedEkspedisi = ekspedisiRepo.save(ekspedisi);
            logger.info("==================== Logger Start  ====================");
            logger.info(ekspedisi);
            logger.info("==================== Logger End =================");
            return ResponseHandler.generateResponse("Successfully Updated Ekspedisi!",HttpStatus.OK, updatedEkspedisi);
        }catch(Exception e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data Not Found!");
        }
        }
    }




