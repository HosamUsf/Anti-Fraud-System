package antifraud.services;

import antifraud.dto.TransactionDTO;
import antifraud.dto.TransactionFeedback;
import antifraud.exceptions.EntityNotFoundException;
import antifraud.exceptions.UnsupportedException;
import antifraud.models.Transaction;
import antifraud.repositoreis.StolenCardRepository;
import antifraud.repositoreis.SuspiciousIPRepository;
import antifraud.repositoreis.TransactionRepository;
import antifraud.utils.CheckCardNumber;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@AllArgsConstructor
public class TransactionService {

    private final SuspiciousIPRepository suspiciousIPRepository ;
    private final StolenCardRepository stolenCardRepository;
    private final TransactionRepository transactionRepository ;

    private final List<String> regions = new ArrayList<>(){{
        add("EAP");
        add("ECA");
        add("HIC");
        add("LAC");
        add("MENA");
        add("SA");
        add("SSA");
    }};





    public TransactionDTO addTransaction(Transaction transaction) {
        Set<String> reasons = new TreeSet<>();

        boolean allowed = false;
        boolean manual = false;
        boolean prohibited = false;
        String cardNumber = transaction.getNumber();

        if(!CheckCardNumber.isValidCardNumber(cardNumber)){
            throw new UnsupportedException(cardNumber);
        }


        if(!regions.contains(transaction.getRegion())){
            throw new UnsupportedException(transaction.getRegion());
        }

        //checking for stolen CreditCard
        if(stolenCardRepository.findStolenCardByNumber(cardNumber).isPresent()){
            reasons.add("card-number");
            prohibited = true;
        }

        //checking for SuspiciousIP
        if(suspiciousIPRepository.findSuspiciousIPByIp(transaction.getIp()).isPresent()){
            reasons.add("ip");
            prohibited = true;
        }


        TransactionDTO transactionDTO = new TransactionDTO();
        LocalDateTime end = transaction.getDate();
        LocalDateTime start = end.minusHours(1);


        List<Transaction> transactionsInLastHour = transactionRepository.findAllByDateBetweenAndNumber(start, end, transaction.getNumber());
        transactionsInLastHour.add(transaction);

        long regionsCount = transactionsInLastHour.stream().map(Transaction::getRegion).distinct().count();
        long ipCount = transactionsInLastHour.stream().map(Transaction::getIp).distinct().count();

        if(ipCount > 2){
            reasons.add("ip-correlation");
            if (ipCount == 3) manual = true;
            else prohibited = true;
        }

        if (regionsCount > 2) {
            reasons.add("region-correlation");
            if (regionsCount == 3) manual = true;
            else prohibited = true;
        }

        long amount = transaction.getAmount();


        if(amount <= 200   && !prohibited && !manual){
            allowed = true;
            reasons.add("none");
        }

        if ( amount > 200 &&amount <= 1500   && !prohibited ) {
            reasons.add("amount");
            manual = true;
        }

        if (amount > 1500) {
            reasons.add("amount");
            prohibited=true;
        }

        String info = String.join(", ", reasons);

        if(prohibited){
            transactionDTO.setResult("PROHIBITED");
            transaction.setResult("PROHIBITED");
            transactionDTO.setInfo(info);
        }

        else if(allowed){
            transactionDTO.setResult("ALLOWED");
            transaction.setResult("ALLOWED");
            transactionDTO.setInfo(info);
        }

        else {
            transactionDTO.setResult("MANUAL_PROCESSING");
            transaction.setResult("MANUAL_PROCESSING");
            transactionDTO.setInfo(info);
        }


        transactionRepository.save(transaction);




        return transactionDTO ;
    }

    public Transaction addFeedback(TransactionFeedback transactionFeedback) {
        Transaction transaction = transactionRepository.findById(transactionFeedback.getTransactionId())
                .orElseThrow(() -> new EntityNotFoundException("error"));

        if (!transaction.getFeedback().isEmpty()) throw new ResponseStatusException(HttpStatus.CONFLICT);


        return  transaction ;
    }
}
