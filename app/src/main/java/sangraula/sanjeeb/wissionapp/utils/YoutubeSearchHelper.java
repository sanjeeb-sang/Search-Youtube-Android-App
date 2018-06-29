package sangraula.sanjeeb.wissionapp.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import sangraula.sanjeeb.wissionapp.models.YoutubeVideoItem;

public class YoutubeSearchHelper {

    private static final int NUM_OF_VIDEOS_IN_ONE_REQUEST = 10;
    public static final String API_KEY = YoutubeApiUtils.API_KEY;

    private static final String URL_FRONT_PART = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=";
    private static final String URL_BACK_PART = "&type=video&maxResults=" + NUM_OF_VIDEOS_IN_ONE_REQUEST + "&order=date&key=" + API_KEY;

    private OnSearchCompleteListener mListener;

    public final URL getRequestUrl(String searchFor) {

        URL url = null;

        try {

            url = new URL(getUrlString(searchFor));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;

    }


    private final String getUrlString(String searchFor) {

        return URL_FRONT_PART + handleSearchString(searchFor) + URL_BACK_PART;

    }


    private String handleSearchString(String s) {

        if (s != null && s.length() != 0) {
            return s;

        } else {

            return "comedy";
        }
    }

    public final void searchYoutube(String searchFor, @NonNull OnSearchCompleteListener listener) {

        mListener = listener;

        URL url = getRequestUrl(searchFor);

        SearchTask st = new SearchTask();
        st.execute(url);
    }

    private class SearchTask extends AsyncTask<URL, Void, ArrayList<YoutubeVideoItem>> {

        @Override
        protected ArrayList<YoutubeVideoItem> doInBackground(URL... urls) {

            URL url = urls[0];

            StringBuilder response = new StringBuilder();

            HttpURLConnection httpconn = null;

            try {
                httpconn = (HttpURLConnection) url.openConnection();

                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<YoutubeVideoItem> videos = new ArrayList<>();

            try {

                JSONObject o1 = new JSONObject(response.toString());

                JSONArray a1 = o1.getJSONArray("items");

                videos.clear();

                for (int i = 0; i < a1.length(); i++) {

                    JSONObject o2 = a1.getJSONObject(i);

                    String videoId = o2.getJSONObject("id").getString("videoId");

                    String title = o2.getJSONObject("snippet").getString("title");

                    String thumbnailUrl = o2.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");

                    YoutubeVideoItem video = new YoutubeVideoItem(title, videoId, thumbnailUrl, 0);

                    videos.add(video);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return videos;
        }


        @Override
        protected void onPostExecute(ArrayList<YoutubeVideoItem> youtubeVideoItems) {
            super.onPostExecute(youtubeVideoItems);

            mListener.onSearchComplete(youtubeVideoItems);

            GetLikesTask task = new GetLikesTask();
            task.execute(youtubeVideoItems);
        }
    }

    private class GetLikesTask extends AsyncTask<ArrayList<YoutubeVideoItem>, Void, ArrayList<YoutubeVideoItem>> {


        @Override
        protected ArrayList<YoutubeVideoItem> doInBackground(ArrayList<YoutubeVideoItem>... arrayLists) {

            ArrayList<YoutubeVideoItem> videos = arrayLists[0];

            for (int i = 0; i < videos.size(); i++) {

                YoutubeVideoItem v = videos.get(i);

                v.likes = getLikes(v.videoId);

                videos.set(i, v);
            }
            return videos;

        }

        public int getLikes(String videoId) {

            String urlString = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=" + videoId + "&key=" + API_KEY;

            String likes = "0";

            try {
                URL url = new URL(urlString);

                StringBuilder s = new StringBuilder();

                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(c.getInputStream()), 8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        s.append(strLine);
                    }
                    input.close();
                }

                try {
                    JSONObject o1 = new JSONObject(s.toString());

                    JSONArray a1 = o1.getJSONArray("items");

                    JSONObject o2 = (JSONObject) a1.get(0);

                    likes = o2.getJSONObject("statistics").getString("likeCount");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return Integer.valueOf(likes);

        }

        @Override
        protected void onPostExecute(ArrayList<YoutubeVideoItem> youtubeVideoItems) {
            super.onPostExecute(youtubeVideoItems);

            mListener.onGetLikesTaskFinished(youtubeVideoItems);
        }
    }


    public interface OnSearchCompleteListener {
        void onSearchComplete(ArrayList<YoutubeVideoItem> videos);
        void onGetLikesTaskFinished(ArrayList<YoutubeVideoItem> videos);
    }

}
