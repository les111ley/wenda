package com.wenda.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Create by xrh
 * 2:19 PM on 12/16/19 2019
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LikeServiceTest {
    @Autowired
    LikeService likeService;

    @Test
    public void testLike(){
        System.out.println("testLIke");
        likeService.like(123,1,1);
        Assert.assertEquals(1, likeService.getLikeStatus(123,1,1));

        //likeService.dislike(123,1,1);
        //Assert.assertEquals(-1, likeService.getLikeStatus(123,1,1));

    }

}