package testunit.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import aws.mitocode.spring.App;
import aws.mitocode.spring.service.ITipoNegocioService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@TestPropertySource(
		  locations = "classpath:application.yml")
public class ITipoNegocioServiceTest {

	@Autowired
	private ITipoNegocioService tipoNegocioService;
	
    @Test
    public void testGet() {
        assertNotNull(tipoNegocioService.obtenerTodos());
    }
}
