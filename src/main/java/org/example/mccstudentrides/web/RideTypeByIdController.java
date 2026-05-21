package org.example.mccstudentrides.web;

import org.example.mccstudentrides.domain.RideType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RideTypeByIdController implements Converter<String, RideType>{
    @Override
    public RideType convert(String type){
        if(type.equals("Fort Omaha")){
            return RideType.FORT_OMAHA;
        }else if(type.equals("South Omaha")){
            return RideType.SOUTH_OMAHA;
        }else if(type.equals("Applied Technology Center")){
            return RideType.APPLIED_TECH_CENTER;
        }else if(type.equals("Fremont Area Center")){
            return RideType.FREMONT_AREA_CENTER;
        }else if(type.equals("Sarpy Center")){
            return RideType.SARPY_CENTER;
        }else{
            return RideType.valueOf(type.toUpperCase());
        }
    }
}
