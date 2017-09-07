package nigelmcintyre.login;

/**
 * Created by Nigel on 16/03/2016.
 */
public class Workout {
    public String workoutUser, workoutName,date,duration, time;


    public String getTime(){
        return time;
    }
    public String getWorkoutUser(){
        return workoutUser;
    }
    public String getWorkoutName(){
        return workoutName;
    }
    public String getDate(){
        return date;
    }
    public String getDuration(){
        return duration;
    }

    public void setWorkoutUser(String workoutUser){
        this.workoutUser = workoutUser;
    }
    public void setWorkoutName(String workoutName){
        this.workoutName = workoutName;
    }
    public void setDate(String date){
        this.date = date;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String toString(){
        return this.workoutName+"                  "+this.date;
    }
}
