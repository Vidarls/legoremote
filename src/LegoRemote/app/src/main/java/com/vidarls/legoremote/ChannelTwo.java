package com.vidarls.legoremote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChannelTwo extends ChannelService {
    public ChannelTwo() { super(2, ChannelTwo.class);}
}
