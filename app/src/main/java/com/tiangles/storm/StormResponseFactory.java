package com.tiangles.storm;

import com.tiangles.storm.network.Response;
import com.tiangles.storm.network.ResponseFactory;


public class StormResponseFactory implements ResponseFactory {
    @Override
    public Response createResponse() {
        return new StormResponse();
    }
}
