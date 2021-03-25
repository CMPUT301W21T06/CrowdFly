package com.cmput301w21t06.crowdfly;

import com.cmput301w21t06.crowdfly.Models.User;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User mockUser(){
        String user_id = "mockID";
        //String contact_info = "04/05/98";
        //String display_ID = "dispTestID";
        User user = new User(user_id);
        return user;
    }

    @Test
    public void testGetUserID(){
        String trialID = "mockID";
        User user = mockUser();
        user.setUserID(trialID);

        assertEquals(0, trialID.compareTo(user.getUserID()));
    }

    @Test
    public void testGetDisplayID(){
        String dispID = "bigman";
        User user = mockUser();
        user.setDisplayID(dispID);

        assertEquals(0, "bigman".compareTo(user.getDisplayID()));
    }

    @Test
    public void testGetContactInfo(){
        String contactInfo = "04/05/98";
        User user = mockUser();
        user.setContactInfo(contactInfo);

        assertEquals(0, "04/05/98".compareTo(user.getContactInfo()));
    }

    @Test
    public void testToHashMap(){
        Map<String, Object> data = new HashMap<>();
        data.put("userID", "test_ID");
        data.put("contactInfo", "test_contact_info");
        data.put("displayID", "test_disp_ID");

        User user = new User(data);
        user.setDisplayID("test_disp_ID");
        assertEquals(data, user.toHashMap());
    }

}
