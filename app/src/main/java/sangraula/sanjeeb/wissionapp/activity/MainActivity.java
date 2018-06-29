package sangraula.sanjeeb.wissionapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sangraula.sanjeeb.wissionapp.R;
import sangraula.sanjeeb.wissionapp.utils.YoutubeSearchHelper;
import sangraula.sanjeeb.wissionapp.models.YoutubeVideoItem;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv = findViewById(R.id.tv);

        YoutubeSearchHelper helper = new YoutubeSearchHelper();
        helper.searchYoutube("comedy", new YoutubeSearchHelper.OnSearchCompleteListener() {
            @Override
            public void onSearchComplete(ArrayList<YoutubeVideoItem> videos) {
                for (YoutubeVideoItem v : videos) {

                    tv.append("\n" + v.title + "   " + v.videoId + "   " + v.thumbnailUrl + "     " + v.likes +   "\n");
                }
            }

            @Override
            public void onGetLikesTaskFinished(ArrayList<YoutubeVideoItem> videos) {

                tv.setText("");

                for (YoutubeVideoItem v : videos) {

                    tv.append("\n" + v.title + "   " + v.videoId + "   " + v.thumbnailUrl + "     " + v.likes +   "\n");
                }
            }
        });
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {


            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
