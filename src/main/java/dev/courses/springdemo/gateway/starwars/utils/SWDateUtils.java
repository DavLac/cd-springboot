package dev.courses.springdemo.gateway.starwars.utils;

import org.apache.commons.lang3.StringUtils;

public interface SWDateUtils {

    int CURRENT_DATE = 50;
    String UNKNOWN_DOB = "unknown";
    String BBY = "BBY";

    static Integer getAgeFromDateOfBirth(String dateOfBirth) {
        if (StringUtils.isEmpty(dateOfBirth) || UNKNOWN_DOB.equals(dateOfBirth)) {
            return null;
        }

        String battleOfYavinIndicator = dateOfBirth.substring(dateOfBirth.length() - 3);
        int year = Integer.parseInt(dateOfBirth.substring(0, dateOfBirth.length() - 3));

        if (BBY.equals(battleOfYavinIndicator)) {
            return CURRENT_DATE + year;
        } else {
            if(year >= CURRENT_DATE) {
                return year;
            }
            return CURRENT_DATE - year;
        }
    }
}
