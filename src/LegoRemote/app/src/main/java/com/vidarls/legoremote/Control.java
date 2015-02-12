package com.vidarls.legoremote;

import android.content.Context;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class Control extends ActionBarActivity {
    private boolean isClicked;

    private Button.OnTouchListener fwdTouch = new Button.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Intent i = new Intent(v.getContext() ,ChannelOne.class);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                i.setAction(ChannelService.ACTION_RED_FWD);
                i.putExtra(ChannelService.EXTRA_SPEED, 7);
                startService(i);

                return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                i.setAction(ChannelService.ACTION_RED_STOP);
                startService(i);
                return false;
            }
            return false;
        }
    };
    private Button.OnTouchListener revTouch = new Button.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Intent i = new Intent(v.getContext() ,ChannelOne.class);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                i.setAction(ChannelService.ACTION_RED_REV);
                i.putExtra(ChannelService.EXTRA_SPEED, 7);
                startService(i);

                return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                i.setAction(ChannelService.ACTION_RED_STOP);
                startService(i);
                return false;
            }
            return false;
        }
    };
    private Button.OnTouchListener leftTouch = new Button.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Intent i = new Intent(v.getContext() ,ChannelTwo.class);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                i.setAction(ChannelService.ACTION_BLUE_FWD);
                i.putExtra(ChannelService.EXTRA_SPEED, 7);
                startService(i);

                return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                i.setAction(ChannelService.ACTION_BLUE_STOP);
                startService(i);
                return false;
            }
            return false;
        }
    };
    private Button.OnTouchListener rightTouch = new Button.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Intent i = new Intent(v.getContext() ,ChannelTwo.class);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                i.setAction(ChannelService.ACTION_BLUE_REV);
                i.putExtra(ChannelService.EXTRA_SPEED, 7);
                startService(i);

                return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                i.setAction(ChannelService.ACTION_BLUE_STOP);
                startService(i);
                return false;
            }
            return false;
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Button fwd = (Button) findViewById(R.id.button);
        fwd.setOnTouchListener(fwdTouch);
        Button rev = (Button) findViewById(R.id.button2);
        rev.setOnTouchListener(revTouch);
        Button left = (Button) findViewById(R.id.button3);
        left.setOnTouchListener(leftTouch);
        Button right = (Button) findViewById(R.id.button4);
        right.setOnTouchListener(rightTouch);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
