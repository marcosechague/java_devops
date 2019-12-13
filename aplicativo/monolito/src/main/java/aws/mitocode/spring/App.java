package aws.mitocode.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@EnableWebMvc
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public AWSCognitoIdentityProviderClient CognitoClient() {        
        return new AWSCognitoIdentityProviderClient(new DefaultAWSCredentialsProviderChain());
	}
	
}
