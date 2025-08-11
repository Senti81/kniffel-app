package de.coin.kniffel.model.dto;

public class TotalPlayerScoreDTO {

    private Integer position;
    private String playerName;
    private Integer totalScore;

    public TotalPlayerScoreDTO(Integer position, String playerName, Integer totalScore) {
        this.position = position;
        this.playerName = playerName;
        this.totalScore = totalScore;
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

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return "TotalPlayerScoreDTO{" +
                "position=" + position +
                "playerName='" + playerName + '\'' +
                ", totalScore=" + totalScore +
                '}';
    }
}
