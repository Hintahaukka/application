package hifian.hintahaukka.Domain;

public class User {
    private String nickname;
    private int points;

    public User(String nickname, int points) {
        this.nickname = nickname;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public String getNickname() {
        return nickname;
    }

}
