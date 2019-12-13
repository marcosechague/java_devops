package testunit.service;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import aws.mitocode.spring.App;
import aws.mitocode.spring.controller.api.ApiSecurityController;
import aws.mitocode.spring.dto.LoginDTO;
import aws.mitocode.spring.dto.RespuestaApi;
import aws.mitocode.spring.security.AwsCognitoJwtAuthenticationFilter;
import aws.mitocode.spring.service.SecurityService;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment=SpringBootTest.WebEnvironment.MOCK, 
    classes = App.class)
@AutoConfigureMockMvc
@TestPropertySource(
		  locations = "classpath:application.yml")
//@WebMvcTest(ApiSecurityController.class)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mvc;

	@MockBean
	private SecurityService securityService;
	
    @Test
    public void loginOk() throws Exception {

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("admin");
        loginDTO.setPassword("12345678");

        RespuestaApi rptaMock = new RespuestaApi();
        rptaMock.setStatus("OK");
		rptaMock.setAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
		rptaMock.setIdToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
		rptaMock.setRefreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
		rptaMock.setBody("Autenticacion correcta");

        Mockito.when(securityService.getToken(
            loginDTO.getUsername(), 
            loginDTO.getPassword())).thenReturn(rptaMock);

        mvc.perform(MockMvcRequestBuilders
            .post("/api/security/login")
            .content(asJsonString(loginDTO))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK") )
            .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    public void getTokenError() throws Exception {

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mvc.perform(MockMvcRequestBuilders
            .post("/api/security/token")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isForbidden());
            //.andExpect(jsonPath("$.status").value("OK") );
    }

    @Test
    public void getTokenOk() throws Exception {
        AwsCognitoJwtAuthenticationFilter awsFilter = Mockito.mock(AwsCognitoJwtAuthenticationFilter.class);

        MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(ApiSecurityController.class)
            .addFilters(awsFilter)
            .build();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.doNothing().when(awsFilter);

        mockMvc.perform(MockMvcRequestBuilders  
            .post("/api/security/token")
            .header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
    }

    private static String asJsonString(final Object obj) {
    try {
        return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
}
