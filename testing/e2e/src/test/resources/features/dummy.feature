Feature: Validacion Login App

  	Scenario: Credenciales correctas
		Given un cliente que ingresa a la pagina login
		When el cliente ingresa sus credenciales "admin" y clave "12345678"
		Then se ingresa al sistema con pagina inicial del mapa