package nigelmcintyre.login;

/**
 * Created by Nigel on 16/03/2016.
 */
public class ExEntry {
    public String exName, uName, exWorkout, date;
    public int exNum, reps, sets;
    public double weight;

    public double getWeight(){
        return weight;
    }
    public String getExWorkout(){
        return exWorkout;
    }
    public String getDate(){
        return date;
    }
    public String getExName(){
        return exName;
    }
    public int getExNum(){
        return exNum;
    }
    public int getReps(){
        return reps;
    }
    public int getSets(){
        return sets;
    }
    public String getuName(){
        return uName;
    }
    public void setExName(String exName){
        this.exName = exName;
    }
    public void setExNum(int exNum){
        this.exNum = exNum;
    }
    public void setReps(int reps){
        this.reps = reps;
    }
    public void setSets(int sets){
        this.sets = sets;
    }
    public void setuName(String uName){
        this.uName = uName;
    }
    public void setWeight(double weight){
        this.weight = weight;
    }
    public void setExWorkout(String exWorkout){
        this.exWorkout = exWorkout;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String toString(){
        return this.exName+"            "+this.sets+"     "+this.reps+"    "+this.weight;
    }
}
