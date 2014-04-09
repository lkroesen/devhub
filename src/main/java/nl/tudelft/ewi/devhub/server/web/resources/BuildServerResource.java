package nl.tudelft.ewi.devhub.server.web.resources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.tudelft.ewi.devhub.server.backend.BuildsBackend;
import nl.tudelft.ewi.devhub.server.database.controllers.Users;
import nl.tudelft.ewi.devhub.server.database.entities.BuildServer;
import nl.tudelft.ewi.devhub.server.database.entities.User;
import nl.tudelft.ewi.devhub.server.web.errors.ApiError;
import nl.tudelft.ewi.devhub.server.web.templating.TemplateEngine;

import org.eclipse.jetty.util.UrlEncoded;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.persist.Transactional;

@Path("build-servers")
@Produces(MediaType.TEXT_HTML)
public class BuildServerResource {

	private static final long USER_ID = 1;
	
	private final TemplateEngine templateEngine;
	private final BuildsBackend backend;
	private final Users users;

	@Inject
	BuildServerResource(TemplateEngine templateEngine, BuildsBackend backend, Users users) {
		this.templateEngine = templateEngine;
		this.backend = backend;
		this.users = users;
	}

	@GET
	public String showUserPage(@Context HttpServletRequest request, @QueryParam("error") String error) throws IOException {
		User requester = users.find(USER_ID);

		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("user", requester);
		parameters.put("servers", backend.listActiveBuildServers());
		if (!Strings.isNullOrEmpty(error)) {
			parameters.put("error", error);
		}

		List<Locale> locales = Collections.list(request.getLocales());
		return templateEngine.process("build-servers.ftl", locales, parameters);
	}
	
	@GET
	@Path("setup")
	public String showNewBuildServerSetupPage(@Context HttpServletRequest request, @QueryParam("error") String error) 
			throws IOException {
		
		User requester = users.find(USER_ID);
		List<Locale> locales = Collections.list(request.getLocales());
		
		Map<String, Object> parameters = Maps.newHashMap();
		parameters.put("user", requester);
		if (!Strings.isNullOrEmpty(error)) {
			parameters.put("error", error);
		}
		
		return templateEngine.process("build-server-setup.ftl", locales, parameters);
	}
	
	@POST
	@Path("setup")
	public Response addNewBuildServer(@FormParam("name") String name, @FormParam("secret") String secret, @FormParam("host") String host) 
			throws URISyntaxException {
		
		try {
			BuildServer server = new BuildServer();
			server.setHost(host);
			server.setName(name);
			server.setSecret(secret);
			backend.addBuildServer(server);
			
			return Response.seeOther(new URI("/build-servers")).build();
		}
		catch (ApiError e) {
			String error = UrlEncoded.encodeString(e.getResourceKey());
			return Response.seeOther(new URI("/build-servers/setup?error=" + error)).build();
		}
	}
	
	@POST
	@Path("delete")
	@Transactional
	public Response deleteBuildServer(@FormParam("id") long id) throws URISyntaxException {
		try {
			backend.deleteBuildServer(id);
			return Response.seeOther(new URI("/build-servers")).build();
		}
		catch (ApiError e) {
			String error = UrlEncoded.encodeString(e.getResourceKey());
			return Response.seeOther(new URI("/build-servers?error=" + error)).build();
		}
	}
	
}