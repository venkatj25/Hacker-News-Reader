package com.jv.hnreader.net;

import android.util.JsonReader;

import com.jv.hnreader.models.Story;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

class StoryJsonReader {

    List<Integer> readStoryIds(InputStream stream) throws IOException {
        try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(stream)))) {
            List<Integer> idList = new ArrayList<>(500);
            reader.beginArray();
            for (int i = 0; reader.hasNext() && i < 500; i++) {
                idList.add(i, reader.nextInt());
            }
            reader.endArray();
            return idList;
        }
    }

    Story readStory(InputStream stream) throws IOException, URISyntaxException {
        try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(stream)))) {
            reader.beginObject();
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
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "by":
                        userId = reader.nextString();
                        break;
                    case "descendants":
                        commentsCount = reader.nextInt();
                        break;
                    case "id":
                        id = reader.nextInt();
                        break;
                    case "kids":
                        commentIdList = readCommentIds(reader);
                        break;
                    case "score":
                        score = reader.nextInt();
                        break;
                    case "time":
                        time = reader.nextLong();
                        break;
                    case "title":
                        title = reader.nextString();
                        break;
                    case "url":
                        link = reader.nextString();
                        break;
                    case "text":
                        text = reader.nextString();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            if (id == 0 || title == null) throw new IOException();
            if (link != null) {
                URI uri = new URI(link);
                String host = uri.getHost();
                linkDomain = host.startsWith("www.") ? host.substring(4) : host;
            }

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
    }

    private List<Integer> readCommentIds(JsonReader reader) throws IOException{
        List<Integer> idList = new ArrayList<>();
        reader.beginArray();
        for (int i = 0; reader.hasNext() ; i++) {
            idList.add(i, reader.nextInt());
        }
        reader.endArray();
        return idList;
    }
}
