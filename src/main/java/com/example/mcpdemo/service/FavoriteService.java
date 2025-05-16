package com.example.mcpdemo.service;

import com.example.mcpdemo.pojo.Favorite;
import com.example.mcpdemo.pojo.FavoritedVideo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Classname: FavoriteService
 * Package: com.example.mcpdemo.service
 * Description:
 * Author: No_Ripple(吴波)
 * Creat： - 10:27
 */
@Service
public class FavoriteService {
//    String upMid = "20923983";
    List<FavoritedVideo> contents = new ArrayList<>();
    List<Favorite> favorites = new ArrayList<>();
    @Tool(name = "getFavoriteContents", description = "根据Bilibili收藏夹id和页号和页面大小获取收藏夹内容,每个元素包含视频标题,简介,链接。获取收藏夹内容时可以多次使用不同页号和页面大小调用以获取全部内容。页面大小限制为1到40.")
    public List<FavoritedVideo> getFavoriteContents(String mediaId, Integer pageNum, Integer pageSize) {
        String apiUrl = "https://api.bilibili.com/x/v3/fav/resource/list?" + "ps=" + pageSize + "&media_id=" + mediaId + "&pn=" + pageNum;


        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 将响应内容转换为字符串
                    String result = EntityUtils.toString(entity);
                    // 解析 JSON 响应
                    Gson gson = new Gson();
                    JsonObject bilibiliResponse = gson.fromJson(result, JsonObject.class);
                    JsonObject dataObject = bilibiliResponse.getAsJsonObject("data");
                    if (dataObject.get("medias") != null) {
                        JsonArray mediasArray = dataObject.getAsJsonArray("medias");
                        for (JsonElement mediaElement : mediasArray) {
                            JsonObject element = mediaElement.getAsJsonObject();
                            contents.add(new FavoritedVideo(
                                    element.get("title").getAsString(),
                                    element.get("intro").getAsString(),
                                    "https://www.bilibili.com/video/" + element.get("bvid").getAsString()
                            ));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }
    @Tool(name = "getFavorites", description = "根据Bilibili用户id获取收藏夹,每个元素包含收藏夹id,标题,收藏夹内视频数量。未知用户id时需要向用户询问并获取用户id。")
    public List<Favorite> getFavorites(String upMid) {
        String apiUrl = "https://api.bilibili.com/x/v3/fav/folder/created/list-all?up_mid=" + upMid;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(apiUrl);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 将响应内容转换为字符串
                    String result = EntityUtils.toString(entity);
                    // 解析 JSON 响应
                    Gson gson = new Gson();
                    JsonObject bilibiliResponse = gson.fromJson(result, JsonObject.class);
                    JsonObject dataObject = bilibiliResponse.getAsJsonObject("data");
                    JsonArray listArray = dataObject.getAsJsonArray("list");

                    for (JsonElement mediaElement : listArray) {
                        JsonObject element = mediaElement.getAsJsonObject();
                        favorites.add(new Favorite(
                                element.get("id").getAsString(),
                                element.get("title").getAsString(),
                                element.get("media_count").getAsInt()
                        ));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favorites;
    }



}
