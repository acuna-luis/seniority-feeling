package com.turingtask.seniority;

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

//import es.uca.spifm.citasapi.appointment.Appointment;
//import es.uca.spifm.citasapi.appointment.AppointmentNotAvailableException;
//import es.uca.spifm.citasapi.appointment.AppointmentService;
//import es.uca.spifm.citasapi.appointment.AppointmentType;
import com.turingtask.seniority.user.User;
import com.turingtask.seniority.user.UserNotFoundException;
import com.turingtask.seniority.user.UserService;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Component
public class DialogFlowIntents extends DialogflowApp {

	private static final String NO_USER_MSG = "Lo siento, pero no encuentro tu identificador";


	private DateTimeFormatter isoDateFormatter = DateTimeFormatter.ISO_DATE_TIME;

	@Autowired
	private UserService userService;
	//@Autowired
	//private AppointmentService appointmentService;

	@ForIntent("identification")
	public ActionResponse identificateUserIntent(ActionRequest request) {
		log.debug(request);
		// Read request parameter
		String identityId = (String) request.getParameter("identityId");

		Optional<User> user = userService.findById(identityId);
		ResponseBuilder builder;
		if (user.isPresent()) {

			// Write response
			builder = getResponseBuilder(request);
			builder.add(
					"Hola " + user.get().getName() + ". ¿Cómo te sientes hoy?");

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
					request.getSessionId(), "sentiment", answer, percentage, "" );
			}catch(Exception e){}
		ResponseBuilder builder;
		Map<String, String> params = new HashMap<String, String>();
		ActionContext context = new ActionContext("sentiment-answered", 1);
		builder = getResponseBuilder(request);
		String question1 = userService.getQuestion("question1");

		builder.add(
					"Tu puntaje de sentimiento es: "+percentage+"% veamos, "+question1);
		params.put("SentimentPercentage", percentage+"");
			context.setParameters(params);
			builder.add(context);
		ActionResponse actionResponse = builder.build();

		return actionResponse;
		
	}
	@ForIntent("question1")
	public ActionResponse question1Intent(ActionRequest request) {
		log.debug(request);
		// Read request parameter
		String identityId = (String) request.getParameter("identityId");
		log.debug("identityId:");
		log.debug(identityId);
		identityId="111122";
		String okanswer="sabado";
		Calendar c = Calendar.getInstance();
		int dia =  c.get(Calendar.DAY_OF_WEEK);
		if(dia==Calendar.SUNDAY){
			okanswer="domingo";
		}if(dia==Calendar.MONDAY){
		    okanswer="lunes";
		}if(dia==Calendar.TUESDAY){
		    okanswer="martes";
		}if(dia==Calendar.WEDNESDAY){
			okanswer="miercoles";
		}if(dia==Calendar.THURSDAY){
		    okanswer="jueves";
		}if(dia==Calendar.FRIDAY){
		    okanswer="viernes";
		}
		String answer1=(String)request.getParameter("answer");
		double score=0;
		String emotionalReaction = "";
		if(okanswer.equals(answer1.toLowerCase())){
			score=1;
			emotionalReaction = "Bien has acertado!!, ";
		}
		try{
		userService.insertAnswer(
				request.getSessionId(), "question1", answer1, score, identityId );
		}catch(Exception e){}

		ResponseBuilder builder;
		String question2 = userService.getQuestion("question2");

		Map<String, String> params = new HashMap<String, String>();
		ActionContext context = new ActionContext("question2-asked", 1);
		builder = getResponseBuilder(request);
		builder.add(
			emotionalReaction+"Ahora dime "+question2);
			context.setParameters(params);
			builder.add(context);
		ActionResponse actionResponse = builder.build();
		return actionResponse;
	}

	@ForIntent("question2")
	public ActionResponse question2Intent(ActionRequest request) {
		log.debug(request);
		// Read request parameter
		String identityId = (String) request.getParameter("identityId");
		log.debug("identityId:");
		log.debug(identityId);
		identityId="111122";
		Calendar c = Calendar.getInstance();
		int okanswer =  c.get(Calendar.DAY_OF_MONTH);
		int answer1 = 0;
		try{
		    answer1=Integer.parseInt((String)request.getParameter("answer"));
		}catch(Exception e){}
		double score=0;
		String emotionalReaction = "";
		if(okanswer==answer1){
			score=1;
			emotionalReaction = "Bien has acertado!!, ";
		}
		try{
		userService.insertAnswer(
				request.getSessionId(), "question2", answer1+"", score, identityId );
		}catch(Exception e){}

		ResponseBuilder builder;
		String question3= userService.getQuestion("question3");

		Map<String, String> params = new HashMap<String, String>();
		ActionContext context = new ActionContext("question3-asked", 1);
		builder = getResponseBuilder(request);
		builder.add(
			emotionalReaction+"Ahora dime "+question3);
			context.setParameters(params);
			builder.add(context);
		ActionResponse actionResponse = builder.build();
		return actionResponse;
	}

	@ForIntent("question3")
	public ActionResponse question3Intent(ActionRequest request) {
		log.debug(request);
		// Read request parameter
		String identityId = (String) request.getParameter("identityId");
		log.debug("identityId:");
		log.debug(identityId);
		identityId="111122";
		String okanswer="";
		try{
			okanswer = userService.getExpectedResult("question3", identityId);
		}catch(Exception e){}
		log.debug("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX okanswer:");		
		log.debug(okanswer);		
		String answer=(String)request.getParameter("answer");
		double score=0;
		String emotionalReaction = "";
		if(okanswer.equals(answer.toLowerCase())){
			score=1;
			emotionalReaction = "Bien has acertado!!, ";
		}
		try{
		userService.insertAnswer(
				request.getSessionId(), "question3", answer, score, identityId );
		}catch(Exception e){}

		ResponseBuilder builder;
		String game1 = userService.getQuestion("game1");

		Map<String, String> params = new HashMap<String, String>();
		ActionContext context = new ActionContext("game1-asked", 1);
		builder = getResponseBuilder(request);
		builder.add(
			emotionalReaction+"Un juego: "+game1);
			context.setParameters(params);
			builder.add(context);
		ActionResponse actionResponse = builder.build();
		return actionResponse;
	}
	@ForIntent("game1")
	public ActionResponse game1Intent(ActionRequest request) {
		log.debug(request);
		// Read request parameter
		String identityId = (String) request.getParameter("identityId");
		log.debug("identityId:");
		log.debug(identityId);
		identityId="111122";
		String okanswer="26";
		
		log.debug("okanswer:");		
		log.debug(okanswer);
		String answer=(String)request.getParameter("answer");
		double score=0;
		String emotionalReaction = "";
		if(okanswer.equals(answer.toLowerCase())){
			score=1;
			emotionalReaction = "Bien has acertado!!, ";
		}
		try{
		userService.insertAnswer(
				request.getSessionId(), "game1", answer, score, identityId );
		}catch(Exception e){}

		Optional<User> user = userService.findById(identityId);
		ResponseBuilder builder;
		String game2 = userService.getQuestion("game2");

		Map<String, String> params = new HashMap<String, String>();
		ActionContext context = new ActionContext("game2-asked", 1);
		builder = getResponseBuilder(request);
		builder.add(
			emotionalReaction+"Ahora dime "+game2);
			context.setParameters(params);
			builder.add(context);
		ActionResponse actionResponse = builder.build();
		return actionResponse;
	}

	@ForIntent("game2")
	public ActionResponse game2Intent(ActionRequest request) {
		log.debug(request);
		// Read request parameter
		String identityId = (String) request.getParameter("identityId");
		log.debug("identityId:");
		log.debug(identityId);
		identityId="111122";
		String okanswer="3";
		
		log.debug("okanswer:");		
		log.debug(okanswer);
		String answer=(String)request.getParameter("answer");
		double score=0;
		String emotionalReaction = "";
		if(okanswer.equals(answer.toLowerCase())){
			score=1;
			emotionalReaction = "Bien has acertado!!, ";
		}
		try{
		userService.insertAnswer(
				request.getSessionId(), "game2", answer, score, identityId );
		}catch(Exception e){}

		double suma = Double.parseDouble((String)userService.getQuestionSum(request.getSessionId()));
		double sentiment = Double.parseDouble((String)userService.getQuestionSentiment(request.getSessionId()));
		
		ResponseBuilder builder;

		Map<String, String> params = new HashMap<String, String>();
		ActionContext context = new ActionContext("game2-asked", 1);
		builder = getResponseBuilder(request);
		builder.add(
			emotionalReaction+"Obtuviste "+suma+" de un total de 5 puntos y con un puntaje de sentimientos del "+sentiment+"%");
			context.setParameters(params);
			builder.add(context);
		ActionResponse actionResponse = builder.build();
		return actionResponse;
	}

}