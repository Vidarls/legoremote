package com.vidarls.legoremote;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public abstract class ChannelService extends IntentService {
    private static final int _frequency = 38000;
    private static final int _maxMsgDuration = 16;

    private final int _channel;
    private final int _channelCode;
    private final int _initialDelay;
    private final int _firstDelay;
    private final int _secondDelay;
    private final Class _class;
    private ConsumerIrManager _ir;
    private int _blueSpeedCode;
    private int _redSpeedCode;
    private int _blueIteration;
    private int _redIteration;
    private boolean _blueStopping;
    private boolean _redStopping;
    private int _currentCode;
    private int _currentIteration;


    public static final String ACTION_BLUE_FWD = "com.vidarls.legoremote.action.blue.fwd";
    public static final String ACTION_BLUE_REV = "com.vidarls.legoremote.action.blue.rev";
    public static final String ACTION_BLUE_STOP = "com.vidarls.legoremote.action.blue.stop";
    public static final String ACTION_RED_FWD = "com.vidarls.legoremote.action.red.fwd";
    public static final String ACTION_RED_REV = "com.vidarls.legoremote.action.red.rev";
    public static final String ACTION_RED_STOP = "com.vidarls.legoremote.action.red.stop";

    private static final String ACTION_REPEAT = "com.vidarls.legoremote.action.repeat";

    public static final String EXTRA_SPEED = "com.vidarls.legoremote.extra.speed";
    public static final String EXTRA_CODE = "com.vidarls.legoremote.extra.code";

    public ChannelService(int channel, Class c) {
        super("ChannelService" + String.valueOf(channel));
        _channel = channel;
        _channelCode = LegoRcProtocol.getChannelCode(channel);
        _initialDelay = (4 - channel) * _maxMsgDuration;
        _firstDelay = 5 * _maxMsgDuration;
        _secondDelay = (6 + 2 * channel) * _maxMsgDuration;
        _class = c;
        _redStopping = true;
        _blueStopping = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _ir = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            try {
                if (ACTION_REPEAT.equals(action)) {
                    final int param = intent.getIntExtra(EXTRA_CODE, 0);
                    if (param == 0) return;
                    if (param != _currentCode) return;
                    _currentIteration = _currentIteration + 1;
                    _blueIteration = _blueIteration + 1;
                    _redIteration = _redIteration + 1;
                    if (_blueStopping && _redStopping && (_redIteration > 4) && (_blueIteration > 4 )) return;
                    try {
                        Thread.sleep(getDelay(_currentIteration));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    transmitCode(param);
                    startService(intent);
                } else if (ACTION_BLUE_STOP.equals(action)) {
                    _blueSpeedCode = LegoRcProtocol.BRK_FLOAT;
                    _currentCode = getCurrentCode();
                    _blueStopping = true;
                    _redIteration = _redIteration + 1;
                    _currentIteration = 0;
                   Thread.sleep(_initialDelay);
                   transmitCode(_currentCode);
                   repeatCode(_currentCode);
                } else if (ACTION_RED_STOP.equals(action)) {
                    _redSpeedCode = LegoRcProtocol.BRK_FLOAT;
                    _currentCode = getCurrentCode();
                    _redStopping = true;
                    _blueIteration = _blueIteration + 1;
                    _currentIteration = 0;
                    Thread.sleep(_initialDelay);
                    transmitCode(_currentCode);
                    repeatCode(_currentCode);
                } else if (ACTION_BLUE_FWD.equals(action) || ACTION_BLUE_REV.equals(action) ||
                           ACTION_RED_FWD.equals(action) || ACTION_RED_REV.equals(action)) {
                    _currentIteration = 0;
                    final int param = intent.getIntExtra(EXTRA_SPEED,-1);
                    if (param == -1) return;
                    int speedCode = 0;
                    if (ACTION_BLUE_FWD.equals(action) || ACTION_RED_FWD.equals(action)) {
                        speedCode = LegoRcProtocol.getForwardSpeedCode(param);
                    } else if (ACTION_BLUE_REV.equals(action) || ACTION_RED_REV.equals(action)) {
                        speedCode = LegoRcProtocol.getReverseSpeed(param);
                    }
                    if (ACTION_BLUE_FWD.equals(action) || ACTION_BLUE_REV.equals(action)) {
                        _blueSpeedCode = speedCode;
                        _blueStopping = false;
                        _blueIteration = 0;
                        _redIteration = _redIteration + 1;
                    } else if (ACTION_RED_FWD.equals(action) || ACTION_RED_REV.equals(action)) {
                        _redSpeedCode = speedCode;
                        _redStopping = false;
                        _redIteration= 0;
                        _blueIteration = _blueIteration + 1;
                    }
                    _currentCode = getCurrentCode();
                    Thread.sleep(_initialDelay);
                    transmitCode(_currentCode);
                    repeatCode(_currentCode);
                }
            } catch (InterruptedException e) {
                Log.e("CHANNEL", "WHUT", e);
                e.printStackTrace();
            }
        }
    }

    private void repeatCode(int code) {
        Intent i = new Intent(this, _class);
        i.setAction(ACTION_REPEAT);
        i.putExtra(EXTRA_CODE, code);
        startService(i);
    }

    private int getDelay(int iteration) {
        if (iteration == 1) return _firstDelay;
        if (iteration == 2) return _firstDelay;
        return _secondDelay;
    }

    private int getCurrentCode() {
       return LegoRcProtocol.getPwmCode(_channelCode, _blueSpeedCode, _redSpeedCode);
    }

    private void transmitCode(int code) {
        _ir.transmit(_frequency, LegoRcProtocol.generatePattern(code));
    }
}
