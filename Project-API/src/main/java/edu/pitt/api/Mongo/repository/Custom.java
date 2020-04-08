package edu.pitt.api.Mongo.repository;

public class Custom {
//    private String state;
//    private String county;
//    private int total;

    public Custom(){};



    public class CountState{
        private String state;
        private int total;
        public int getTotal(){return total;}
        public void setTotal(int total) { this.total = total; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
//
    }

    public class CountCounty{
        private String county;
        private int total;
        public String getCounty(){return county;}

        public void setCounty(String county) { this.county = county; }
        public int getTotal(){return total;}
        public void setTotal(int total) { this.total = total; }
    }

    public class CountVis{
        private String visibility;
        private int total;

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }

        public String getVisibility() { return visibility; }
        public void setVisibility(String visibility){this.visibility = visibility;}
    }

    public class CountHumid{
        private String humidity;
        private int total;
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }

        public String getHumidity() { return humidity; }
        public void setHumidity(String humidity){this.humidity=humidity;}
    }

    public class CountWeather{
        private String weather_condition;
        private int total;
        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }

        public String getWeather_condition() { return weather_condition; }
        public void setWeather_condition(String weather_condition){this.weather_condition=weather_condition;}
    }

}
