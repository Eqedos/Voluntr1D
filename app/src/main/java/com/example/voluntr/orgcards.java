package com.example.voluntr;

public class orgcards {

    private String userId; //different from the one below
    private String name;
    private String profileImageUrl;
    public orgcards (String userId, String  name, String profileImageUrl){
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
    public void setName(String name){
        this.name=name;
    }
    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl=profileImageUrl;
    }
}
