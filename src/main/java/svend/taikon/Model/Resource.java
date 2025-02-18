package svend.taikon.Model;

import java.util.Objects;
import java.util.UUID;

public class Resource {
    private int flowers;
    private int wood;
    private int stone;
    private int sand;
    private UUID userId;

    public Resource(){

    }

    public Resource(int flowers, int wood, int stone, int sand, UUID userId) {
        this.flowers = flowers;
        this.wood = wood;
        this.stone = stone;
        this.sand = sand;
        this.userId = userId;
    }



    public int getFlowers() {
        return flowers;
    }

    public void setFlowers(int flowers) {
        this.flowers = flowers;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getStone() {
        return stone;
    }

    public void setStone(int stone) {
        this.stone = stone;
    }

    public int getSand() {
        return sand;
    }

    public void setSand(int sand) {
        this.sand = sand;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return flowers == resource.flowers && wood == resource.wood && stone == resource.stone && sand == resource.sand  && Objects.equals(userId, resource.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash( flowers, wood, stone, sand, userId);
    }
}
