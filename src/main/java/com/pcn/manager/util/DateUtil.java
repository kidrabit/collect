package com.pcn.manager.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @class DateUtil
 * 	com.pcn.manager.util
 * @section 클래스작성정보
 *    |    항  목        	|       	내  용       			|
 *    | :--------: 	| -----------------------------	|
 *    | Company 	| PCN
 *    | Author 		| rnd
 *    | Date 		| 2019. 9. 27.
 *    | 작업자 		| rnd, Others...
 * @section 상세설명
 * - 클래스의 업무내용에 대해 기술...
*/
public class DateUtil {

    private String fromDate;
    private String toDate;

    private LocalDate curDate = LocalDate.now();

    /**
     * @fn String getFromDate()
     * @return the fromDate get
    */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @fn void setFromDate(String fromDate)
     * @param fromDate the fromDate to set
    */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @fn String getToDate()
     * @return the toDate get
    */
    public String getToDate() {
        return toDate;
    }

    /**
     * @fn void setToDate(String toDate)
     * @param toDate the toDate to set
    */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * @fn boolean isDateDiffer()
     * @return the dateDiffer get
    */
    public boolean isDateDiffer() {
        return dateDiffer;
    }

    /**
     * @fn void setDateDiffer(boolean dateDiffer)
     * @param dateDiffer the dateDiffer to set
    */
    public void setDateDiffer(boolean dateDiffer) {
        this.dateDiffer = dateDiffer;
    }

    private boolean dateDiffer = false;

    public void dateDiffer() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'");
        LocalDateTime from = LocalDateTime.parse(getFromDate(), formatter);
        LocalDateTime to = LocalDateTime.parse(getToDate(), formatter);

        if (Duration.between(from, to).toHours() > 3) {
            setDateDiffer(true);
        }
    }

    public void validateCheck(LocalDateTime dt) {
        if (dt.getSecond() != 0 || dt.getNano() == 0) {

        }
    }
}
