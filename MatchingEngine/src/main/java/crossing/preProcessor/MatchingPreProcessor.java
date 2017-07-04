package crossing.preProcessor;

import crossing.MatchingContext;

/**
 * Created by dharmeshsing on 7/07/15.
 */
public interface MatchingPreProcessor {
    enum MATCHING_ACTION {
        NO_ACTION,
        AGGRESS_ORDER,
        ADD_AND_AGGRESS,
        PARK_ORDER,
        ADD_ORDER;
    }

    void preProcess(MatchingContext context);
}
