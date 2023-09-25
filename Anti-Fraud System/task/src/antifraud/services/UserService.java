package antifraud.services;

import antifraud.dto.UserAccessDto;
import antifraud.dto.UserRoleDTO;
import antifraud.exceptions.UnsupportedException;
import antifraud.exceptions.EntityAlreadyExistException;
import antifraud.exceptions.EntityNotFoundException;
import antifraud.models.Userr;
import antifraud.repositoreis.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class UserService {

    public UserRepository userRepository ;

    PasswordEncoder encoder ;



    public Userr register(Userr user ){
        Optional<Userr> user1 = this.userRepository.findByUsernameIgnoreCase(user.getUsername());
        if(user1.isPresent()){
            throw new EntityAlreadyExistException("User with username "+user.getUsername()+" already exist");

        }
        String role ;
        if(userRepository.findAll().isEmpty()){
            role ="ADMINISTRATOR" ;
        }else {
            role = "MERCHANT" ;
        }
        user.setRole(role);
        user.setLocked(!role.equals("ADMINISTRATOR"));

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;

    }

    public Optional<Userr> getUserByUsername(String username){
        return userRepository.findByUsernameIgnoreCase(username) ;
    }


    public List<Userr> getAll(){
        return userRepository.findAll();
    }

    public void deleteUser(String username) {
        Userr user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException(username));

        userRepository.delete(user);
    }

    public Userr updateUserRole(UserRoleDTO userRoleDTO) {
        String username = userRoleDTO.getUsername();

        Userr user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException(username));

        String roleForUpdate = userRoleDTO.getRole();

        if(!List.of("MERCHANT", "SUPPORT").contains(roleForUpdate)){
            throw new UnsupportedException(roleForUpdate);
        }

        if (roleForUpdate.equals(user.getRole())) {
            throw new EntityAlreadyExistException(roleForUpdate);

        }
        user.setRole(roleForUpdate);
        userRepository.save(user);
        log.info("User role was updated {} ", user);
        return user;

        }

    public String updateAccess(UserAccessDto userAccessDto) {
        String username = userAccessDto.getUsername();

        Userr user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UnsupportedException(username));

        if(userAccessDto.getOperation().equals("LOCK")){
            user.setLocked(true);
            userRepository.save(user);
            return "User " +username+ " locked!" ;
        } else if (userAccessDto.getOperation().equals("UNLOCK")) {
            user.setLocked(false);
            userRepository.save(user);
            return "User " +username+ " unlocked!" ;
        } else{
            throw new UnsupportedException(username);
        }

    }
}
