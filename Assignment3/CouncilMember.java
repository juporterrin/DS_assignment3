class CouncilMember {
    private String name;
    private int responseTime;
    private boolean isOffline;
    private boolean isCandidate;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private int port;
    public CouncilMember(String name, int responseTime, boolean isOffline, boolean isCandidate) {
        this.name = name;
        this.responseTime = responseTime;
        this.isOffline = isOffline;
        this.isCandidate = isCandidate;
    }

    public String getName() {
        return name;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public boolean isCandidate() {
        return isCandidate;
    }
}