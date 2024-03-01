package antifraud.controllers;

import antifraud.dto.UserAccessDto;
import antifraud.dto.UserRoleDTO;
import antifraud.dto.UserStatusDTO;
import antifraud.models.Userr;
import antifraud.models.Views;
import antifraud.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private UserService service ;

    public AuthenticationController(UserService service) {
        this.service = service;
    }

    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public Userr register(@Valid @RequestBody Userr user){
        return service.register(user);
    }


    @JsonView(Views.Public.class)
    @GetMapping("/list")
    public List<Userr> getAllUser(){
        return service.getAll();
    }



    @DeleteMapping("/user/{username}")
    public ResponseEntity<UserStatusDTO>  deleteUser(@PathVariable String username){
        service.deleteUser(username);
        return new ResponseEntity<>(new UserStatusDTO(username, "Deleted successfully!"), HttpStatus.OK);

    }

    @PutMapping("/role")
    @JsonView(Views.Public.class)
    public Userr editRole(@Valid @RequestBody UserRoleDTO userRoleDTO){
        log.info("User role was updated {} ", userRoleDTO.getRole());
        return service.updateUserRole(userRoleDTO) ;
    }

    @PutMapping("/access")
    public ResponseEntity<?> updateAccess(@Valid @RequestBody UserAccessDto userAccessDto){

        return new ResponseEntity<>(Map.of("status" ,service.updateAccess(userAccessDto)),HttpStatus.OK);
    }
}
