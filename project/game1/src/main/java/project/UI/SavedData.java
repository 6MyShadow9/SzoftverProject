package project.UI;
import java.time.LocalDate;

@lombok.Data
public class SavedData {
    private String Player1Name;
    private String Player2Name;
    private String WinnerName;
    private int AmountOfMoves;
    private LocalDate DateOfGame;
}
