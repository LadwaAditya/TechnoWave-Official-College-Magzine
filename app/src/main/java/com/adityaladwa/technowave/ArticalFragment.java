package com.adityaladwa.technowave;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * Created by AdityaLadwa on 16-Jul-15.
 */
public class ArticalFragment extends Fragment {

    private TextView tv;
    private TextToSpeech tts;
    private String toSpeak = "";

    public ArticalFragment() {

    }

    @Override
    public void onResume() {
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_article, container, false);


        Bundle extra = getArguments();

        Integer pos = extra.getInt("pos");

        tv = (TextView) rootView.findViewById(R.id.tvContent);
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(getResources().getAssets().open(pos + ".txt")));
            String line;

            while ((line = br.readLine()) != null) {
                tv.append(line);
                tv.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        toSpeak = tv.getText().toString();
        return rootView;
    }


    @Override
    public void onStop() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_article, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_listen) {
            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
