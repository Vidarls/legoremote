package com.vidarls.legoremote;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class ChannelOne extends ChannelService {
    public ChannelOne() {
        super(1, ChannelOne.class);
    }
}
