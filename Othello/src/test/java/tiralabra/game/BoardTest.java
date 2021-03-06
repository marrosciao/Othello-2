/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiralabra.game;

import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tiralabra.game.ai.AI;
import tiralabra.game.ai.AI.Move;
import tiralabra.utilities.BoardUtilities;
import tiralabra.utilities.ZobristHash;

/**
 *
 * @author atte
 */
public class BoardTest {

    Board board = new Board();
    int[][] verticalTestBoard;
    int[][] horizontalTestBoard;
    int[][] diagonalTestBoard1;
    int[][] diagonalTestBoard2;
    AI ai;

    public BoardTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        verticalTestBoard = new int[][]{{0, 1, 2, 0, 0, 2, 1, 0}};
        horizontalTestBoard = new int[][]{{0}, {1}, {2}, {0}, {0}, {2}, {1}, {0}};
        diagonalTestBoard1 = new int[][]{{0, 0, 0, 0},
        {0, 1, 1, 0},
        {0, 2, 2, 0},
        {0, 0, 0, 0}};
        diagonalTestBoard2 = new int[][]{{0, 0, 0, 0},
        {0, 2, 2, 0},
        {0, 1, 1, 0},
        {0, 0, 0, 0}};
        ai = new AI(board);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void placingABlackPiecePutsAPieceAtTheRightSpot() {
        board.setBoard(BoardUtilities.createPlayerTable(verticalTestBoard), Player.BLACK);

        assertFalse(board.place(3, 0, Player.BLACK, true) == 0);
        assertEquals(Player.BLACK, board.getTile(3, 0));
    }

    @Test
    public void placingAWhitePiecePutsAPieceAtTheRightSpot() {
        board.setBoard(BoardUtilities.createPlayerTable(verticalTestBoard), Player.WHITE);

        assertFalse(board.place(0, 0, Player.WHITE, true) == 0);
        assertEquals(Player.WHITE, board.getTile(0, 0));
    }

    @Test
    public void placingBlacksFlipsPiecesVertically() {
        board.setBoard(BoardUtilities.createPlayerTable(verticalTestBoard), Player.WHITE);

        assertEquals(2, board.place(3, 0, Player.BLACK, true));
        assertEquals(2, board.place(4, 0, Player.BLACK, true));

        assertEquals(6, board.blackPieces());
        assertEquals(0, board.whitePieces());
    }

    @Test
    public void placingWhitesFlipsPiecesVertically() {
        board.setBoard(BoardUtilities.createPlayerTable(verticalTestBoard), Player.WHITE);

        assertEquals(2, board.place(0, 0, Player.WHITE, true));
        assertEquals(2, board.place(7, 0, Player.WHITE, true));
        assertEquals(6, board.whitePieces());
        assertEquals(0, board.blackPieces());
    }

    @Test
    public void placingBlacksFlipsPiecesHorizontally() {
        board.setBoard(BoardUtilities.createPlayerTable(horizontalTestBoard), Player.WHITE);

        assertEquals(2, board.place(0, 3, Player.BLACK, true));
        assertEquals(2, board.place(0, 4, Player.BLACK, true));
        assertEquals(6, board.blackPieces());
        assertEquals(0, board.whitePieces());
    }

    @Test
    public void placingWhitesFlipsPiecesHorizontally() {
        board.setBoard(BoardUtilities.createPlayerTable(horizontalTestBoard), Player.WHITE);

        assertEquals(2, board.place(0, 0, Player.WHITE, true));
        assertEquals(2, board.place(0, 7, Player.WHITE, true));
        assertEquals(6, board.whitePieces());
        assertEquals(0, board.blackPieces());
    }

    @Test
    public void placingBlacksFlipsPiecesDiagonally1() {
        board.setBoard(BoardUtilities.createPlayerTable(diagonalTestBoard1), Player.BLACK);

        assertEquals(2, board.place(0, 3, Player.BLACK, true));
        assertEquals(2, board.place(3, 3, Player.BLACK, true));
        assertEquals(6, board.blackPieces());
        assertEquals(0, board.whitePieces());
    }

    @Test
    public void placingBlacksFlipsPiecesDiagonally2() {
        board.setBoard(BoardUtilities.createPlayerTable(diagonalTestBoard2), Player.BLACK);

        assertEquals(2, board.place(0, 0, Player.BLACK, true));
        assertEquals(2, board.place(3, 0, Player.BLACK, true));
        assertEquals(6, board.blackPieces());
        assertEquals(0, board.whitePieces());
    }

    @Test
    public void placingWhitesFlipsPiecesDiagonally1() {
        board.setBoard(BoardUtilities.createPlayerTable(diagonalTestBoard2), Player.WHITE);

        assertEquals(2, board.place(0, 3, Player.WHITE, true));
        assertEquals(2, board.place(3, 3, Player.WHITE, true));
        assertEquals(6, board.whitePieces());
        assertEquals(0, board.blackPieces());
    }

    @Test
    public void placingWhitesFlipsPiecesDiagonally2() {
        board.setBoard(BoardUtilities.createPlayerTable(diagonalTestBoard1), Player.WHITE);

        assertEquals(2, board.place(0, 0, Player.WHITE, true));
        assertEquals(2, board.place(3, 0, Player.WHITE, true));
        assertEquals(6, board.whitePieces());
        assertEquals(0, board.blackPieces());
    }

    @Test
    public void undoingFlipsWorks() {
        AI ai = new AI(board);
        ZobristHash hasher = new ZobristHash(8, 8);
        for (int i = 0; i < 40; i++) {

            BigInteger original = hasher.hash(board.getBoard());

            long move = ai.selectRandomMove(board.playerInTurn());
            board.placeTile(ai.selectRandomMove(board.playerInTurn()));
            board.undo();

            assertEquals(original, hasher.hash(board.getBoard()));

            board.placeTile(move);
        }

    }

    @Test
    public void checkingWhetherYouCanPlaceAPieceWorks() {
        board.setBoard(BoardUtilities.createPlayerTable(verticalTestBoard), Player.BLACK);
        assertTrue(board.canPlace(0, 0, Player.WHITE));
        assertTrue(board.canPlace(3, 0, Player.BLACK));
    }

    @Test
    public void checkingWhetherYouCanPlacePiecesAtAllWorks() {
        assertTrue(board.canMove(Player.WHITE));
        assertTrue(board.canMove(Player.BLACK));
    }

    @Test
    public void undoingWorksAgainstPasses() {
        board.placeTile(3, 5);

        ZobristHash hasher = new ZobristHash(8, 8);
        BigInteger hash = hasher.hash(board.getBoard());
        assertEquals(hash, hasher.hash(board.getBoard()));

        board.pass();
        board.undo();

        assertEquals("Undoing passes should keep the board the same", hash, hasher.hash(board.getBoard()));
    }

    @Test
    public void placingATileChangesTurn() {
        Player player = Player.BLACK;

        for (int i = 0; i < 40; i++) {
            assertEquals(player, board.playerInTurn());
            long move = ai.selectRandomMove(board.playerInTurn());

            if (move == -1) board.pass();
            else board.placeTile(move);
            
            player = Player.opposing(player);
        }
    }

    @Test
    public void incrementalHashesWork() {
        ZobristHash hasher = board.getHasher();
        for (int i = 0; i < 40; i++) {
            long move = ai.selectRandomMove(board.playerInTurn());
            
            if (move == -1) board.pass();
            else board.placeTile(move);

            BigInteger excepted = hasher.hash(board.getBoard());
            assertEquals(excepted, board.getHash());
        }
    }

    @Test
    public void countingPiecesWorks() {
        AI ai = new AI(board);
        assertEquals(4, board.getNumberOfPieces());
        int i = 0;
        while (!board.gameOver()) {
            assertEquals(4 + i, board.getNumberOfPieces());

            long move = ai.selectRandomMove(board.playerInTurn());
            if (move == - 1) {
                board.pass();
            } else {
                board.placeTile(move);
                i++;
            }
        }
    }

}
