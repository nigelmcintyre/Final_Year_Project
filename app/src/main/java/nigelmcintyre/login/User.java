package nigelmcintyre.login;

/**
 * Created by Nigel on 13/02/2016.
 */
public class User {

    public int id, age;
    public String name, surname, email, password, sex;
    public double BMR,TDEE, height, weight, activityLevel, goal, carbs, fats, protein;


    public User(){

    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getSurname(){
        return surname;
    }

    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getSex(){
        return sex;
    }
    public double getBMR(){
        return BMR;
    }
    public double getGoal(){
        return goal;
    }

    public double getTDEE(){
        return TDEE;
    }
    public double getHeight(){
        return height;
    }
    public double getWeight(){
        return weight;
    }

    public double getCarbs(){
        return carbs;
    }
    public double getFats(){
        return fats;
    }
    public double getProtein(){
        return protein;
    }
    public double getActivityLevel(){
        return activityLevel;
    }
    public int getAge(){
        return age;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    public void setBMR(double BMR){
        this.BMR = BMR;
    }
    public void setTDEE(double TDEE){
        this.TDEE = TDEE;
    }

    public void setHeight(double height){
        this.height = height;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }
    public void setActivityLevel(double activityLevel){
        this.activityLevel = activityLevel;
    }
    public void setGoal(double goal){
        this.goal = goal;
    }
    public void setCarbs(double carbs){
        this.carbs = carbs;
    }
    public void setFats(double fats){
        this.fats = fats;
    }
    public void setProtein(double protein){
        this.protein = protein;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
