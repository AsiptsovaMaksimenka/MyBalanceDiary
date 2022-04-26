package com.application.mybalancediary;

public class Steps {
    private int running = 1;
    private int firstStep = 0;

    public Steps() {
    }

    Steps(String representation) {
        String[] elements = representation.split(":");
        this.running = Integer.parseInt(elements[0]);
        this.firstStep = Integer.parseInt(elements[1]);
    }
    public boolean checkGoal(int steps, int goal){
        if(steps > goal){
            System.out.println("steps.checkGoal: You have reached your goal");
            return true;
        }
        else {
            return false;
        }
    }
    public int countSteps(int androidSteps){
        int mySteps;
        if(running == 1){
            firstStep = androidSteps;
            running = 0;
            return 0;
        }
        else{
            mySteps = (androidSteps - firstStep);
            System.out.println("system already running");
        }
        return mySteps;
    }
    public int returnPercentage(int goal, int steps) {
        if (steps != 0 && goal != 0) {
            System.out.println(goal + steps);
            return ((100 / goal) * steps);
        }
        return 0;
    }
}




