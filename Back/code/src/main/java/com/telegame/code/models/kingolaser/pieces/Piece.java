package com.telegame.code.models.kingolaser.pieces;

import com.telegame.code.builder.PieceBuilder;
import com.telegame.code.models.Player;
import com.telegame.code.models.kingolaser.LaserBoard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Piece implements Movable {

    public enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    public enum Owner {
        UNASSIGNED, PLAYER_ONE, PLAYER_TWO
    }

    @Id
    @GeneratedValue
    Long id;

    @Enumerated(EnumType.STRING)
    private Owner owner;
    private int posX;
    private int posY;
    @Enumerated(EnumType.STRING)
    private Direction rotation;
    @Embedded
    private Map<Direction, PieceSide> sides;

    @ManyToOne
    @JoinColumn(name = "laser_board_id")
    LaserBoard laserBoard;

    public void setSide(Direction side, PieceSide pieceSide) {
        this.sides.put(side, pieceSide);
    }

    public PieceSide getSide(Direction side) {
        return this.sides.get(side);
    }


    @Override
    public void rotate(String rotateTo) {
        if(Objects.equals(rotateTo, "R")) {
            switch(this.rotation) {
                case NORTH -> this.rotation = Direction.EAST;
                case EAST -> this.rotation = Direction.SOUTH;
                case SOUTH -> this.rotation = Direction.WEST;
                case WEST -> this.rotation = Direction.NORTH;
            }
        }
        if(Objects.equals(rotateTo, "L")) {
            switch(this.rotation) {
                case NORTH -> this.rotation = Direction.WEST;
                case EAST -> this.rotation = Direction.NORTH;
                case SOUTH -> this.rotation = Direction.EAST;
                case WEST -> this.rotation = Direction.SOUTH;
            }
        }
        if(this.getClass() == Deflector.class) this.setSides(PieceBuilder.buildDeflectorSides(this.rotation));
        if(this.getClass() == Defender.class) this.setSides(PieceBuilder.buildDefenderSides(this.rotation));
    }

    @Override
    public boolean move(int[] nextPosition) {
        int nextY = nextPosition[0];
        int nextX = nextPosition[1];

        boolean validateY = (nextY == this.posY || nextY == this.posY +1 || nextY == this.posY -1);
        boolean validateX = (nextX == this.posX || nextX == this.posX +1 || nextX == this.posX -1);

        if(validateY && validateX) {
            this.posY = nextY;
            this.posX = nextX;
            return true;
        } else {
            System.out.println("Movimiento incorrecto");
            return false;
        }
    }
}