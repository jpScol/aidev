package aicogdev.agent;

import aicogdev.interaction.Interaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentTP2 extends Agent {
    private ValuationSystem valuationSystem = new ValuationSystem(new int[]{
            1, 1,
            1, -1,
            -1, 1
    });

    private Map<Integer, Integer> attentes = new HashMap<>();

    private int actionActuellementExploree = 1;
    private int numberOfTimesRight;

    public AgentTP2() {
    }


    @Override
    protected Interaction getDecision() {
        return new Interaction(actionActuellementExploree, attentes.getOrDefault(actionActuellementExploree, 0));
    }

    @Override
    protected String[] processReaction(int action, int expectedFeedback, int actualFeedback) {
        int recompense = valuationSystem.getValue(action, actualFeedback);

        if (Objects.equals(expectedFeedback, actualFeedback)) {
            numberOfTimesRight++;

            boolean isBored = false;

            if (numberOfTimesRight == 3) {
                isBored = true;

                actionActuellementExploree = changerDAction();

                numberOfTimesRight = 0;
            }

			return new String[] { (isBored ? "Ennuyé" : "Content") + " ; " + recompense };
        } else {
            attentes.put(action, actualFeedback);
            numberOfTimesRight = 0;


			return new String[] { "Surpris ; " + recompense};
        }
    }

    private int changerDAction() {
        // Exploration
        for (int i = 1 ; i <= 3 ; i++) {
            if (!attentes.containsKey(i)) {
                return i;
            }
        }

        // Exploitation
        int bestAction = 0;
        int bestValue = Integer.MIN_VALUE;

        for (int i = 1 ; i <= 3 ; i++) {
            if (i == actionActuellementExploree) {
                continue;
            }

            int valuation = evaluerValeurAttendue(i);

            if (bestAction == 0 || bestValue < valuation) {
                bestAction = i;
                bestValue = valuation;
            }
        }

        return bestAction;
    }

    private int evaluerValeurAttendue(int i) {
        return valuationSystem.getValue(i, attentes.get(i));
    }

}
