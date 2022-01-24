package com.turingtask.seniority;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.actions.api.ActionContext;
import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
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

	private static final String NO_USER_MSG = "Lo siento, pero no te tengo registrado en el sistema de salud";

	private static final String NO_APPOINTMENT_MSG = "Lo siento, pero a esa hora no hay disponibilidad de cita con su doctor";

	private DateTimeFormatter isoDateFormatter = DateTimeFormatter.ISO_DATE_TIME;

	@Autowired
	private UserService userService;
	//@Autowired
	//private AppointmentService appointmentService;

	@ForIntent("identification")
	public ActionResponse identificateUserIntent(ActionRequest request) {
		log.debug(request);
		// Read request parameter
		String identityDocument = (String) request.getParameter("identityId");

		Optional<User> user = userService.findById(identityDocument);
		ResponseBuilder builder;
		if (user.isPresent()) {

			// Write response
			builder = getResponseBuilder(request);
			builder.add(
					"Hola " + user.get().getName() + ". ¿Cómo puedo ayudarte?");

			// Set output context and its parameters
			ActionContext context = new ActionContext("ctx-useridentified", 10);
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
/*
	@ForIntent("Mi Próxima Cita Intent")
	public ActionResponse rememberAppointmentIntent(ActionRequest request) {

		// Read context parameter
		ActionContext context = request.getContext("ctx-useridentified");
		String identityNumber = (String) context.getParameters().get("identityDocument");

		ResponseBuilder builder;

		try {
			Optional<Appointment> appointmentOpt = appointmentService.findNextAppointment(identityNumber);

			if (appointmentOpt.isPresent()) {
				Appointment appointment = appointmentOpt.get();
				builder = getResponseBuilder(request);
				builder.add("Su próxima cita es el día " + renderDateTime(appointment.getDateTime()));

			} else {

				builder = getResponseBuilder(request);
				builder.add("No tiene ninguna cita");
			}
		} catch (UserNotFoundException e) {
			builder = getResponseBuilder(request);
			builder.add(NO_USER_MSG);
		}

		ActionResponse actionResponse = builder.build();
		return actionResponse;
	}
*/
	@ForIntent("Solicitar Cita Intent")
	public ActionResponse queryAvaliabilityIntent(ActionRequest request) {

		ResponseBuilder builder = getResponseBuilder(request);

		builder.add("Comenzaremos con algunas preguntas sencillas");
		builder.add("Qué día de la semana es hoy?");

		// Set output context
		ActionContext context = new ActionContext("ctx-dayofweekasked", 5);
		Map<String, String> params = new HashMap<String, String>();
		params.put("lastquestion", "dayofweek");
		context.setParameters(params);
		builder.add(context);

		ActionResponse actionResponse = builder.build();
		return actionResponse;

	}
/*
	@ForIntent("Solicitar Cita Intent - yes")
	public ActionResponse confirmAppointmentIntent(ActionRequest request) {

		// Read user id from the context
		ActionContext context = request.getContext("ctx-useridentified");
		String identityNumber = (String) context.getParameters().get("identityDocument");

		// Read date time from the context
		context = request.getContext("ctx-slotproposed");
		LocalDateTime dateTime = readDateTime(context.getParameters().get("dateTime"));

		// Read appointmentType and subject from the request
		AppointmentType appointmentType = AppointmentType.valueOf((String) request.getParameter("appointmentType"));
		String subject = (String) request.getParameter("subject");

		ResponseBuilder builder = getResponseBuilder(request);

		Appointment appointment;
		try {

			appointment = appointmentService.confirmAppointment(identityNumber, dateTime, appointmentType, subject);

			builder.add("Le confirmo que su próxima cita es el día " + renderDateTime(appointment.getDateTime()));
			builder.removeContext("ctx-slotproposed");

		} catch (UserNotFoundException e) {
			builder.add(NO_USER_MSG);
		} catch (AppointmentNotAvailableException e) {
			builder.add(NO_APPOINTMENT_MSG);
		}

		ActionResponse actionResponse = builder.build();

		return actionResponse;

	}

	@ForIntent("Solicitar Cita Intent - no")
	public ActionResponse cancelAppointmentIntent(ActionRequest request) {

		ResponseBuilder builder = getResponseBuilder(request);
		builder.removeContext("ctx-slotproposed");
		ActionResponse actionResponse = builder.build();

		return actionResponse;

	}

	private String renderDateTime(LocalDateTime dateTime) {
		// TODO Auto-generated method stub
		return dateTime.getDayOfMonth() + " de "
				+ dateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")) + " a las "
				+ dateTime.getHour() + " horas y " + dateTime.getMinute() + " minutos";
	}

	private LocalDateTime readDateTime(Object parameter) {
		String value = "";
		if (parameter instanceof LinkedTreeMap) {
			LinkedTreeMap map = (LinkedTreeMap) parameter;
			value = (String) map.values().stream().findFirst().get();
		} else if (parameter instanceof String) {
			value = (String) parameter;
		}

		return LocalDateTime.parse(value, isoDateFormatter);
	}
*/
}