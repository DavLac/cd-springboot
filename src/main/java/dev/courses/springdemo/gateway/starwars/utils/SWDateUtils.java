package dev.courses.springdemo.gateway.starwars.utils;

import org.apache.commons.lang3.StringUtils;

public interface SWDateUtils {

    int DEFAULT_ZERO_ABY_AGE = 50;
    String UNKNOWN_DOB = "unknown";
    String BBY = "BBY";

    /**
     * If current date = 50
     * date of birth 0 ABY/BBY = 50 years old
     * date of birth 10 ABY = 40 years old
     * date of birth 5 BBY = 55 years old
     */
    static Integer getAgeFromDateOfBirth(String dateOfBirth) {
        if (StringUtils.isEmpty(dateOfBirth) || UNKNOWN_DOB.equals(dateOfBirth)) {
            return null;
        }

        String battleOfYavinIndicator = dateOfBirth.substring(dateOfBirth.length() - 3);
        int year = Integer.parseInt(dateOfBirth.substring(0, dateOfBirth.length() - 3));

        if (BBY.equals(battleOfYavinIndicator)) {
            return DEFAULT_ZERO_ABY_AGE + year;
        } else {
            if(year >= DEFAULT_ZERO_ABY_AGE) {
                return year;
            }
            return DEFAULT_ZERO_ABY_AGE - year;
        }
    }
}
