package org.example.mccstudentrides.web;

import org.example.mccstudentrides.domain.Image;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public class CarImageByMultipartConverter implements Converter<MultipartFile, Image>{
    @Override
    public Image convert(MultipartFile source){
        Image carImage = new Image();
        if(source != null && !source.isEmpty()){
            String encoding = source.getOriginalFilename().substring(source.getOriginalFilename().lastIndexOf("."));
            try{
                carImage = new Image("ride" + encoding, source.getBytes());
            }catch(IOException e){
                System.out.println(e);
            }
        }
        return carImage;
    }
}
