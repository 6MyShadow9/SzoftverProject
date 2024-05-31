package project.UI;

import project.model.GameModel;
import project.model.Position;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.tinylog.Logger;

/**
 * Class, that helps in the JavaFX application to choose the moves.
 */
public class MoveManager{

    /**
     * Represents the phases of the selection of the positions for the moves
     */
    public enum Phase {
        SELECT_FROM,
        SELECT_TO,
        READY_TO_MOVE
    }

    private ReadOnlyObjectWrapper<Phase> phase = new ReadOnlyObjectWrapper<>(Phase.SELECT_FROM);

    private GameModel model;

    private Position from;
    private Position to;
    private boolean invalidSelection = false;

    public MoveManager(GameModel model) {
        this.model = model;
    }

    public ReadOnlyObjectProperty<Phase> phaseProperty() {
        return phase.getReadOnlyProperty();
    }



    //returns if the game is in the phase ready to move}
    public boolean isReadyToMove() {
        return phase.get() == Phase.READY_TO_MOVE;
    }


    public void select(Position p) {
        switch (phase.get()) {
            case SELECT_FROM -> selectFrom(p);
            case SELECT_TO -> selectTo(p);
            case READY_TO_MOVE -> throw new IllegalStateException();
        }
    }

    private void selectTo(Position p) {
        if (p.equals(from)) {
            Logger.debug("Same disk selected." + p + from);
            phase.set(Phase.SELECT_FROM);
        } else if (model.canMove(from, p)) {
            Logger.debug("Not the same disk selected." + p + from);
            to = p;
            phase.set(Phase.READY_TO_MOVE);
            invalidSelection = false;
        } else {
            Logger.debug("Not the same disk selected." + p + from);
            invalidSelection = true;
        }

    }

    private void selectFrom(Position p) {
        if (!model.isEmpty(p) && model.getField(p) == model.getPlayer().getColor()) {
            from = p;
            Logger.info(model.getPlayer().getColor() + " colored disk selected");
            phase.set(Phase.SELECT_TO);
            invalidSelection = false;
        } else {
            invalidSelection = true;
        }
    }


    //Performs the move
    public void makeMove() {
        if (!isReadyToMove()) {
            throw new IllegalStateException();
        }
        model.move(from, to);
        resetPhase();
    }


    public void resetPhase() {
        Logger.info("Move selector reset.");
        phase.set(Phase.SELECT_FROM);
        invalidSelection = false;
    }



    public Position getFrom() {
        if (phase.get() == Phase.SELECT_FROM && from == null) {
            throw new IllegalStateException();
        }
        return from;
    }

    public Position getTo() {
        if (!isReadyToMove()) {
            throw new IllegalStateException();
        }
        return to;
    }

}