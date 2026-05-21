package org.example.mccstudentrides.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Data;
import java.util.Base64;

@Embeddable
public class Image {
    private String imageName;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] imageContent;
    public Image(){
        imageName = "";
        imageContent = new byte[0];
    }

    public Image(String imageName, byte[] imageContent){
        this.imageName = imageName;
        this.imageContent = imageContent;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageContent() {
        return imageContent;
    }

    public void setImageContent(byte[] imageContent) {
        this.imageContent = imageContent;
    }

    public String getBase64Image(){
        return Base64.getEncoder().encodeToString(imageContent);
    }

    public String getEncoding(){
        return imageName.substring(imageName.lastIndexOf(".") + 1);
    }
}
