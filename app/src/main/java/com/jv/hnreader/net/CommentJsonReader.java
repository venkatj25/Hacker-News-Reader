package com.jv.hnreader.net;

import android.util.JsonReader;
import android.util.Log;

import com.jv.hnreader.datamodels.Comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommentJsonReader {

    Comment readComment(InputStream inputStream) throws IOException{
        try (JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)))){
            reader.beginObject();
            int commentId = 0;
            int parentId = 0;
            long time = 0;
            List<Integer> nestedCommentIds = null;
            String userId = null;
            String text = null;
            while (reader.hasNext()){
                String name = reader.nextName();
                switch (name) {
                    case "by":
                        userId = reader.nextString();
                        break;
                    case "id":
                        commentId = reader.nextInt();
                        break;
                    case "kids":
                        nestedCommentIds = readNestedCommentIds(reader);
                        break;
                    case "parent":
                        parentId = reader.nextInt();
                        break;
                    case "text":
                        text = reader.nextString();
                        break;
                    case "time":
                        time = reader.nextLong();
                        break;
                    default:
                        reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            if (commentId == 0 || parentId == 0) throw new IOException();

            return new Comment(String.valueOf(commentId),
                    userId,
                    String.valueOf(time),
                    text,
                    nestedCommentIds,
                    String.valueOf(parentId));
        }
    }

    private List<Integer> readNestedCommentIds(JsonReader reader) throws IOException{
        List<Integer> idList = new ArrayList<>();
        reader.beginArray();
        for (int i = 0; reader.hasNext() ; i++) {
            idList.add(i, reader.nextInt());
        }
        reader.endArray();
        return idList;
    }
}
