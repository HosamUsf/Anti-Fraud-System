package antifraud.services;

import antifraud.exceptions.EntityAlreadyExistException;
import antifraud.exceptions.EntityNotFoundException;
import antifraud.exceptions.UnsupportedException;
import antifraud.models.SuspiciousIP;
import antifraud.repositoreis.SuspiciousIPRepository;
//import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

@Service
@AllArgsConstructor
public class SuspiciousIPService {
    private final SuspiciousIPRepository suspiciousIPRepository ;




    public  List<SuspiciousIP> getAll() {
        return suspiciousIPRepository.findAll();
    }


    public void saveSuspiciousIP(SuspiciousIP ip){
        Optional<SuspiciousIP> suspiciousIP = this.suspiciousIPRepository.findSuspiciousIPByIp(ip.getIp());
        if(suspiciousIP.isPresent()){
            throw new EntityAlreadyExistException("User with username "+ip.getIp()+" already exist");

        }
        suspiciousIPRepository.save(ip);
    }

    public void deleteSuspiciousIP (String ip){
        String regex = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);

        if (!matcher.matches()) {
            throw new UnsupportedException(ip);
        }
        SuspiciousIP suspiciousIP = suspiciousIPRepository.findSuspiciousIPByIp(ip)
                        .orElseThrow(() -> new EntityNotFoundException(ip));

        suspiciousIPRepository.delete(suspiciousIP);
    }

}
