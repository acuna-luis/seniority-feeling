package com.turingtask.td;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.actions.api.ActionContext;
import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.google.api.services.actions_fulfillment.v2.model.Argument;
import com.google.api.services.dialogflow_fulfillment.v2.model.QueryResult;
import com.google.gson.internal.LinkedTreeMap;
import com.turingtask.td.user.User;
import com.turingtask.td.user.UserNotFoundException;
import com.turingtask.td.user.UserService;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Component
public class DialogFlowIntents extends DialogflowApp {

	private static final String NO_USER_MSG = "Lo siento, pero no encuentro tu identificador";


	private DateTimeFormatter isoDateFormatter = DateTimeFormatter.ISO_DATE_TIME;

	@Autowired
	private UserService userService;
	Optional<User> user;
	//@Autowired
	//private AppointmentService appointmentService;

	@ForIntent("identification")
	public ActionResponse identificateUserIntent(ActionRequest request) {
		log.debug(request);
		// Read request parameter
		String identityId = (String) request.getParameter("identityId");

		user = userService.findById(identityId);
		ResponseBuilder builder;
		if (user.isPresent()) {

			// Write response
			builder = getResponseBuilder(request);
			builder.add(
					"Hola " + user.get().getName() + ". Por favor coméntanos ¿En qué te podemos ayudar?");

			// Set output context and its parameters
			ActionContext context = new ActionContext("identity-answered", 10);
			Map<String, String> params = new HashMap<String, String>();
			params.put("identityId", user.get().getId());
			params.put("name", user.get().getName());
			context.setParameters(params);
			builder.add(context);

		} else {

			builder = getResponseBuilder(request);
			builder.add(NO_USER_MSG);
		}

		ActionResponse actionResponse = builder.build();

		return actionResponse;
	}

	@ForIntent("sentiment")
	public ActionResponse sentimentIntent(ActionRequest request) throws IOException {
		Path fileName = Path.of("/tmp/tmp.json");
		String requestString = Files.readString(fileName);
		log.debug("requestString:");
		log.debug(requestString);
		int index = requestString.indexOf("score");
		log.debug("index: "+index);
		String scoreString;
		if(index!=-1){
		requestString = requestString.substring(index);
		scoreString = requestString.substring(requestString.indexOf(" ")+1, requestString.indexOf(","));
		log.debug("scoreString: "+scoreString);
		}else{
			scoreString="0";
		}

		double score = Double.parseDouble(scoreString);
		log.debug("score: "+score);
		double percentage = Math.round(((score+1)/1.9*100));
		String answer = (String) request.getParameter("sentimenttext");

		try{
			userService.insertAnswer(
					request.getSessionId(), answer, percentage, (String) user.get().getId());
			}catch(Exception e){}
		ResponseBuilder builder;
		Map<String, String> params = new HashMap<String, String>();
		ActionContext context = new ActionContext("sentiment-answered", 1);
		builder = getResponseBuilder(request);

		builder.add("Muchas gracias por la informacion, en breve te contactaremos a "+
		(String) user.get().getId());					
			builder.add(context);
		ActionResponse actionResponse = builder.build();

		return actionResponse;
		
	}
}