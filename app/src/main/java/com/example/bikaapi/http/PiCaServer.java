package com.example.bikaapi.http;

import com.hjq.http.config.IRequestServer;
import com.lfkdsk.bika.BikaConfig;

public class PiCaServer implements IRequestServer {
    @Override
    public String getHost() {
        return BikaConfig.BASE_URL_PIKA;
    }
}
