package seyah.co.uk.internshiptracker.pojo;

/**
 * Created by Ollie on 11/08/2017.
 */

public enum ApplicationStatus {

    NOT_STARTED,
    CV_CREATED,
    CV_SUBMITTED,
    APPLICATION_SUBMITTED,
    REFERRED,
    FIRST_INTERVIEW,
    SECOND_INTERVIEW,
    THIRD_INTERVIEW,
    SUCCEEDED,
    FAILED,
    WAITING;

    public static int statusAsPercentage(ApplicationStatus status) {
        switch (status) {
            case NOT_STARTED:
                return 0;
            case CV_CREATED:
                return 5;
            case CV_SUBMITTED:
                return 10;
            case APPLICATION_SUBMITTED:
                return 20;
            case REFERRED:
                return 20;
            case FIRST_INTERVIEW:
                return 60;
            case SECOND_INTERVIEW:
                return 65;
            case THIRD_INTERVIEW:
                return 70;
            case SUCCEEDED:
                return 100;
            case FAILED:
                return -1;
            case WAITING:
                return -2;
            default:
                return 0;
        }
    }

}
