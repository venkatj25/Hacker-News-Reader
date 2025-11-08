package com.jv.hnreader.net;

import android.util.JsonReader;
import android.util.JsonToken;

import com.jv.hnreader.models.Story;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SearchJsonReader {

    List<Story> readSearchResults(InputStream stream) throws IOException {
        try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(stream)))) {
            List<Story> storyList = null;
            //begin parsing json object
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if ("hits".equals(name)) {
                    storyList = readStoryArray(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            //throw exception if story list is null or empty
            if (storyList == null || storyList.size() == 0) {
                throw new IOException("Error parsing HN Algolia Search api json response");
            }
            return storyList;
        }
    }

    private List<Story> readStoryArray(JsonReader reader) throws IOException{
        List<Story> storyList = new ArrayList<Story>(30);
        reader.beginArray();
        while (reader.hasNext()) {
            storyList.add(readStory(reader));
        }
        reader.endArray();
        return storyList;
    }

    private Story readStory(JsonReader reader) throws IOException{
        int id = 0;
        int commentsCount = 0;
        int score = 0;
        long time = 0;
        String title = null;
        String link = null;
        String userId = null;
        String text = null;
        String linkDomain = null;
        List<Integer> commentIdList = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            // skip value if it's null
            if (reader.peek() == JsonToken.NULL) {
                reader.skipValue();
                continue;
            }

            switch (name) {
                case "title":
                    title = reader.nextString();
                    break;
                case "url":
                    link = reader.nextString();
                    break;
                case "author":
                    userId = reader.nextString();
                    break;
                case "points":
                    score = reader.nextInt();
                    break;
                case "story_text":
                    text = reader.nextString();
                    break;
                case "num_comments":
                    commentsCount = reader.nextInt();
                    break;
                case "objectID":
                    id = reader.nextInt();
                    break;
                case "created_at_i":
                    time = reader.nextLong();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        if (id == 0 || title == null) throw new IOException("Insufficient data");
        linkDomain = getLinkDomain(link);

        return new Story(String.valueOf(id),
                String.valueOf(score),
                title,
                String.valueOf(commentsCount),
                link,
                String.valueOf(time),
                userId,
                linkDomain,
                text,
                commentIdList);

    }

    private String getLinkDomain(String link) {
        try {
            URI uri = new URI(link);
            String host = uri.getHost();
            return host.startsWith("www.") ? host.substring(4) : host;
        } catch (Exception e) {
            return null;
        }
    }
}
