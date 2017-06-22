package com.tiangles.storm.network;

import com.tiangles.storm.network.connection.Response;
import com.tiangles.storm.network.connection.ResponseFactory;

public class JResponseFactory implements ResponseFactory {
    public Response createResponse() {
        return new JResponse();
    }
}
