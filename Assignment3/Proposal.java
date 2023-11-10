public class Proposal {
    private int sequenceNumber;
    private String content;
    private int votes;
    private boolean isAccepted;
    private String proposer;

    public Proposal(int sequenceNumber, String content, String proposer) {
        this.sequenceNumber = sequenceNumber;
        this.content = content;
        this.votes = 0;
        this.isAccepted = false;
        this.proposer = proposer;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public String getContent() {
        return content;
    }

    public int getVotes() {
        return votes;
    }

    public void incrementVotes() {
        votes++;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public String getProposer() {
        return proposer;
    }
}