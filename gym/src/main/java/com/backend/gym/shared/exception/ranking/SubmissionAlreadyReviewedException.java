package com.backend.gym.shared.exception.ranking;

import com.backend.gym.shared.exception.domain.BusinessException;

public class SubmissionAlreadyReviewedException extends BusinessException {
    public SubmissionAlreadyReviewedException() {
        super("Submission is already reviewed");
    }
}