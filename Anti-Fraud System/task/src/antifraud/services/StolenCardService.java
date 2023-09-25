package antifraud.services;

import antifraud.exceptions.EntityAlreadyExistException;
import antifraud.exceptions.EntityNotFoundException;
import antifraud.exceptions.UnsupportedException;
import antifraud.models.StolenCard;
import antifraud.repositoreis.StolenCardRepository;
import antifraud.utils.CheckCardNumber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StolenCardService {
    private final StolenCardRepository stolenCardRepository ;


    public void save(StolenCard card){
        String cardNumber = card.getNumber();
        if(!CheckCardNumber.isValidCardNumber(cardNumber)){
            throw new UnsupportedException(cardNumber);
        }
        Optional<StolenCard> stolenCard = stolenCardRepository.findStolenCardByNumber(cardNumber);
        if(stolenCard.isPresent()){
            throw new EntityAlreadyExistException(card.getNumber());
        }


        stolenCardRepository.save(card);
    }

    public void deleteStolenCard(String number){
        if(!CheckCardNumber.isValidCardNumber(number)){
            throw new UnsupportedException(number);
        }
        StolenCard stolenCard = stolenCardRepository.findStolenCardByNumber(number)
                .orElseThrow(() -> new EntityNotFoundException(number));

        stolenCardRepository.delete(stolenCard);
    }

    public List<StolenCard> getAll(){
        return stolenCardRepository.findAll();
    }
}
