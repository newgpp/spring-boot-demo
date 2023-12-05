package com.felix.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @desprication: some desc
 * @author: felix
 * @date: 2023/6/16 9:36
 */
public class Attributes {

    private String doe;

    private BigDecimal pi;

    private Boolean xmas;

    private Integer frenchHens;

    private List<String> callingBirds;

    private XmasFifthDay xmasFifthDay;

    public static class XmasFifthDay {

        private String callingBirds;

        private Integer frenchHens;

        public String getCallingBirds() {
            return callingBirds;
        }

        public void setCallingBirds(String callingBirds) {
            this.callingBirds = callingBirds;
        }

        public Integer getFrenchHens() {
            return frenchHens;
        }

        public void setFrenchHens(Integer frenchHens) {
            this.frenchHens = frenchHens;
        }
    }

    public String getDoe() {
        return doe;
    }

    public void setDoe(String doe) {
        this.doe = doe;
    }

    public BigDecimal getPi() {
        return pi;
    }

    public void setPi(BigDecimal pi) {
        this.pi = pi;
    }

    public Boolean getXmas() {
        return xmas;
    }

    public void setXmas(Boolean xmas) {
        this.xmas = xmas;
    }

    public Integer getFrenchHens() {
        return frenchHens;
    }

    public void setFrenchHens(Integer frenchHens) {
        this.frenchHens = frenchHens;
    }

    public List<String> getCallingBirds() {
        return callingBirds;
    }

    public void setCallingBirds(List<String> callingBirds) {
        this.callingBirds = callingBirds;
    }

    public XmasFifthDay getXmasFifthDay() {
        return xmasFifthDay;
    }

    public void setXmasFifthDay(XmasFifthDay xmasFifthDay) {
        this.xmasFifthDay = xmasFifthDay;
    }
}
