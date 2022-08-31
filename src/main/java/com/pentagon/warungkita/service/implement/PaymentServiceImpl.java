package com.pentagon.warungkita.service.implement;

import com.pentagon.warungkita.dto.*;
import com.pentagon.warungkita.exception.ResourceNotFoundException;
import com.pentagon.warungkita.model.*;
import com.pentagon.warungkita.repository.*;
import com.pentagon.warungkita.response.ResponseHandler;
import com.pentagon.warungkita.security.service.UserDetailsImpl;
import com.pentagon.warungkita.service.PaymentService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.pentagon.warungkita.model.Enum.BankList.*;
import static com.pentagon.warungkita.model.Enum.PaymentResponse.*;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    PaymentRepo paymentRepo;

    OrderRepo orderRepo;
    private static final Logger logger = LogManager.getLogger(PaymentServiceImpl.class);

    @Override
    public ResponseEntity<Object> getAllPayment() {
        try {
            List<Payment> payments = paymentRepo.findAll();
            if (payments.isEmpty()) {
                throw new ResourceNotFoundException("Data is Empty");
            }
            List<PaymentResponseDTO> paymentsList = new ArrayList<>();
            logger.info("==================== Logger Start Get All Payment ====================");
            for (Payment dataresult : payments) {
                PaymentResponseDTO paymentResponseDTO = dataresult.convertToResponse();
                paymentsList.add(paymentResponseDTO);
                logger.info("code     :" + dataresult.getPaymentId());
                logger.info("Order Id :" + dataresult.getOrder().getOrderId());
                logger.info("Amount   :" + dataresult.getAmount());
                logger.info("Date     :" + dataresult.getDatePay());
                logger.info("------------------------------------");
            }
            logger.info("==================== Logger End  ====================");
            return ResponseHandler.generateResponse("Success Get By Id", HttpStatus.OK, payments);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }

    @Override
    public ResponseEntity<Object> getPaymentById(Long Id) throws ResourceNotFoundException {

        Optional<Payment> payment = paymentRepo.findById(Id);
        if(payment.isEmpty()){
            throw new ResourceNotFoundException("Booking not exist with id " + Id);
        }
        try {
            Payment paymentget = payment.get();
            PaymentResponseDTO result = paymentget.convertToResponse();
            logger.info("======== Logger Start Find Product List with ID "+Id+ "  ========");
            logger.info("code     :"+paymentget.getPaymentId());
            logger.info("Order Id :"+paymentget.getOrder().getOrderId() );
            logger.info("Amount   :"+paymentget.getAmount() );
            logger.info("Date     :"+paymentget.getDatePay() );
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
    public ResponseEntity<Object> createPayment(PaymentRequestDTO paymentRequestDTO) {
        try{

            if(paymentRequestDTO.getOrder() == null ){
                throw new ResourceNotFoundException("Payment must have order id");
                }
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Payment payment = paymentRequestDTO.convertToEntity();
            Optional <Order> order = orderRepo.findById(paymentRequestDTO.getOrder().getOrderId());
            List<Payment> done = paymentRepo.findByOrderOrderId(paymentRequestDTO.getOrder().getOrderId());
            if(!done.isEmpty()){
               throw new ResourceNotFoundException("Your Order Id is wrong or on process, please check and update your payment" );
            }
            if(order.get().getUserId().getUserId() != userDetails.getUserId()){
                throw new ResourceNotFoundException("You just can pay your order");
                }

            Integer amount = paymentRequestDTO.getAmount();
            if(paymentRequestDTO.getNamaBank() == BANK_SYARIAH_INDONESIA){
                payment.setCcNum("023143213");
                } else if (paymentRequestDTO.getNamaBank()==BANK_BRI){
                    payment.setCcNum("12342342");
                        } else if (paymentRequestDTO.getNamaBank() == BANK_BNI){
                            payment.setCcNum("45234234");
                            }else if (paymentRequestDTO.getNamaBank()==BANK_PERMATA){
                                payment.setCcNum("3423124143");
                                }else if (paymentRequestDTO.getNamaBank()==BANK_BCA){
                                    payment.setCcNum("67547645456");
                                    }else {
                                        payment.setCcNum("8568568568");
                                    }

            if(order.get().getTotal().equals(amount)){
                payment.setResponse(PAYMENT_SUCCES);
                } else {
                    payment.setResponse(WAITING);
                    payment.setActive(true);
                    paymentRepo.save(payment);
                    PaymentResponseDTO result = payment.convertToResponse();
                    return ResponseHandler.generateResponse("Your Amount is not match", HttpStatus.BAD_GATEWAY,result);
                }

            paymentRepo.save(payment);
            PaymentResponseDTO result = payment.convertToResponse();
            logger.info("======== Logger Start   ========");
            logger.info("code     :"+payment.getPaymentId());
            logger.info("Order Id :"+payment.getOrder().getOrderId() );
            logger.info("Amount   :"+payment.getAmount() );
            logger.info("Date     :"+payment.getDatePay() );
            logger.info("=========  Logger End ============");
            return ResponseHandler.generateResponse("Success Create Payment",HttpStatus.CREATED,result);
        }catch (Exception e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST,"Failed Create Database");
        }
    }

    @Override
    public ResponseEntity<Object> deletePaymentById(Long id) throws ResourceNotFoundException{
        Optional<Payment> optionalPayment = paymentRepo.findById(id);
        if(optionalPayment.isEmpty()){
            throw new ResourceNotFoundException("Payment not exist with id " + id);
        }
        try {
            paymentRepo.deleteById(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            logger.info("======== Logger Start   ========");
            logger.info("Payment deleted " + response);
            logger.info("======== Logger End   ==========");
            return ResponseHandler.generateResponse("Success Delete Payment by ID",HttpStatus.OK,response);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }

    @Override
    public ResponseEntity<Object> updatePayment(Long id, PaymentRequestDTO paymentRequestDTO)throws ResourceNotFoundException {
        try {
            Optional<Payment> opPayment = paymentRepo.findById(id);
            Payment optionalPayment = paymentRepo.getReferenceById(id);
            Optional <Order> order = orderRepo.findById(paymentRequestDTO.getOrder().getOrderId());
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(opPayment.isEmpty()){
                throw new ResourceNotFoundException("Payment not exist with id " + id);
            }

            if(paymentRequestDTO.getOrder() == null ){
                throw new ResourceNotFoundException("Payment must have order id");
            }

            if(!Objects.equals(optionalPayment.getOrder().getUserId().getUserId(), userDetails.getUserId())){
                throw new ResourceNotFoundException("You can only update your payment");
            }
            if(!optionalPayment.isActive()){
                throw new ResourceNotFoundException("Payment is DONE");
            }

            Payment payment = paymentRequestDTO.convertToEntity();
            Integer amount = paymentRequestDTO.getAmount();
            if(paymentRequestDTO.getNamaBank() == BANK_SYARIAH_INDONESIA){
                payment.setCcNum("023143213");
                } else if (paymentRequestDTO.getNamaBank()==BANK_BRI){
                    payment.setCcNum("12342342");
                    } else if (paymentRequestDTO.getNamaBank() == BANK_BNI){
                        payment.setCcNum("45234234");
                        }else if (paymentRequestDTO.getNamaBank()==BANK_PERMATA){
                            payment.setCcNum("3423124143");
                            }else if (paymentRequestDTO.getNamaBank()==BANK_BCA){
                                payment.setCcNum("67547645456");
                                }else {
                                    payment.setCcNum("8568568568");
                                }

            if(order.get().getTotal().equals(amount)){
                payment.setResponse(PAYMENT_SUCCES);
            } else {
                payment.setDatePay(LocalDate.now());
                payment.setResponse(WAITING);
                payment.setActive(true);
                payment.setPaymentId(id);
                paymentRepo.save(payment);
                PaymentResponseDTO result = payment.convertToResponse();
                return ResponseHandler.generateResponse("Your Amount is not match", HttpStatus.BAD_GATEWAY,result);
            }
            payment.setDatePay(LocalDate.now());
            payment.setPaymentId(id);
            paymentRepo.save(payment);
            PaymentResponseDTO results = payment.convertToResponse();
            logger.info("======== Logger Start   ========");
            logger.info("code     :"+payment.getPaymentId());
            logger.info("Order Id :"+payment.getOrder().getOrderId() );
            logger.info("Amount   :"+payment.getAmount() );
            logger.info("Date     :"+payment.getDatePay() );
            logger.info("=========  Logger End ============");
            return ResponseHandler.generateResponse("Success Update Payment",HttpStatus.CREATED,results);
        }catch (Exception e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.BAD_REQUEST,"Bad Request");
        }
    }

    @Override
    public Payment getReferenceById(Long Id) {
        return null;
    }

    @Override
    public ResponseEntity<Object>  findByOrderUserIdUsernameContaining() {
        try{
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Payment> payments = paymentRepo.findByOrderUserIdUsernameContaining(userDetails.getUsername());
            if(payments.isEmpty()){
                throw new ResourceNotFoundException("Buyer not have Histori Transaksi ");
            }
            List<PaymentResponseDTO> paymentsList = new ArrayList<>();
            for(Payment dataresult:payments){
                PaymentResponseDTO paymentResponseDTO = dataresult.convertToResponse();
                paymentsList.add(paymentResponseDTO);
            }
            return ResponseHandler.generateResponse("Succes get buyer histori",HttpStatus.OK, paymentsList);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }

    @Override
    public ResponseEntity<Object> cancelPaymnet(Long Id) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Payment payment = paymentRepo.findById(Id)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not exist with Id :" + Id));
            if (payment.getOrder().getUserId().getUserId() != userDetails.getUserId()) {
                throw new ResourceNotFoundException("Only your payment can cancel");
            }
            if (payment.isActive() == false) {
                throw new ResourceNotFoundException("Payment can't to cancel");
            }
            payment.setActive(false);
            payment.setResponse(CANCELLED);
            paymentRepo.save(payment);
            return ResponseHandler.generateResponse("Payment cancelled",HttpStatus.OK, payment.getResponse());
        }catch (ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }

    }

    @Override
    public ResponseEntity<Object> historiSeller() {
        try{
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().
                    getPrincipal();
            List<Payment> payments = paymentRepo.findByOrderOrderProductProductIdUsersUsernameContaining(userDetails.getUsername());
            if(payments.isEmpty()){
                throw new ResourceNotFoundException("Seller not have Histori Transaksi ");
            }
            List<PaymentResponseDTO> paymentsList = new ArrayList<>();
            for(Payment dataresult:payments){
                PaymentResponseDTO paymentResponseDTO = dataresult.convertToResponse();
                paymentsList.add(paymentResponseDTO);
            }
            return ResponseHandler.generateResponse("Succes get seller histori",HttpStatus.OK, paymentsList);
        }catch(ResourceNotFoundException e){
            logger.error("------------------------------------");
            logger.error(e.getMessage());
            logger.error("------------------------------------");
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.NOT_FOUND,"Data not found");
        }
    }
}
