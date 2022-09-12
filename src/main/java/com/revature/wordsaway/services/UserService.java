package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.requests.LoginRequest;
import com.revature.wordsaway.dtos.requests.NewUserRequest;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.repositories.UserRepository;
import com.revature.wordsaway.utils.CustomExceptions.AuthenticationException;
import com.revature.wordsaway.utils.CustomExceptions.InvalidRequestException;
import com.revature.wordsaway.utils.CustomExceptions.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private static UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public static User register(NewUserRequest request){
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());
        validateEmail(request.getEmail());
        checkAvailableUsername(request.getUsername());
        checkAvailableEmail(request.getEmail());
        String salt = UUID.randomUUID().toString().replace("-","");
        String hashedPassword = hashPassword(request.getPassword().toCharArray(), DatatypeConverter.parseHexBinary(salt));
        User user = new User(
                request.getUsername(),
                hashedPassword,
                salt,
                request.getEmail(),
                1000, //TODO set this to be the average ELO
                0,
                0,
                false
        );
        userRepository.save(user);
        return user;
    }

    public static String login(LoginRequest request) throws AuthenticationException {
        User user = userRepository.findUserByUsername(request.getUsername());
        if (user != null && user.getPassword().equals(
                hashPassword(request.getPassword().toCharArray(), DatatypeConverter.parseHexBinary(user.getSalt()))))
            return TokenService.generateToken(user.getUsername());
        throw new AuthenticationException("Login unsuccessful. Please check username and password.");
    }

    public static User getByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public static List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

    public static void validateUsername(String username) throws InvalidRequestException {
        if(!username.matches("^[A-Za-z\\d]{3,15}$"))
            throw new InvalidRequestException("Username must start with a letter and consist of between 3 and 15 alphanumeric characters or be a valid email address.");
    }

    public static void validatePassword(String password) throws InvalidRequestException {
        if(!password.matches("^[A-Za-z\\d@$!%*?&]{5,30}$"))
            throw new InvalidRequestException("Password must be between 5 and 30 alphanumeric or special characters.");
    }

    public static void validateEmail(String email) throws InvalidRequestException {
        if(!email.matches("^|[A-Za-z0-9][A-Za-z0-9!#$%&'*+\\-/=?^_`{}|]{0,63}@[A-Za-z0-9.-]{1,253}.[A-Za-z]{2,24}$"))
            throw new InvalidRequestException("Password must be between 5 and 30 alphanumeric or special characters.");
    }

    public static void checkAvailableUsername(String username) throws ResourceConflictException {
        if (userRepository.findUserByUsername(username) != null){
            throw new ResourceConflictException("Username is already taken, please choose another.");
        }
    }

    public static void checkAvailableEmail(String email) throws ResourceConflictException {
        if (userRepository.findUserByEmail(email) != null){
            throw new ResourceConflictException("Email is already taken, please choose another.");
        }
    }

    public static String hashPassword(char[] password, byte[] salt) {
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            KeySpec ks = new PBEKeySpec(password, salt, 1024, 128);
            return DatatypeConverter.printHexBinary(f.generateSecret(ks).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
