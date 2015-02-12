package com.vidarls.legoremote;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class ChannelFour extends ChannelService {
    public ChannelFour() { super(4, ChannelFour.class); }
}
