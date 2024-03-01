package antifraud.controllers;


import antifraud.dto.TransactionDTO;
import antifraud.dto.TransactionFeedback;
import antifraud.models.StolenCard;
import antifraud.models.SuspiciousIP;
import antifraud.models.Transaction;
import antifraud.repositoreis.TransactionRepository;
import antifraud.services.StolenCardService;
import antifraud.services.SuspiciousIPService;
import antifraud.services.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("api/antifraud")
public class AntifraudController {

    private final TransactionService transactionService ;
    private final SuspiciousIPService suspiciousIPService ;
    private final StolenCardService   stolenCardService;
    private final TransactionRepository transactionRepository;


    @PostMapping("/transaction")
    public TransactionDTO postTransaction(@Valid @RequestBody Transaction transaction){
        return this.transactionService.addTransaction(transaction) ;
    }


    // ? suspicious-ip

    @PostMapping("/suspicious-ip")
    public SuspiciousIP addSuspiciousIP(@Valid @RequestBody SuspiciousIP suspiciousIP){
        suspiciousIPService.saveSuspiciousIP(suspiciousIP);
        return suspiciousIP ;
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<?> deleteIP(@PathVariable String ip){
        suspiciousIPService.deleteSuspiciousIP(ip);
        return new ResponseEntity<>(Map.of("status" , "IP " + ip+" successfully removed!") , HttpStatus.OK);
    }

    @GetMapping("/suspicious-ip")
    public List<SuspiciousIP> getAllIP(){
        return suspiciousIPService.getAll();
    }

    // ? Stolen card

    @PostMapping("/stolencard")
    public StolenCard addStolenCard(@Valid @RequestBody StolenCard stolenCard){
        stolenCardService.save(stolenCard);
        return stolenCard ;
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<?> deleteStolenCard(@PathVariable String number){
        stolenCardService.deleteStolenCard(number);
        return new ResponseEntity<>(Map.of("status" , "Card " + number+" successfully removed!") , HttpStatus.OK);
    }

    @GetMapping("/stolencard")
    public List<StolenCard> getAllCards(){
        return stolenCardService.getAll();
    }


    @GetMapping("/test")
    public List<Transaction> getAll(){
        return transactionRepository.findAll();
    }



    @PutMapping("/transaction")
    public Transaction addFeedback(@RequestBody TransactionFeedback transactionFeedback){
        return transactionService.addFeedback(transactionFeedback);
    }
}
