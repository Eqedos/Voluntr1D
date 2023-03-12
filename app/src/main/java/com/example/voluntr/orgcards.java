package com.example.voluntr;

public class orgcards {

    private String userId; //different from the one below
    private String name;
    public orgcards (String userId, String  name){
        this.userId = userId;
        this.name = name;
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
}
