package com.telegame.code.models.kingolaser;

import com.telegame.code.models.kingolaser.pieces.Bouncer;
import com.telegame.code.models.kingolaser.pieces.Piece;
import com.telegame.code.models.kingolaser.pieces.PieceSide;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LaserBeam {

    Piece.Direction direction;
    List<int[]> route;

    public void setStep(int[] step) {
        this.route.add(step);
    }

    public List<int[]> shootLaser(int player_id, Piece.Direction direction, Object[][] board) {

        int[] currentPosition;
        List<int[]> route = new ArrayList<>();

        if(player_id == 1) {
            currentPosition = new int[]{9, 7};
        } else {
            currentPosition = new int[]{0, 0};
        }

        while ( currentPosition[0] >= 0 && currentPosition[0] <= 9 &&
                currentPosition[1] >= 0 && currentPosition[1] <= 7) {

            int[] newYX = forward(direction, currentPosition);
            int posY = newYX[0];
            int posX = newYX[1];


            if(board[posY][posX] instanceof Piece) {
                Piece piece = (Piece) board[posY][posX];
                PieceSide pieceSide = new Block();

                switch (direction) {
                    case NORTH:
                        pieceSide = piece.getSide(Piece.Direction.SOUTH);
                        break;
                    case EAST:
                        pieceSide = piece.getSide(Piece.Direction.WEST);
                        break;
                    case SOUTH:
                        pieceSide = piece.getSide(Piece.Direction.NORTH);
                        break;
                    case WEST:
                        pieceSide = piece.getSide(Piece.Direction.EAST);
                        break;
                }

                boolean bouncer = (piece instanceof Bouncer);

                Piece.Direction nextDirection = pieceSide.interact(direction, piece.getRotation(), bouncer);

                if(nextDirection == null) return route;
                else {
                    direction = nextDirection;
                    currentPosition = newYX;
                    route.add(newYX);
                }

            } else {
                board[posY][posX] = "  /\\  ";
                currentPosition[0] = posY;
                currentPosition[1] = posX;
                route.add(new int[]{posY, posX});
                if(posY == 0 && direction == Piece.Direction.NORTH || posY == 9 && direction == Piece.Direction.SOUTH) {
                    return route;
                }
                if(posX == 0 && direction == Piece.Direction.WEST || posX == 7 && direction == Piece.Direction.EAST){
                    return route;
                }


            }
        }
        return route;
    }

    public int[] forward(Piece.Direction direction, int[] currentPosition) {
        int posY = currentPosition[0];
        int posX = currentPosition[1];
        switch (direction) {
            case NORTH:
                posY--;
                break;
            case EAST:
                posX++;
                break;
            case SOUTH:
                posY++;
                break;
            case WEST:
                posX--;
        }
        return new int[]{posY, posX};
    }

}