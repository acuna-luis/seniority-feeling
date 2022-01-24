package com.turingtask.seniority;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
public class DialogFlowWebhookController {

	// private static Logger logger =
	// LoggerFactory.getLogger(DialogFlowWebhookController.class);

	@Autowired
	private DialogFlowIntents dialogFlowIntents;

	@RequestMapping(value = "/dialogflow", method = RequestMethod.POST, produces = { "application/json" })
	String serveAction(@RequestBody String body, @RequestHeader Map<String, String> headers) {
		log.debug(body);
		try {
			return dialogFlowIntents.handleRequest(body, headers).get();
		} catch (InterruptedException | ExecutionException e) {
			return handleError(e);
		}
	}

	private String handleError(Exception e) {
		e.printStackTrace();
		// logger.error("Error in App.handleRequest ", e);
		return "Error handling the intent - " + e.getMessage();
	}

}