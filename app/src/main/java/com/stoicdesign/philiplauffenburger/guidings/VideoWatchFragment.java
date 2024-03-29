package com.stoicdesign.philiplauffenburger.guidings;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class VideoWatchFragment extends Fragment implements View.OnClickListener {
    String user;
    String userId;
    String annoId,title,keywords,time;
//    Context _this = this;
    int likes;
    TextView usertxt;
    TextView liketxt;
    boolean likeTruth =false;
    int altLikes;
    ImageView heart;
    TableRow keywordRow;
    Uri uri;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args= getArguments();
        userId = args.getString("userId");
        time = args.getString("time");
        keywords = args.getString("keywords");
        likes = args.getInt("likes");
        likeTruth = args.getBoolean("likeTruth");
        title = args.getString("title");
        annoId = args.getString("annoId");
        uri = Uri.parse(args.getString("uri"));
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View videoWatchFragment = inflater.inflate(R.layout.video_watch_card, container, false);

        RelativeLayout likeLayout = (RelativeLayout) videoWatchFragment.findViewById(R.id.like_layout);
        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!likeTruth) {
                    Toast.makeText(getActivity(), "You have liked this video", Toast.LENGTH_SHORT).show();
                    heart.setImageResource(R.drawable.liked_heart);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Annotation");


                    query.getInBackground(annoId, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                altLikes++;
//                        int i=altLikes;
//                        Integer intObj = new Integer(i);
//                        Number numObj = intObj;
                                String newlikes = String.valueOf(altLikes);
                                liketxt.setText(newlikes);
                                likeTruth = true;
                                object.put("numberOfLikes", altLikes);
                                object.saveInBackground();
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                currentUser.add("likedGuides",annoId);
                                currentUser.saveInBackground();
                            } else {
                                // something went wrong
                            }
                        }
                    });
                } else{
                    Toast.makeText(getActivity(), "You have un-liked this video", Toast.LENGTH_SHORT).show();
                    heart.setImageResource(R.drawable.unliked_grey_heart_hdpi);

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Annotation");


                    query.getInBackground(annoId, new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                altLikes--;
                                String newlikes = String.valueOf(altLikes);
                                liketxt.setText(newlikes);
                                likeTruth = false;
                                object.put("numberOfLikes", altLikes);
                                object.saveInBackground();
                                ParseUser currentUser = ParseUser.getCurrentUser();
                                List<String> s = new ArrayList<String>() {
                                    {
                                        add(annoId);
                                    }
                                };
                                currentUser.removeAll("likedGuides", s);
                                currentUser.saveInBackground();
                            } else {
                                // something went wrong
                            }
                        }
                    });
                }




            }


        });

        return videoWatchFragment;

    }
//        setContentView(R.layout.activity_video_watch);
//        v.setVisibility(View.VISIBLE);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
public void onViewCreated(View view, Bundle savedInstanceState){
//        user= getIntent().getExtras().get("user").toString();

        heart = (ImageView) view.findViewById(R.id.like_icon_watch);
        keywordRow = (TableRow) view.findViewById(R.id.keyword_table_row_video);
        if (likeTruth){
            heart.setImageResource(R.drawable.liked_heart);
        }
//        likeTruth=likeTruthsent;
        altLikes =likes;
       String mlikes=String.valueOf(likes);
        int lengthStrings = keywords.length();
        keywords = keywords.substring(1,lengthStrings-1);




        TextView titletxt = (TextView)view.findViewById(R.id.video_title);
       usertxt =  (TextView)view.findViewById(R.id.uploaded_by);
//                usertxt.bringToFront();
         liketxt= (TextView) view.findViewById(R.id.like_count_watch);
        TextView timetxt = (TextView) view.findViewById(R.id.upload_time);
                liketxt.setText(mlikes);
                titletxt.setText(title);
        usertxt.setOnClickListener(this);
//        usertxt.setOnClickListener(new View.OnClickListener() {

//            @Override

//            public void onClick(View v) {
//                Log.v("VIEWCLICKED", v.toString());
//
////                if (v.getId() == R.id.uploaded_by) {
//                    Intent i = new Intent(_this,UserActivity.class);
//                    i.putExtra("user", user);
//                    i.putExtra("userId",userId);
//                startActivity(i);
////                }
//            }
//        });
//        Log.v("TIME", time);
//        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy, HH:mm");
//        Date date = null;
//        try {
//           date  = formatter.parse(time);
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//
//        SimpleDateFormat formatFix = new SimpleDateFormat("dd-MMM-yy");
//        String timefix = formatFix.format(date);
        timetxt.setText(time);
        ArrayList<String> keywordArray = new ArrayList<String>(Arrays.asList(keywords.split(", ")));
    Iterator it = keywordArray.iterator();
        int i= 0;
        while (it.hasNext()){

            String text= it.next().toString();
            TextView newKeyword = (TextView) getActivity().getLayoutInflater().inflate(R.layout.keyword_text_view, null);
            newKeyword.setText(text);
            GradientDrawable gdDefault = new GradientDrawable();
            gdDefault.setColor(Color.parseColor("#eeeeee"));
            gdDefault.setCornerRadius(100);
            gdDefault.setStroke(5, Color.parseColor("#000000"));
            newKeyword.setPadding(20, 20, 20, 20);
            newKeyword.setBackground(gdDefault);
            keywordRow.addView(newKeyword, i);
            i++;


        }

        final VideoView video = (VideoView) view.findViewById(R.id.video_player);
        video.setVideoURI(uri);

        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                          /*
                                           *  add media controller
                                           */
                        MediaController mc = new MediaController(getActivity());
                        ;
                        video.setMediaController(mc);
                                          /*
                                           * and set its position on screen
                                           */
                        mc.setAnchorView(video);
                    }
                });
            }
        });
//        video.setMediaController(new MediaController(this));
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", userId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    //List contain object with specific user id.

                    user = objects.get(0).getString("nickname");
                    if (user==null){
                        user = objects.get(0).getUsername();
                    }
                    SpannableString content = new SpannableString(user);
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    usertxt.setText(content);


                    video.start();
                } else {
                    // error
                }
            }
        });


    }
//    public void onClick(View v) {
//        Log.v("VIEW", v.toString());
//
////        if (v.getId() == R.id.uploaded_by) {
//            Intent i = new Intent(_this,UserActivity.class);
//            i.putExtra("user", user);
//            i.putExtra("userId",userId);
//        startActivity(i);
////        }
//    }
public void onClick(View v) {

    Intent i = new Intent(getActivity(),UserActivity.class);
//    i.putExtra("user", user);
    i.putExtra("userId",userId);
    startActivity(i);
//                }
}






}
