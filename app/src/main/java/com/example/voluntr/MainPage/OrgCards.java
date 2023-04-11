package com.example.voluntr.MainPage;

public class OrgCards {

    private String userId; //different from the one below
    private String name;
    private String profileImageUrl;

    private String age;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public OrgCards(String userId, String  name, String profileImageUrl, String age){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.age=age;

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
