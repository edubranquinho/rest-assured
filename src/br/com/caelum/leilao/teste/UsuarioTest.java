package br.com.caelum.leilao.teste;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;

import br.com.caelum.leilao.modelo.Leilao;
import br.com.caelum.leilao.modelo.Usuario;

public class UsuarioTest {

	private Usuario mauricio;
	private Usuario guilherme;
	private Leilao geladeira;

	@Before
	public void setUp() {
		mauricio = new Usuario(1l, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		guilherme = new Usuario(2l, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");
		geladeira = new Leilao(1l, "Geladeira", 800.0, mauricio, false);
	}

	@Test
	public void testaConsultaUsuariosXML() {
		XmlPath xmlPath = given().header("Accept", "application/xml").get("/usuarios").andReturn()
				.xmlPath();
		Usuario usuario = xmlPath.getObject("list.usuario[0]", Usuario.class);
		Usuario usuario2 = xmlPath.getObject("list.usuario[1]", Usuario.class);
		assertEquals(usuario, mauricio);
		assertEquals(usuario2, guilherme);
	}

	@Test
	public void testaConsultaUsuariosXMLComoLista() {
		XmlPath xmlPath = given().header("Accept", "application/xml").get("/usuarios").andReturn()
				.xmlPath();
		List<Usuario> usuarios = xmlPath.getList("list.usuario", Usuario.class);
		assertEquals(usuarios.get(0), mauricio);
		assertEquals(usuarios.get(1), guilherme);
	}

	@Test
	public void testaConsultaUsuarioPorIdJSON() {
		JsonPath jsonPath = given().header("Accept", "application/json").param("usuario.id", 1)
				.get("/usuarios/show").andReturn().jsonPath();
		Usuario usuario = jsonPath.getObject("usuario", Usuario.class);
		assertEquals(usuario, mauricio);
	}

	@Test
	public void testaConsultaLeilaoPorIdJSON() {
		JsonPath jsonPath = given().header("Accept", "application/json").param("leilao.id", 1)
				.get("/leiloes/show").andReturn().jsonPath();
		Leilao leilao = jsonPath.getObject("leilao", Leilao.class);
		assertEquals(leilao, geladeira);
	}

	@Test
	public void testaNumeroDeLeiloes() {
		XmlPath xmlPath = given().header("Accept", "application/xml").get("/leiloes/total").andReturn().xmlPath();
		int total = xmlPath.getInt("total");
		assertEquals(total, 2);
	}

	 @Test
	 public void deveAdicionarUmUsuario() {
	 Usuario joao = new Usuario("Joao da Silva", "joao@dasilva.com");
	 XmlPath retorno = given()
	 .header("Accept", "application/xml")
	 .contentType("application/xml")
	 .body(joao)
	 .expect().statusCode(200)
	 .when().post("/usuarios")
	 .andReturn().xmlPath();

	 Usuario resposta = retorno.getObject("usuario", Usuario.class);
	 assertEquals("Joao da Silva", resposta.getNome());
	 assertEquals("joao@dasilva.com", resposta.getEmail());
	 // deletando aqui 
	 given()
	 .contentType("application/xml").body(resposta)
	 .expect().statusCode(200)
	 .when().delete("/usuarios/deleta").andReturn().asString();
	 }
	 
	 @Test
	 public void deveAdicionarUmLeilao(){
		 Leilao leilao = new Leilao(0l, "Televisão", 400.0, guilherme, true); 
		 XmlPath xmlPath = given().header("Accept","application/xml").contentType("application/xml").body(leilao).
		 expect().statusCode(200)
		 .when().post("/leiloes").andReturn().xmlPath();
		 
		 Leilao leilaoRecebido = xmlPath.getObject("leilao", Leilao.class);
		 assertEquals(leilaoRecebido.getUsuario(), guilherme);
		// deletando aqui 
		 given()
		 .contentType("application/xml").body(leilaoRecebido)
		 .expect().statusCode(200)
		 .when().delete("/leiloes/deletar").andReturn().asString();
		 
	 }
	
}
