package com.cmput301w21t06.crowdfly.Database;

import junit.framework.TestCase;

import org.junit.Test;

public class CrowdFlyFirestorePathsTest extends TestCase {

    private String mockUserID(){
        return "12345";
    }

    @Test
    public void testUserProfile() {

        assertEquals(CrowdFlyFirestorePaths.userProfile(mockUserID()), "/Users/12345");

    }
}