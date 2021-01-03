package com.example.burgerlist;

public class ImageUpload {
    String imgName, imgUri;

    public ImageUpload(){}
    public ImageUpload( String imgName, String imgUri){
        this.imgName = imgName;
        this.imgUri = imgUri;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }


}
