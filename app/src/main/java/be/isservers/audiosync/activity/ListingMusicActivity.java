package be.isservers.audiosync.activity;

import androidx.appcompat.app.AppCompatActivity;
import be.isservers.audiosync.R;
import be.isservers.audiosync.convert.Music;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ListingMusicActivity extends AppCompatActivity {

    ListView lv_list;
    ArrayList<Music> musicTab;
    boolean isHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_music);

        lv_list = findViewById(R.id.lv_list);

        Intent intent = getIntent();
        isHome = intent.getBooleanExtra("isHome",true);

        if (isHome){
            getSupportActionBar().setTitle("Acceuil");

            musicTab = new ArrayList<>();
            File directory = new File(Music.PathToMusic);
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile())
                        musicTab.add(new Music(file.getName()));
                }
                Collections.sort(musicTab);
            }
        }
        else {
            getSupportActionBar().setTitle(intent.getStringExtra("title"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            musicTab = (ArrayList<Music>) intent.getSerializableExtra("data");
            if (musicTab == null) musicTab = new ArrayList<>();
        }

        MusicAdapter musicAdapter = new MusicAdapter(this,musicTab);

        lv_list.setAdapter(musicAdapter);
        lv_list.setOnItemClickListener((AdapterView.OnItemClickListener) (parent, view, position, id) -> {
            if (isHome){
                Music music = musicTab.get(position);
                Intent openMusicPlayer = new Intent(ListingMusicActivity.this,MusicPlayer.class);
                openMusicPlayer.putExtra("music",(Serializable) music);
                startActivity(openMusicPlayer);
            }
            else {
                Toast.makeText(this,"Pas de lecture depuis cette endroit !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isHome){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.action_bar_support_menu,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            Intent activiy = new Intent(ListingMusicActivity.this, SynchronizationActivity.class);
            startActivity(activiy);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}