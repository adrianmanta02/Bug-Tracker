package main.common;

public final class SystemConstants {
    private SystemConstants() { }

    public static final double THREE_DAY_INTERVAL = 3;

    public static final double NORMALIZATION_FACTOR = 100.0;
    public static final double ROUNDING_FACTOR = 100.0;

    public static final int TESTING_PHASE_DURATION = 12;
    public static final double CLOSED_TICKETS_COEFFICIENT = 0.5;

    public static final double HIGH_PRIORITY_COEFFICIENT = 0.7;

    public static final double SENIOR_RESOLUTION_TIME_PENALTY = 0.5;
    public static final double MID_RESOLUTION_TIME_PENALTY = 0.3;
    public static final double TICKET_TYPE_COUNT = 3.0;

    public static final double MAXIMUM_FEATURE_RISK = 20;
    public static final double MAXIMUM_BUG_RISK = 12;
    public static final double MAXIMUM_UI_USABILITY_SCORE = 11;

    public static final double MODERATE_RISK_THRESHOLD = 25.0;
    public static final double SIGNIFICANT_RISK_THRESHOLD = 50.0;
    public static final double MAJOR_RISK_THRESHOLD = 75.0;

    public static final double IMPACT_THRESHOLD = 50.0;
    public static final double MAXIMUM_UI_EFFICIENCY = 20;
    public static final double MAXIMUM_FEATURE_REQUEST_EFFICIENCY = 20;
    public static final double MAXIMUM_BUG_EFFICIENCY = 70;

    public static final double SEVERITY_COEFFICIENT = 10;

    public static final double MAXIMUM_BUG_IMPACT = 48;

    public static final int MINIMUM_COMMENT_LENGTH = 10;

    public static final int SENIOR_PERFORMANCE_BONUS = 30;
    public static final int MID_PERFORMANCE_BONUS = 15;
    public static final int JUNIOR_PERFORMANCE_BONUS = 5;

    public static final String SEARCH_TYPE_FIELD = "searchType";
    public static final String BUSINESS_PRIORITY_FIELD = "businessPriority";
    public static final String TYPE_FIELD = "type";
    public static final String CREATED_AT_FIELD = "createdAt";
    public static final String CREATED_BEFORE_FIELD = "createdBefore";
    public static final String CREATED_AFTER_FIELD = "createdAfter";
    public static final String AVAILABLE_FIELD = "availableForAssignment";
    public static final String KEYWORDS_FIELD = "keywords";
    public static final String EXPERTISE_AREA_FIELD = "expertiseArea";
    public static final String SENIORITY_FIELD = "seniority";
    public static final String PERFORMANCE_ABOVE_FIELD = "performanceScoreAbove";
    public static final String PERFORMANCE_BELOW_FIELD = "performanceScoreBelow";

    public static final String FILTERS_FIELD = "filters";
    public static final String RESULTS_FIELD = "results";
    public static final String TICKET_ID_FIELD = "ticketID";
    public static final String COMMENT_FIELD = "comment";
    public static final String ASSIGNED_TICKETS_FIELD = "assignedTickets";
    public static final String DEVELOPER_FIELD = "developer";
    public static final String MILESTONES_FIELD = "milestones";
    public static final String TICKETS_FIELD = "tickets";
    public static final String REPORT_FIELD = "report";
    public static final String COMMAND_FIELD = "command";

    public static final String ACTIVE_STATUS = "ACTIVE";
    public static final String COMPLETED_STATUS = "COMPLETED";


}
