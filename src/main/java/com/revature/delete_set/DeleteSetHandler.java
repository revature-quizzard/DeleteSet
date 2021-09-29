package com.revature.delete_set;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.Exceptions.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;

public class DeleteSetHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final SetRepo setRepo = new SetRepo();
    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {

        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        //getting id out of request body
        String target_id = "9078fgrur6fy6gt75ri6";
        Object o = mapper.fromJson(requestEvent.getBody() , Object.class);
        System.out.println("From Delete Handler : " + o.toString());

        // attempting to delete target set based on id
        try{

            boolean success = setRepo.deleteSetById(target_id );
            responseEvent.setBody(mapper.toJson(success));
        }catch (InvalidRequestException ire) {
           responseEvent.setStatusCode(400);
        }


        responseEvent.setStatusCode(200);
        return responseEvent;
    }
}
