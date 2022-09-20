package ecommerce.eco.sport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
/*@Api(value = "User Controller", description = "User functionalities")*/
@CrossOrigin(origins = "*")
public class UserController {
}
