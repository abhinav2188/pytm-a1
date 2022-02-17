package com.paytm.assignment1.filters;

import com.paytm.assignment1.exceptions.UserAuthorizationException;
import com.paytm.assignment1.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserAuthorizationFilter.class);


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){

        logger.trace("Authorization Filter: shouldNotFilter()");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(SecurityContextHolder.getContext().getAuthentication() == null || auth instanceof AnonymousAuthenticationToken){
            logger.debug("no authentication or anonymous user");
            return true;
        }

        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.debug("Roles : "+userDetails.getAuthorities());
        String requestURI = request.getRequestURI();
        logger.debug("request URI: "+requestURI);

        if(requestURI.equals("/user/all")) {
            logger.debug("non filter uri");
            return true;
        }

        if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            logger.debug("ADMIN role");
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        logger.trace("Authorization Filter : doFilter()");

        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String requestURI = request.getRequestURI();

        logger.debug("request URI: "+requestURI);

        try {
            if(requestURI.startsWith("/user/")) {
                int userId = Integer.parseInt(requestURI.substring(6));
                logger.debug("userId: " + userId);
                if (!userRepository.existsByUserNameAndId(userDetails.getUsername(), userId)) {
                    throw new UserAuthorizationException(userId);
                }
            }
            if(requestURI.startsWith("/create-wallet/")){
                String userMobile = requestURI.substring(15);
                logger.debug("userMobile: "+userMobile);
                if(!userRepository.existsByUserNameAndMobile(userDetails.getUsername(), userMobile)){
                    throw new UserAuthorizationException(userMobile);
                }
            }
            if(requestURI.startsWith("/wallet/")){
                String userMobile = requestURI.substring(8);
                logger.debug("userMobile: "+userMobile);
                if(!userRepository.existsByUserNameAndMobile(userDetails.getUsername(), userMobile)){
                    throw new UserAuthorizationException(userMobile);
                }
            }
            if(requestURI.startsWith("/add-balance")){
                String userMobile = request.getParameter("mobile");
                logger.debug("userMobile: "+userMobile);
                if(!userRepository.existsByUserNameAndMobile(userDetails.getUsername(), userMobile)){
                    throw new UserAuthorizationException(userMobile);
                }
            }

        }catch(UserAuthorizationException ex){
            logger.debug("sending error 403");
            response.sendError(403,ex.getMessage());
            return;
        }

        filterChain.doFilter(request,response);

    }

}