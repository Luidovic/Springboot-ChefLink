package com.SoftwareEngineeringProject.demo.subEntity;

public class FoodOption {
    private String option_name;
    private double option_price;
    private String option_type;

    public FoodOption(String option_name, double option_price, String option_type) {
        this.option_name = option_name;
        this.option_price = option_price;
        this.option_type = option_type;
    }

    public String getOption_name() {
        return option_name;
    }

    public void setOption_name(String option_name) {
        this.option_name = option_name;
    }

    public double getOption_price() {
        return option_price;
    }

    public void setOption_price(double option_price) {
        this.option_price = option_price;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setOption_type(String option_type) {
        this.option_type = option_type;
    }

}
