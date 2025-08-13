package de.coin.kniffel.model.dto;

public class GameResultDTO {

    private Integer position;
    private String playerName;
    private Integer finalScore;
    private Double contribution;

    public GameResultDTO(Integer position, String playerName, Integer finalScore) {
        this.position = position;
        this.playerName = playerName;
        this.finalScore = finalScore;
        this.contribution = 0.0;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    public Double getContribution() {
        return contribution;
    }

    public void setContribution(Double contribution) {
        this.contribution = contribution;
    }

    @Override
    public String toString() {
        return "GameResultDTO{" +
                "position=" + position +
                ", playerName='" + playerName + '\'' +
                ", finalScore=" + finalScore +
                ", contribution=" + contribution +
                '}';
    }
}
