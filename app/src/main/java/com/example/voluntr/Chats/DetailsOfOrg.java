package com.example.voluntr.Chats;

public class DetailsOfOrg {
    private String userId;
    private String name;
    private String profileImageUrl;



    public DetailsOfOrg (String userId, String name, String profileImageUrl){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    // getter
    public String getUserId(){
        return userId;
    }
    //setter
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getName(){
        return name;
    }
    //setter
    public void setName(String name){
        this.name = name;
    }
    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    //setter
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
