package br.com.caelum.leilao.teste;

import static com.jayway.restassured.RestAssured.expect;
import org.junit.Test;

public class WSTest {

	@Test
	public void testaGeracaoDeCookie() {
		expect().cookie("rest-assured", "funciona")
		.when().get("/cookie/teste");
	}

}
