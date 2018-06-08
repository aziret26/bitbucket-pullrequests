package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PullRequestControllerTest {
    PullRequestController prc;
    String repoSuccess = "aziret26/first-repo";
    String repoFail = "aziret26/no-repo";
    @BeforeEach
    void setUp() {
        prc = new PullRequestController();
    }

    /**
     * test for existing repository
     */
    @Test
    void setRepositorySuccess() {
        prc.setRepository(repoSuccess);
        boolean result = prc != null && !prc.getData().equals("");
        assertTrue(result);
    }

    /**
     * test for no existing repository
     */
    @Test
    void setRepositoryFail() {
        prc.setRepository(repoFail);
        boolean result = prc != null && !prc.getData().equals("");
        assertFalse(result);
    }
}