package com.savantspender.util;

import com.savantspender.R;
import com.savantspender.db.entity.Goal;

public class ColorSystemDecider {
    private static double WARN_THRESHOLD = 0.2; // within 20% of hitting goal

    public enum GoalState {
        GOOD ,
        WARN,
        BAD
    }

    public static int getColorId(GoalState state) {
        switch (state) {
            case GOOD:
                return R.color.colorGoalGood;
            case WARN:
                return R.color.colorGoalWarning;
            case BAD:
            default:
                return R.color.colorGoalBad;
        }
    }

    public static GoalState getState(Goal goal) {
        if (goal.getPredicted() > goal.getGoalAmount())
            if (goal.getR() > 0.6)
                return GoalState.BAD;
            else return GoalState.WARN; // doesn't look good, but not a very good prediction either

        double warnThreshold = goal.getGoalAmount() * (1 - WARN_THRESHOLD);

        if (goal.getPredicted() > warnThreshold)
            return GoalState.WARN;

        if (goal.getTotalSpending() > warnThreshold)
            return GoalState.WARN;

        return GoalState.WARN;
    }

}
