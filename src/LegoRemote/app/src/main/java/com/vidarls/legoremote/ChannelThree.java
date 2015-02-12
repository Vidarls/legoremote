package com.vidarls.legoremote;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class ChannelThree extends ChannelService {
    public ChannelThree() { super(3, ChannelThree.class); }
}
