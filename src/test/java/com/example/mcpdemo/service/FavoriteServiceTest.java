package com.example.mcpdemo.service;

import org.junit.jupiter.api.Test;

/**
 * Classname: FavoriteServiceTest
 * Package: com.example.mcpdemo.service
 * Description:
 *
 * @Author: No_Ripple(吴波)
 * @Creat： - 11:03
 */
class FavoriteServiceTest {

    @Test
    void getFavorite() {
        FavoriteService favoriteService = new FavoriteService();
        favoriteService.getFavorites("20923983");
    }

    @Test
    void getFavoriteContents() {
        FavoriteService favoriteService = new FavoriteService();
        String mediaId = favoriteService.getFavorites("20923983").get(6).id();
        favoriteService.getFavoriteContents(mediaId, 1, 10);
    }
}