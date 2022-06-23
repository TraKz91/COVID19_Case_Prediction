public class Date implements Comparable<Date>{

    int day;
    int month;
    int year;

    public Date(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(String dateData){
        String[] date = dateData.split("-\\s*");
        this.year = Integer.parseInt(date[0]);
        this.month = Integer.parseInt(date[1]);
        this.day = Integer.parseInt(date[2]);
    }

    Date incrementDate(){
        int incDay = this.day + 1;
        int incMonth = this.month;
        int incYear = this.year;
        if(incMonth == 2){
            if(isLeap(incYear) && incDay > 29 || !isLeap(incYear) && incDay > 28){
                {
                    incDay = 1;
                    incMonth = 3;
                }
            }
        } else if (incMonth == 4 || incMonth == 6 || incMonth == 9 || incMonth == 11){
            if (incDay > 30){
                incDay = 1;
                incMonth++;
            }
        } else if (incDay > 31){
            incDay = 1;
            incMonth++;
        }
        if(incMonth > 12){
            incMonth = 1;
            incYear++;
        }
        return new Date(incDay,incMonth,incYear);
    }

    boolean isLeap(int year){
        if( year % 400 == 0){
            return true;
        } else return year % 4 == 0 && year % 100 != 0;
    }

    public String toString(){
        return year +  "-" + month + "-" + day;
    }

    @Override
    public int compareTo(Date o) {
        if(o.year < this.year){
            return -1;
        } else if(o.year > this.year){
            return 1;
        } else {
            if(o.month < this.month){
                return -1;
            } else if(o.month > this.month){
                return 1;
            } else {
                if(o.day < this.day){
                    return -1;
                } else if (o.day > this.day){
                    return 1;
                } else return 0;
            }
        }
    }
}
