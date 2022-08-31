package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.dto.*;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.*;
import com.pentagon.warungkita.repository.*;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.security.service.UserDetailsImpl;
import com.pentagon.warungkita.service.*;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private static final Logger logger = LogManager.getLogger(WishlistServiceImpl.class);

    WishlistRepo wishlistRepo;

    UsersService usersService;

    ProductService productService;
    ProductRepo productRepo;

    @Override
    public ResponseEntity<Object> getAllWishlist() {
        try{
        List<Wishlist> wishlists = wishlistRepo.findAll();
        if(wishlists.isEmpty()){
            throw new ResourceNotFoundException("Data is Empty");
        }
            List<WishlistResponseDTO> productListmaps = new ArrayList<>();
            logger.info("==================== Logger Start Get All Product List ====================");
            for(Wishlist dataresult: wishlists){
                WishlistResponseDTO wishlistResponseDTO = dataresult.convertToResponse();
                productListmaps.add(wishlistResponseDTO);
                logger.info("code :"+dataresult.getWishlistId());
                logger.info("User :"+dataresult.getUser() );
                logger.info("Product :"+dataresult.getProduct() );
                logger.info("------------------------------------");
                WishlistResponseDTO wishlistResponseDTO1 = WishlistResponseDTO.builder()
                        .product(dataresult.convertToResponse().getProduct())
                        .build();
                wishlistResponseDTO1.setProduct(dataresult.getProduct());
                wishlistResponseDTO1.setUser(dataresult.getUser());
            }
            logger.info("==================== Logger End  ====================");
            return ResponseHandler.generateResponse("Succes Get All", HttpStatus.OK,productListmaps);
        }catch (ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Table has no value");
        }
    }

    @Override
    public ResponseEntity<Object> getWishlistById(Long id) throws ResourceNotFoundException {
        Optional<Wishlist> wishlists = wishlistRepo.findById(id);
        if(wishlists.isEmpty()){
            throw new ResourceNotFoundException("Wishlist not exist with id " + id);
        }
        try {
            Wishlist productListget = wishlists.get();
            WishlistResponseDTO result = productListget.convertToResponse();
            logger.info("======== Logger Start Find Product List with ID "+id+ "  ========");
            logger.info("==================== Logger End =================");
            return ResponseHandler.generateResponse("Success Get By Id",HttpStatus.OK,result);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }

    @Override
    public ResponseEntity<Object> createWishlist(WishlistRequestDTO wishlistRequestDTO) {
        try{
            if(wishlistRequestDTO.getProduct().getProductId() == null ){
                throw new ResourceNotFoundException("Wishlist must have product id");
            }
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional <Users> users = usersService.getUserById(userDetails.getUserId());
            Optional <Product> product = productRepo.findById(wishlistRequestDTO.getProduct().getProductId());
            if(product.get().getUsers().getUserId().equals(userDetails.getUserId()) ){
                throw new ResourceNotFoundException("Can't add your own product");
            }
            Wishlist wishlist = Wishlist.builder()
                    .user(users.get())
                    .product(wishlistRequestDTO.getProduct())
                    .build();
            WishlistResponsePOST result = wishlist.convertToResponsePost();
            wishlistRepo.save(wishlist);
            logger.info("======== Logger Start   ========");
            logger.info("User :"+ wishlist.getUser() );
            logger.info("Product :"+ wishlist.getProduct());
            logger.info("==================== Logger End =================");
            return ResponseHandler.generateResponse("Success Create Wishlist",HttpStatus.CREATED,result);
        }catch (Exception e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST,"Failed Create Database");
        }
    }

    @Override
    public ResponseEntity<Object> deleteProductListById(Long id) throws ResourceNotFoundException{
        Optional<Wishlist> optionalProductList = wishlistRepo.findById(id);
        if(optionalProductList.isEmpty()){
            throw new ResourceNotFoundException("Wishlist not exist with id " + id);
        }
        try {
            Wishlist wishlist = wishlistRepo.getReferenceById(id);
            this.wishlistRepo.delete(wishlist);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            logger.info("======== Logger Start   ========");
            logger.info("Product List deleted " + response);
            logger.info("==================== Logger End =================");
            return ResponseHandler.generateResponse("Success Delete Product List by ID",HttpStatus.OK,response);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }

    @Override
    public Wishlist getReferenceById(Long Id) {
        return null;
    }

    @Override
    public ResponseEntity<Object> findByUserUsernameContaining() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Wishlist> wishlists = wishlistRepo.findByUserUsernameContaining(userDetails.getUsername());
        if(wishlists.isEmpty()){
            throw new ResourceNotFoundException("User not have Wishlist");
        }
        try {
            List<Wishlist> test = wishlistRepo.findByUserUsernameContaining(userDetails.getUsername());
            List<WishlistResponseDTO> productListmaps = new ArrayList<>();
            for (Wishlist dataresult : test) {
                WishlistResponseDTO wishlistResponseDTO = dataresult.convertToResponse();
                productListmaps.add(wishlistResponseDTO);
                logger.info("code :" + dataresult.getWishlistId());
                logger.info("User :" + dataresult.getUser());
                logger.info("Product :" + dataresult.getProduct());
                logger.info("------------------------------------");
                WishlistResponseDTO wishlistResponseDTO1 = WishlistResponseDTO.builder()
                        .product(dataresult.convertToResponse().getProduct())
                        .build();
                wishlistResponseDTO1.setProduct(dataresult.getProduct());
                wishlistResponseDTO1.setUser(dataresult.getUser());
            }
            return ResponseHandler.generateResponse("Succes Get Your Wishlist", HttpStatus.OK, productListmaps);

        }catch (ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Table has no value");
        }
    }
}
