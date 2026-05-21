package org.example.mccstudentrides.domain;
//where the ride is headed
public enum RideType {
    ELKHORN, FORT_OMAHA, SOUTH_OMAHA, APPLIED_TECH_CENTER, FREMONT_AREA_CENTER, SARPY_CENTER;

    public String toString(){
        return switch(this.ordinal()){
            case 0 -> "Elkhorn";
            case 1 -> "Fort Omaha";
            case 2 -> "South Omaha";
            case 3 -> "Applied Technology Center";
            case 4 -> "Fremont Area Center";
            case 5 -> "Sarpy Center";
            default -> "";
        };
    }
}
