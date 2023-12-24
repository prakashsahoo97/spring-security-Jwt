package in.ashokit.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import in.ashokit.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//this class ll come into action only after login to validate wheather this user is valid for service 
//access or not by checking the token
@Component
public class SecurityFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		//read token from request header
		String token=request.getHeader("Authorization");
		if(token!=null) {
			//validate & read subject from token
			String username=jwtUtil.getUsername(token);
			
			//check userdetails
			if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				//load user from db
				UserDetails user=userDetailsService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken authentication=
						new UsernamePasswordAuthenticationToken(
								username, user.getPassword(), user.getAuthorities());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		
		filterChain.doFilter(request, response);
		
	}

}
