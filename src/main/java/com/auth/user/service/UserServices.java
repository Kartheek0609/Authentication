package com.auth.user.service;

import com.auth.user.models.Token;
import com.auth.user.models.User;
import com.auth.user.repositories.TokenRepository;
import com.auth.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TokenRepository tokenRepository;

    public User signUp(String name, String email, String password){
        //Skipping email verification part.
        Optional<User> user= userRepository.findByEmail(email);

        if(user.isPresent()){
            //throwing the error user is already present
        }

        User user1= new User();
        user1.setName(name);
        user1.setEmail(email);
        user1.setHashedPassword(bCryptPasswordEncoder.encode(password));

        return userRepository.save(user1);
    }

    public Token login(String email, String password){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            return null;
        }
        User user1 = user.get();
        if (!bCryptPasswordEncoder.matches(password, user1.getHashedPassword())) {
            // throw password is wrong
            System.out.println("Password is wrong");
            return null;
        }
        Token token = new Token();
        token.setUser(user1);

        token.setExpirydate(get30DaysLaterDate());
        token.setValue(UUID.randomUUID().toString());

        return tokenRepository.save(token);

    }
    private Date get30DaysLaterDate() {

        Date date = new Date();

        // Convert date to calendar
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Add 30 days
        calendar.add(Calendar.DAY_OF_MONTH, 30);

        // extract date from calendar
        return calendar.getTime();
    }

    public void logout(String token) {

        Optional<Token> tokenOptional
                = tokenRepository.findByValueAndIsDeletedEquals(token, false);

        if (tokenOptional.isEmpty()) {
            // throw an exception saying token is not present or already deleted.
            return ;
        }

        Token updatedToken = tokenOptional.get();
        updatedToken.setDeleted(true);
        tokenRepository.save(updatedToken);
    }

    public boolean validateToken(String token) {

        /*
        1. Check if the token is present in db
        2. Check if the token is not deleted
        3. Check if the token is not expired
         */

        Optional<Token> tokenOptional =
                tokenRepository.findByValueAndIsDeletedEqualsAndExpirydateGreaterThan(
                        token, false, new Date());

        return tokenOptional.isPresent();
    }

}
