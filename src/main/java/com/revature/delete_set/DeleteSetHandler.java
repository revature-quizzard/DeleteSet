package com.revature.delete_set;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.exceptions.InvalidRequestException;

public class DeleteSetHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final SetRepo setRepo;
    private final UserRepo userRepo;
    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();

    public DeleteSetHandler() {
        this.setRepo = new SetRepo();
        this.userRepo = new UserRepo();
    }

    public DeleteSetHandler(SetRepo setRepo, UserRepo userRepo) {
        this.setRepo = setRepo;
        this.userRepo = userRepo;
    }

    /**
     * Handles a DELETE request to the /sets/id endpoint
     * @Authors Alfonso Holmes
     * @param requestEvent
     * @param context
     * @return
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("RECEIVED EVENT: " + requestEvent);

        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        //getting id out of request body
        String target_id = requestEvent.getPathParameters().get("id");

        // attempting to delete target set based on id
        try{
            boolean success = setRepo.deleteSetById(target_id);
            userRepo.purgeSet(target_id);
            responseEvent.setBody(mapper.toJson(success));
        }catch (InvalidRequestException ire) {
           responseEvent.setStatusCode(400);
        }


        responseEvent.setStatusCode(200);
        return responseEvent;
    }
}
