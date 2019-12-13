
Feature: Validacion API Curso

	Background: 
		Given un cliente rest
		When se invoca al servicio login con credenciales "admin" y "12345678"
		Then se obtiene un token de autorizacion

  	Scenario Outline: Validacion StatusCode APIs
		Given un cliente rest
		When se invoca a la url "<endpoint>" 
		Then se obtiene el estado http <httpstatus> 
    	
	   Examples:
	    	|	caso 	|		endpoint 																						| httpstatus 	|
	    	| 	1		|	/api/tiponegocio/listar																| 	200			|
	    	|	2		|   /api/sede/listar?tipoNegocio=1&latitud=-12.10216&longitud=-77.0276488&radio=0.029 	| 	200			|

	
	Scenario Outline: Validacion 
		Given un cliente rest
		When se invoca a la url "<endpoint>" 
		Then se valida que el campo "<campo>" sea "<expected>"
    	
	   Examples:
	    	|	caso 	|		endpoint 													| 		campo			|		expected		|
	    	| 	1		|	/api/feedback/listar?page=0&size=100			| 	$.content[0].id		|			1			|
			| 	2		|	/api/tiponegocio/listar							| 	$[0].descripcion	|		Banca			|
	    	