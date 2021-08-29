package com.example.meetmee;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class DashboardScreen extends AppCompatActivity {

    EditText secretCode,meetingsubjectBox;
    Button joinBtn,startBtn;
    TextView meetingIdBox;
    boolean videoFlagBox_start,audioFlagBox_start,videoFlagBox_join,audioFlagBox_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);

        secretCode=findViewById(R.id.codeBox);
        joinBtn=findViewById(R.id.joinBtn);
        startBtn=findViewById(R.id.startBtn);
        meetingIdBox=findViewById(R.id.meetingId);
        String meetingId = getIntent().getStringExtra("meetingId");
        meetingIdBox.setText(meetingId);
        JitsiMeetUserInfo userInfo=new JitsiMeetUserInfo();
        userInfo.setDisplayName(getIntent().getStringExtra("loggeduser"));


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secretCode.getText().toString().trim().length() > 0) {
                    if (!(secretCode.getText().toString().trim().equals(meetingIdBox.getText().toString().trim()))){

                        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardScreen.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.preferences_joinmeeting, viewGroup, false);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            videoFlagBox_join = ((Switch) alertDialog.findViewById(R.id.videoFlag_join)).isChecked();
                            audioFlagBox_join = ((Switch) alertDialog.findViewById(R.id.audioFlag_join)).isChecked();
                            URL serverURL;
                            try {
                                serverURL = new URL("https://meet.jit.si");
                                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                        .setServerURL(serverURL)
                                        .setRoom(secretCode.getText().toString())
                                        .setAudioMuted(!(audioFlagBox_join))
                                        .setVideoMuted(!(videoFlagBox_join))
                                        .setUserInfo(userInfo)
                                        .setWelcomePageEnabled(false)
                                        .setFeatureFlag("invite.enabled",false)
                                        .setFeatureFlag("meeting-password.enabled",false)
                                        .setFeatureFlag("add-people.enabled", false)
                                        .setFeatureFlag("live-streaming.enabled", false)
                                        .setFeatureFlag("help.enabled",false)
                                        .setFeatureFlag("lobby-mode.enabled",false)
                                        .setFeatureFlag("server-url-change.enabled", false)
                                        .setFeatureFlag("kick-out.enabled",false)
                                        .build();
                                JitsiMeetActivity.launch(DashboardScreen.this, options);
                                Toast.makeText(getApplicationContext(),"Joining...", Toast.LENGTH_SHORT).show();

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }



                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();

                }else {
                        Toast.makeText(DashboardScreen.this, "You cannot join your own meeting!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(DashboardScreen.this, "Please enter secret code!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(DashboardScreen.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.preferences_startmeeting, viewGroup, false);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        videoFlagBox_start = ((Switch) alertDialog.findViewById(R.id.videoFlag_start)).isChecked();
                        audioFlagBox_start = ((Switch) alertDialog.findViewById(R.id.audioFlag_start)).isChecked();
                        meetingsubjectBox = (EditText) alertDialog.findViewById(R.id.meetingSubject);

                        if (meetingsubjectBox.getText().toString().trim().length()>0){
                            URL serverURL;
                            try {
                                serverURL = new URL("https://meet.jit.si");
                                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                        .setServerURL(serverURL)
                                        .setRoom(meetingIdBox.getText().toString())
                                        .setAudioMuted(!(audioFlagBox_start))
                                        .setVideoMuted(!(videoFlagBox_start))
                                        .setUserInfo(userInfo)
                                        .setSubject(meetingsubjectBox.getText().toString().trim())
                                        .setFeatureFlag("help.enabled",false)
                                        .setFeatureFlag("invite.enabled",false)
                                        .setFeatureFlag("calendar.enabled",true)
                                        .setWelcomePageEnabled(false)
                                        .build();
                                JitsiMeetActivity.launch(DashboardScreen.this, options);

                                Toast.makeText(getApplicationContext(), "Starting...", Toast.LENGTH_SHORT).show();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "Please enter meeting subject!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CLOSE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                    alertDialog.show();


            }
        });


    }

}